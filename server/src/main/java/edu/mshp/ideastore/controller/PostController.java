package edu.mshp.ideastore.controller;

import edu.mshp.ideastore.service.PostService;
import edu.mshp.ideastore.service.impl.PostServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(
            PostServiceImpl postService
    ) {
        this.postService = postService;
    }

    @GetMapping("/get")
    public ResponseEntity<?> get() {
        return ResponseEntity.ok(postService.get());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> get(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(postService.get(id));
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(
            @RequestHeader("Authorization") String token,
            @RequestParam String title,
            @RequestParam String body,
            @RequestParam String sub
    ) {
        Map<String, Object> result = new HashMap<>();
        Long id = postService.create(token, title, body, sub);
        result.put("post_id", id);

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id
    ) {
        postService.delete(token, id);
        return ResponseEntity.ok("");
    }
}
