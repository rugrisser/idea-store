package edu.mshp.ideastore.service;

public interface UserService {
    public String create(String login, String email, String password);
    public String authorize(String login, String password);
    public void changePassword(String token);
}
