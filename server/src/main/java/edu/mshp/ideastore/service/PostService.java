package edu.mshp.ideastore.service;

public interface PostService {
    public Long create(String token, String title, String body, String sub);
    public void delete(String token, Long id);
}
