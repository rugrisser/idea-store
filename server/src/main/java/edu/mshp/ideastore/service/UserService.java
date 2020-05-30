package edu.mshp.ideastore.service;

import edu.mshp.ideastore.model.User;

public interface UserService {
    public String create(String login, String email, String password);
    public String authorize(String login, String password);
    public void changePassword(String token, String oldPassword, String newPassword);
    public Boolean validate(String token);
    public String removeTokenPrefix(String token);
    public User get(Long id);
    public User get(String token);
}
