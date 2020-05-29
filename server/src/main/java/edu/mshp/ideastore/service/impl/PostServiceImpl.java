package edu.mshp.ideastore.service.impl;

import edu.mshp.ideastore.exception.ForbiddenException;
import edu.mshp.ideastore.exception.NotFoundException;
import edu.mshp.ideastore.model.Post;
import edu.mshp.ideastore.model.User;
import edu.mshp.ideastore.module.jwt.JWTToken;
import edu.mshp.ideastore.respository.PostCrudRepository;
import edu.mshp.ideastore.respository.UserCrudRepository;
import edu.mshp.ideastore.service.PostService;
import edu.mshp.ideastore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.security.InvalidKeyException;
import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    private final UserService userService;
    private final UserCrudRepository userCrudRepository;
    private final PostCrudRepository postCrudRepository;

    @Autowired
    public PostServiceImpl(
            UserServiceImpl userService,
            UserCrudRepository userCrudRepository,
            PostCrudRepository postCrudRepository
    ) {
        this.userService = userService;
        this.userCrudRepository = userCrudRepository;
        this.postCrudRepository = postCrudRepository;
    }

    @Override
    public Post get(Long id) {
        Optional<Post> postOptional = postCrudRepository.findById(id);
        if (postOptional.isEmpty()) {
            throw new NotFoundException("Post not found");
        }
        return postOptional.get();
    }

    @Override
    public List<Post> get() {
        return postCrudRepository.findAll();
    }

    @Override
    public Long create(String token, String title, String body, String sub) {
        if (userService.validate(token)) {
            token = userService.removeTokenPrefix(token);
            JWTToken jwtToken = null;
            try {
                jwtToken = new JWTToken(token);
            } catch (BadPaddingException | InvalidKeyException | IllegalBlockSizeException e) {
                throw new ForbiddenException("Token is invalid");
            }
            Optional<User> optionalUser = userCrudRepository.findById(jwtToken.getId());
            if (optionalUser.isEmpty()) {
                throw new ForbiddenException("User not found");
            }

            User user = optionalUser.get();
            Post post = new Post();
            post.setTitle(title);
            post.setBody(body);
            post.setSub(sub);
            post.setUser(user);

            postCrudRepository.save(post);
            return post.getId();
        } else {
            throw new ForbiddenException("Token is invalid");
        }
    }

    @Override
    public void delete(String token, Long id) {
        if (userService.validate(token)) {
            token = userService.removeTokenPrefix(token);
            JWTToken jwtToken = null;
            try {
                jwtToken = new JWTToken(token);
            } catch (BadPaddingException | InvalidKeyException | IllegalBlockSizeException e) {
                throw new ForbiddenException("Token is invalid");
            }

            Optional<Post> postOptional = postCrudRepository.findById(id);
            if (postOptional.isEmpty()) {
                throw new NotFoundException("Post with id " + id + " not found");
            }
            Post post = postOptional.get();

            if (!post.getUser().getId().equals(jwtToken.getId())) {
                throw new ForbiddenException("You cannot delete this post");
            }
            postCrudRepository.delete(post);
        } else {
            throw new ForbiddenException("Token is invalid");
        }
    }
}
