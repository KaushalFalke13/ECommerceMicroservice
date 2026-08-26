package com.ecommicroservice.authservices.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.ecommicroservice.authservices.entity.Users;

@Service
public interface UsersService {

        public String registerUser(String email, String password);

        public boolean loginUser(String email, String password);

        public UserDetails loadUserByUsername(String username);

        public boolean updatePassword(String email, String newPassword);

        public boolean addNewRoles(String userId, String newRoles);

        public boolean removeRoles(String userId, String newRoles);

        public Users getUserById(String userId);
}
