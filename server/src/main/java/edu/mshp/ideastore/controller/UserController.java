package edu.mshp.ideastore.controller;

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

    @PostMapping("/create")
    public ResponseEntity create(
            @RequestParam String login,
            @RequestParam String email,
            @RequestParam String password
    ) {
        Map<String, Object> result = new HashMap<>();
        String token = userService.create(login, email, password);
        result.put("token", token);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/login")
    public ResponseEntity login(
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
            @RequestParam(name = "old") String oldPassword,
            @RequestParam(name = "new") String newPassword
    ) {
        userService.changePassword(token, oldPassword, newPassword);
        return ResponseEntity.ok(ReturnStructures.returnStatusOk());
    }

}
