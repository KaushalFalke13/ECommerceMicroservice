package com.EComMicroService.UserServices.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.EComMicroService.UserServices.DTO.UsersDTO;
import com.EComMicroService.UserServices.Services.userService;

@RestController
@RequestMapping("/users")
public class UserController {

    private userService userService;

    public UserController(@Autowired userService userService) {
        this.userService = userService;
    }

    @GetMapping("/home")
    public ResponseEntity<?> home() {
        System.out.println("User Service is up and running");
        return ResponseEntity.ok("User Service is up and running");
    }

    @PostMapping("/createNewUser")
    public ResponseEntity<?> createNewUser(@RequestBody UsersDTO usersDTO) {
        userService.saveUser(usersDTO);
        return ResponseEntity.ok("User created");
    }

    @GetMapping("/getUser")
    public ResponseEntity<?> getUserDetails(@RequestParam String id) {
        UsersDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/updateUser")
    public ResponseEntity<?> updateUser() {
        return ResponseEntity.ok("User updated");
    }
}
