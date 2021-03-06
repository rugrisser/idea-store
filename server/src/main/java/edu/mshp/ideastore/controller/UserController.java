package edu.mshp.ideastore.controller;

import edu.mshp.ideastore.exception.BadRequestException;
import edu.mshp.ideastore.module.ReturnStructures;
import edu.mshp.ideastore.service.UserService;
import edu.mshp.ideastore.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(
            UserServiceImpl userService
    ) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(userService.get(id));
    }

    @GetMapping
    public ResponseEntity<?> get(
            @RequestHeader("Authorization") String token
    ) {
        return ResponseEntity.ok(userService.get(token));
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(
            @RequestParam String login,
            @RequestParam String email,
            @RequestParam String password
    ) {
        Map<String, Object> result = new HashMap<>();
        String token = "";
        try {
            token = userService.create(login, email, password);
        } catch (BadRequestException badRequestException) {
            result.put("error", badRequestException.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
        result.put("token", token);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/login")
    public ResponseEntity<?> login(
            @RequestParam String login,
            @RequestParam String password
    ) {
        Map<String, Object> result = new HashMap<>();
        String token = userService.authorize(login, password);
        result.put("token", token);

        return ResponseEntity.ok(result);
    }

    @PutMapping("/changePassword")
    public ResponseEntity changePassword(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "old") String oldPassword,
            @RequestParam(value = "new") String newPassword
    ) {
        userService.changePassword(token, oldPassword, newPassword);
        return ResponseEntity.ok(ReturnStructures.returnStatusOk());
    }

}
