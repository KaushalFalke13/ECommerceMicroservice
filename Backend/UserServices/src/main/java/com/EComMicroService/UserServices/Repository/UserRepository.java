package com.ecommicroservice.userservices.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ecommicroservice.userservices.entity.UsersDetails;

public interface UserRepository extends JpaRepository<UsersDetails, String> {

    @SuppressWarnings("unchecked")
    UsersDetails save(UsersDetails users);

    UsersDetails findUsersById(String id);

    UsersDetails findByEmail(String email);

    boolean existsByEmail(String email);

}
