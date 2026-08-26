package com.ecommicroservice.orderservices.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecommicroservice.orderservices.dto.AddressDTO;
import com.ecommicroservice.orderservices.entity.Address;

@Service
public interface AddressService {

    Address addAddress(AddressDTO addressDTO, String authHeader);

    List<AddressDTO> getAddressesByUserId(String authHeader);

    AddressDTO updateAddress(AddressDTO updatedAddress, String authHeader);

    void removeAddress(String addressId);

}
