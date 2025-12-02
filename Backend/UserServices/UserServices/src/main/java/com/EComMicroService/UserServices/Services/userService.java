package com.EComMicroService.UserServices.Services;

import com.EComMicroService.UserServices.DTO.UsersDTO;
import com.EComMicroService.UserServices.Entity.UsersDetails;


public interface userService {

    UsersDetails saveUser(UsersDTO usersDTO);  
    UsersDTO getUserById(String id);  

}
