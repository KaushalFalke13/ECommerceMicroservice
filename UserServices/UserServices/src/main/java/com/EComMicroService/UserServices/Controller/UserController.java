package com.EComMicroService.UserServices.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import com.EComMicroService.UserServices.DTO.UsersDTO;
import com.EComMicroService.UserServices.Services.userService;

@Controller
public class UserController {

    private userService userService;

    public UserController(@Autowired userService userService) {
        this.userService = userService;
    }

    @PostMapping("/createNewUser")
    public ResponseEntity<?> createNewUser(@RequestBody UsersDTO usersDTO){
        userService.saveUser(usersDTO);
        return ResponseEntity.ok("User created");
    }

    @GetMapping("/getUser")
    public ResponseEntity<?> getUserDetails(@RequestParam String id){
        UsersDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/updateUser")
    public ResponseEntity<?> updateUser(){
        return ResponseEntity.ok(" ");
    }
}
