package com.ecommicroservice.authservices.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommicroservice.authservices.entity.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, String> {

    Users findByEmail(String email);

}
