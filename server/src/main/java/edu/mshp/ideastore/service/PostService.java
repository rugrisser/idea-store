package edu.mshp.ideastore.service;

import edu.mshp.ideastore.model.Post;

import java.util.List;

public interface PostService {
    public Post get(Long id);
    public List<Post> get();
    public Long create(String token, String title, String body, String sub);
    public void delete(String token, Long id);
}
