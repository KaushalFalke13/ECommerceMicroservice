package com.EComMicroService.OrdersServices.Repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.EComMicroService.OrdersServices.Entity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long>{

}
