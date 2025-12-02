package com.EComMicroService.AuthServices.Service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public interface UsersService {

        public String registerUser(String email, String password);
        public boolean loginUser(String email, String password);
        public UserDetails loadUserByUsername(String username);
        public boolean updatePassword(String email, String newPassword);

}
