package com.ecommicroservice.orderservices.repositorys;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ecommicroservice.orderservices.entity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {

    List<Address> findAllByUserId(String userId);

}
