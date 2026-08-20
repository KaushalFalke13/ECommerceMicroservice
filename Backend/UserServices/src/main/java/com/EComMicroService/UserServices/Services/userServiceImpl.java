package com.EComMicroService.UserServices.Services;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.EComMicroService.UserServices.DTO.UsersDTO;
import com.EComMicroService.UserServices.Entity.UsersDetails;
import com.EComMicroService.UserServices.Repository.UserRepository;

@Service
public class userServiceImpl implements userService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UsersDetails saveUser(UsersDTO usersDTO) {
        UsersDetails users = UsersDetails.builder()
                .id(UUID.randomUUID().toString())
                .name(usersDTO.getName())
                .email(usersDTO.getEmail())
                .password(usersDTO.getPassword())
                .build();
        return userRepository.save(users);
    }

    @Override
    public UsersDTO getUserById(String id) {
        userRepository.findById(id);
        return new UsersDTO();
    }

}
