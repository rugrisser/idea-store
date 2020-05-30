package edu.mshp.ideastore.service.impl;

import edu.mshp.ideastore.exception.BadRequestException;
import edu.mshp.ideastore.exception.ForbiddenException;
import edu.mshp.ideastore.exception.InternalServerErrorException;
import edu.mshp.ideastore.exception.NotFoundException;
import edu.mshp.ideastore.model.User;
import edu.mshp.ideastore.module.jwt.JWTToken;
import edu.mshp.ideastore.respository.UserCrudRepository;
import edu.mshp.ideastore.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.security.InvalidKeyException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserCrudRepository userCrudRepository;

    public UserServiceImpl(
            UserCrudRepository userCrudRepository
    ) {
        this.userCrudRepository = userCrudRepository;
    }

    @Override
    public User get(Long id) {
        Optional<User> userOptional = userCrudRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        return userOptional.get();
    }

    @Override
    public User get(String token) {
        if (validate(token)) {
            token = removeTokenPrefix(token);
            JWTToken jwtToken = null;
            try {
                jwtToken = new JWTToken(token);
            } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException e) {
                log.error("An error occured while parsing encrypted JWT token");
                throw new InternalServerErrorException();
            }

            Optional<User> userOptional = userCrudRepository.findById(jwtToken.getId());
            if (userOptional.isEmpty()) {
                log.error("User with ID {} not found", jwtToken.getId());
                throw new InternalServerErrorException();
            }
            return userOptional.get();
        } else {
            throw new BadRequestException("Token is invalid");
        }
    }

    @Override
    public String create(String login, String email, String password) {
        List<User> users = userCrudRepository.findAllByLoginOrEmail(login, email);
        if (users.size() > 0) {
            throw new BadRequestException("User with given credentials already created");
        }
        if (!checkEmail(email)) {
            throw new BadRequestException("E-Mail is invalid");
        }
        if (!checkLogin(login)) {
            throw new BadRequestException("Login is invalid. The length must be bigger than 6 and smaller than 24 symbols");
        }
        if (!checkPassword(password)) {
            throw new BadRequestException("Password is invalid. The length must be bigger than 8 and smaller than 120 symbols. Use digits and characters from latin script");
        }

        User user = new User(login, email, password);
        userCrudRepository.save(user);
        JWTToken token = new JWTToken(user);
        return token.createToken();
    }

    @Override
    public String authorize(String login, String password) {
        List<User> users = userCrudRepository.findAllByLoginOrEmail(login, login);

        if (users.size() == 0) {
            throw new NotFoundException("User not found");
        }
        User user = users.get(0);
        if (!user.comparePassword(password)) {
            throw new BadRequestException("Password is incorrect");
        }

        JWTToken token = new JWTToken(user);

        return token.createToken();
    }

    @Override
    public void changePassword(String token, String oldPassword, String newPassword) {
        if (oldPassword.equals(newPassword)) {
            throw new BadRequestException("Current and new passwords are equal");
        }
        if (!checkPassword(newPassword)) {
            throw new BadRequestException("New password is invalid. The length must be bigger than 8 and smaller than 120 symbols. Use digits and characters from latin script");
        }
        if (validate(token)) {
            token = removeTokenPrefix(token);
            JWTToken jwtToken = null;
            try {
                jwtToken = new JWTToken(token);
            } catch (BadPaddingException | InvalidKeyException | IllegalBlockSizeException e) {
                throw new ForbiddenException("Token is invalid");
            }

            Optional<User> userOptional = userCrudRepository.findById(jwtToken.getId());
            if (userOptional.isEmpty()) {
                throw new ForbiddenException("User with given id not found");
            }
            User user = userOptional.get();
            if (!user.comparePassword(oldPassword)) {
                throw new BadRequestException("Current password is not equal");
            }

            user.setPassword(newPassword);
            userCrudRepository.save(user);

        } else {
            throw new ForbiddenException("Token is invalid");
        }
    }

    public Boolean validate(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return false;
        }

        token = token.substring(7);
        JWTToken jwtToken = null;
        try {
            jwtToken = new JWTToken(token);
        } catch (BadPaddingException | InvalidKeyException | IllegalBlockSizeException e) {
            return false;
        }
        Date now = new Date();

        return now.after(jwtToken.getIssued()) && now.before(jwtToken.getExpiration());
    }

    public String removeTokenPrefix(String token) {
        return token.substring(7);
    }

    private Boolean checkPassword(String password) {
        Integer length = password.length();

        if (length < 8 || length > 120) {
            return false;
        }
        return password.matches("[0-9A-Za-z]{" + length + "}");
    }

    private Boolean checkLogin(String login) {
        Integer length = login.length();

        if (length < 6 || length > 24) {
            return false;
        }
        if (login.contains("@")) {
            return false;
        }

        return true;
    }

    private Boolean checkEmail(String email) {
        if (!email.contains("@") || !email.contains(".")) {
            return false;
        }
        if (email.indexOf('@') != email.lastIndexOf('@')) {
            return false;
        }
        if (email.indexOf('@') > email.lastIndexOf('.')) {
            return false;
        }
        if (email.charAt(email.indexOf('@') + 1) == '.') {
            return false;
        }
        if (email.lastIndexOf('.') + 1 == email.length()) {
            return false;
        }
        return email.indexOf('@') != 0;
    }

}
