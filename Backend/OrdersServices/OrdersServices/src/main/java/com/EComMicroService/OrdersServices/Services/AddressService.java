package com.EComMicroService.OrdersServices.Services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.EComMicroService.OrdersServices.DTO.AddressDTO;
import com.EComMicroService.OrdersServices.Entity.Address;

@Service
public interface AddressService {

    Address addAddress(AddressDTO addressDTO,String authHeader);
    
    List<AddressDTO> getAddressesByUserId(String authHeader);

    AddressDTO updateAddress(AddressDTO updatedAddress , String authHeader); 

}
