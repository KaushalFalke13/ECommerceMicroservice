package com.EComMicroService.UserServices.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.EComMicroService.UserServices.Entity.UsersDetails;

public interface UserRepository extends JpaRepository<UsersDetails, String> {

    @SuppressWarnings({ "unchecked" })
    UsersDetails save(UsersDetails users);

    UsersDetails findUsersById(String id);

}
