package edu.mshp.ideastore.service.impl;

import edu.mshp.ideastore.exception.BadRequestException;
import edu.mshp.ideastore.exception.NotFoundException;
import edu.mshp.ideastore.model.User;
import edu.mshp.ideastore.module.jwt.JWTToken;
import edu.mshp.ideastore.respository.UserCrudRepository;
import edu.mshp.ideastore.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserCrudRepository userCrudRepository;

    public UserServiceImpl(
            UserCrudRepository userCrudRepository
    ) {
        this.userCrudRepository = userCrudRepository;
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
    public void changePassword(String token) {

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
