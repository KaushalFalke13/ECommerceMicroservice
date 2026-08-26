package com.ecommicroservice.userservices.services;

import com.ecommicroservice.userservices.dto.UsersDTO;
import com.ecommicroservice.userservices.entity.UsersDetails;

public interface userService {

    UsersDetails saveUser(UsersDTO usersDTO);

    UsersDTO getUserById(String id);

}
