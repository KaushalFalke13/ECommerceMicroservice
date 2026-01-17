package com.EComMicroService.OrdersServices.Services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.EComMicroService.OrdersServices.Entity.Address;

@Service
public interface AddressService {

    Address addAddress();
    
    List<Address> getAddressesByUserId(String userId);

    Address updateAddress(Address updatedAddress , String userId); 

}
