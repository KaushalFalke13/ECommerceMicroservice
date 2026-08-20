package com.EComMicroService.AuthServices.Service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.EComMicroService.AuthServices.Entity.Users;
import com.EComMicroService.AuthServices.ExceptionHandler.EmailAlreadyExistsException;
import com.EComMicroService.AuthServices.Repository.UsersRepository;

@Service
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsersServiceImpl(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String registerUser(String email, String password) {
         if (usersRepository.findByEmail(email) != null) {
            throw new EmailAlreadyExistsException("Email already registered");
        }   

        List<String> role = List.of("USER");
        Users users = Users.builder()
                .userId(UUID.randomUUID().toString())
                .email(email)
                .role(role)
                .password(passwordEncoder.encode(password))
                .build();
        usersRepository.save(users);
        return "User registered successfully";
    }

    @Override
    public boolean loginUser(String email, String password) {
        Users user = usersRepository.findByEmail(email);
        if (user != null && user.getEmail().equals(email) && passwordEncoder.matches(password, user.getPassword())) {
            return true;
        }
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserDetails user = usersRepository.findByEmail(username);
        if (user != null) {
            return (UserDetails) user;
        }
        throw new RuntimeException("User not found");
    }

    @Override
    public boolean updatePassword(String email, String newPassword) {
        Users users = usersRepository.findByEmail(email);
        if (users != null) {
            users.setPassword(passwordEncoder.encode(newPassword));
            usersRepository.save(users);
            return true;
        }
        return false;
    }

    @Override
    public Users getUserById(String userId) {
        return usersRepository.findById(userId).orElse(null);
    }

    @Override
    public boolean addNewRoles(String userId, String newRoles) {
        Users users = usersRepository.findById(userId).orElse(null);
        if (users != null) {
            List<String> roles = users.getRole();
            if (!roles.contains(newRoles)) {
                roles.add(newRoles);
            }
            users.setRole(roles);
            usersRepository.save(users);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeRoles(String userId, String newRoles) {
        Users users = usersRepository.findById(userId).orElse(null);
        if (users != null) {
            List<String> roles = users.getRole();
            if (roles.contains(newRoles)) {
                roles.remove(newRoles);
            }
            users.setRole(roles);
            usersRepository.save(users);
            return true;
        }
        return false;
    }

}
