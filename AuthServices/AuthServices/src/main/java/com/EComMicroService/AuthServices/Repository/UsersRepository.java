package com.EComMicroService.AuthServices.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.EComMicroService.AuthServices.Entity.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, String> {

    Users findByEmail(String email);

}
