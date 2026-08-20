package com.EComMicroService.OrdersServices.Services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.EComMicroService.OrdersServices.Config.jwtUtil;
import com.EComMicroService.OrdersServices.DTO.AddressDTO;
import com.EComMicroService.OrdersServices.DTO.ChangeDTOs;
import com.EComMicroService.OrdersServices.Entity.Address;
import com.EComMicroService.OrdersServices.Repositorys.AddressRepository;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final ChangeDTOs changeDTOs;
    private final jwtUtil jwtUtil;

    public AddressServiceImpl(AddressRepository addressRepository,ChangeDTOs changeDTOs,jwtUtil jwtUtil){
        this.addressRepository = addressRepository;
        this.changeDTOs = changeDTOs;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Address addAddress(AddressDTO addressDTO,String authHeader) {
        String userId = getUserIdFromAuthHeader(authHeader);
        addressDTO.setUserId(userId);
        Address address = changeDTOs.changeDTOtoAddress(addressDTO);
        return addressRepository.save(address);    
    }

   @Override
    public void removeAddress(Long addressId) {
        addressRepository.deleteById(addressId);
    }

    @Override
    public List<AddressDTO> getAddressesByUserId(String authHeader) {
        String userId = getUserIdFromAuthHeader(authHeader);
        List<Address> address =addressRepository.findAllByUserId(userId);
        return address.stream().map(item -> changeDTOs.changAddressToDTO(item)).toList();
    }

    @Override
    public AddressDTO updateAddress(AddressDTO updatedAddress, String authHeader) {
        // String userId = getUserIdFromAuthHeader(authHeader);

        throw new UnsupportedOperationException("Unimplemented method 'updateAddress'");
    }


    private String getUserIdFromAuthHeader(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        if (token == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT missing or invalid");
        }
        return jwtUtil.getUserIdFromToken(token);
    }

 
}
