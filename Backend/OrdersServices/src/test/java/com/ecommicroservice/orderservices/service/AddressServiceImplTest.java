package com.ecommicroservice.orderservices.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.ecommicroservice.orderservices.config.jwtUtil;
import com.ecommicroservice.orderservices.dto.AddressDTO;
import com.ecommicroservice.orderservices.dto.ChangeDTOs;
import com.ecommicroservice.orderservices.entity.Address;
import com.ecommicroservice.orderservices.repositorys.AddressRepository;
import com.ecommicroservice.orderservices.services.AddressServiceImpl;

@ExtendWith(MockitoExtension.class)
class AddressServiceImplTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private ChangeDTOs changeDTOs;

    @Mock
    private jwtUtil jwtUtil;

    @InjectMocks
    private AddressServiceImpl addressService;

    private final String VALID_TOKEN = "Bearer valid.jwt.token";
    private final String USER_ID = "user123";
    private final String ADDRESS_ID = "1";

    private AddressDTO addressDTO;
    private Address address;

    @BeforeEach
    void setUp() {
        addressDTO = new AddressDTO();
        addressDTO.setStreet("123 Main St");
        addressDTO.setCity("Springfield");
        addressDTO.setState("IL");
        addressDTO.setPincode(62701L);
        addressDTO.setCity("USA");

        address = new Address();
        address.setId(ADDRESS_ID);
        address.setStreet("123 Main St");
        address.setCity("Springfield");
        address.setState("IL");
        address.setPincode(62701L);
        address.setCity("USA");
        address.setUserId(USER_ID);
    }

    @Test
    void addAddress_ShouldExtractUserIdAndSaveAddress() {
        // Arrange
        when(jwtUtil.getUserIdFromToken(anyString())).thenReturn(USER_ID);
        when(changeDTOs.changeDTOtoAddress(any(AddressDTO.class))).thenReturn(address);
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        // Act
        Address result = addressService.addAddress(addressDTO, VALID_TOKEN);

        // Assert
        assertNotNull(result);
        assertEquals(ADDRESS_ID, result.getId());
        assertEquals(USER_ID, addressDTO.getUserId());
        verify(jwtUtil).getUserIdFromToken("valid.jwt.token");
        verify(changeDTOs).changeDTOtoAddress(addressDTO);
        verify(addressRepository).save(address);
    }

    @Test
    void addAddress_ShouldThrowException_WhenTokenIsInvalid() {
        // Arrange
        String invalidHeader = "Invalid";
        when(jwtUtil.getUserIdFromToken(anyString()))
                .thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT missing or invalid"));

        // Act & Assert
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> addressService.addAddress(addressDTO, invalidHeader));
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("JWT missing or invalid", exception.getReason());
        verify(jwtUtil).getUserIdFromToken("Invalid");
    }

    @Test
    void addAddress_ShouldThrowNullPointerException_WhenAuthHeaderIsNull() {
        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> addressService.addAddress(addressDTO, null));
        verify(jwtUtil, never()).getUserIdFromToken(anyString());
    }

    @Test
    void addAddress_ShouldHandleTokenWithLowercaseBearer() {
        // Arrange
        String authHeaderWithLowercase = "bearer valid.jwt.token";
        when(jwtUtil.getUserIdFromToken("bearer valid.jwt.token")).thenReturn(USER_ID);
        when(changeDTOs.changeDTOtoAddress(any(AddressDTO.class))).thenReturn(address);
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        // Act
        Address result = addressService.addAddress(addressDTO, authHeaderWithLowercase);

        // Assert
        assertNotNull(result);
        assertEquals(USER_ID, addressDTO.getUserId());
        verify(jwtUtil).getUserIdFromToken("bearer valid.jwt.token");
        verify(changeDTOs).changeDTOtoAddress(addressDTO);
        verify(addressRepository).save(address);
    }

    @Test
    void addAddress_ShouldHandleTokenWithExtraSpaces() {
        // Arrange
        String authHeaderWithSpaces = "Bearer   valid.jwt.token   ";
        // The replace() removes exactly "Bearer " (with one space), leaving extra
        // spaces
        String expectedToken = "  valid.jwt.token   ";
        when(jwtUtil.getUserIdFromToken(expectedToken)).thenReturn(USER_ID);
        when(changeDTOs.changeDTOtoAddress(any(AddressDTO.class))).thenReturn(address);
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        // Act
        Address result = addressService.addAddress(addressDTO, authHeaderWithSpaces);

        // Assert
        assertNotNull(result);
        assertEquals(USER_ID, addressDTO.getUserId());
        verify(jwtUtil).getUserIdFromToken(expectedToken);
        verify(changeDTOs).changeDTOtoAddress(addressDTO);
        verify(addressRepository).save(address);
    }

    @Test
    void addAddress_ShouldHandleEmptyTokenString() {
        // Arrange
        String authHeaderEmpty = "Bearer ";
        when(jwtUtil.getUserIdFromToken("")).thenReturn(USER_ID);
        when(changeDTOs.changeDTOtoAddress(any(AddressDTO.class))).thenReturn(address);
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        // Act
        Address result = addressService.addAddress(addressDTO, authHeaderEmpty);

        // Assert
        assertNotNull(result);
        assertEquals(USER_ID, addressDTO.getUserId());
        verify(jwtUtil).getUserIdFromToken("");
        verify(changeDTOs).changeDTOtoAddress(addressDTO);
        verify(addressRepository).save(address);
    }

    @Test
    void addAddress_ShouldThrowException_WhenJwtUtilThrowsGenericException() {
        // Arrange
        String invalidHeader = "Bearer invalid.token";
        when(jwtUtil.getUserIdFromToken(anyString()))
                .thenThrow(new RuntimeException("JWT parsing error"));

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> addressService.addAddress(addressDTO, invalidHeader));
        assertEquals("JWT parsing error", exception.getMessage());
        verify(jwtUtil).getUserIdFromToken("invalid.token");
    }

    @Test
    void removeAddress_ShouldDeleteAddressById() {
        // Act
        addressService.removeAddress(ADDRESS_ID);

        // Assert
        verify(addressRepository).deleteById(ADDRESS_ID);
    }

    @Test
    void removeAddress_ShouldHandleNonExistentAddressGracefully() {
        // Arrange
        String nonExistentId = "999";
        doNothing().when(addressRepository).deleteById(nonExistentId);

        // Act
        addressService.removeAddress(nonExistentId);

        // Assert
        verify(addressRepository).deleteById(nonExistentId);
    }

    @Test
    void removeAddress_ShouldHandleNullIdGracefully() {
        // Arrange
        doThrow(new IllegalArgumentException("Id must not be null"))
                .when(addressRepository).deleteById(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> addressService.removeAddress(null));
    }

    @Test
    void getAddressesByUserId_ShouldReturnListOfAddressDTOs() {
        // Arrange
        List<Address> addressList = Arrays.asList(address);
        AddressDTO expectedDTO = new AddressDTO();
        expectedDTO.setStreet("123 Main St");
        expectedDTO.setCity("Springfield");
        expectedDTO.setState("IL");
        expectedDTO.setPincode(62701L);
        expectedDTO.setCity("USA");

        when(jwtUtil.getUserIdFromToken(anyString())).thenReturn(USER_ID);
        when(addressRepository.findAllByUserId(USER_ID)).thenReturn(addressList);
        when(changeDTOs.changAddressToDTO(address)).thenReturn(expectedDTO);

        // Act
        List<AddressDTO> result = addressService.getAddressesByUserId(VALID_TOKEN);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expectedDTO.getStreet(), result.get(0).getStreet());
        verify(jwtUtil).getUserIdFromToken("valid.jwt.token");
        verify(addressRepository).findAllByUserId(USER_ID);
        verify(changeDTOs).changAddressToDTO(address);
    }

    @Test
    void getAddressesByUserId_ShouldReturnEmptyList_WhenNoAddressesFound() {
        // Arrange
        when(jwtUtil.getUserIdFromToken(anyString())).thenReturn(USER_ID);
        when(addressRepository.findAllByUserId(USER_ID)).thenReturn(Arrays.asList());

        // Act
        List<AddressDTO> result = addressService.getAddressesByUserId(VALID_TOKEN);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(addressRepository).findAllByUserId(USER_ID);
        verify(changeDTOs, never()).changAddressToDTO(any(Address.class));
    }

    @Test
    void getAddressesByUserId_ShouldThrowException_WhenTokenIsInvalid() {
        // Arrange
        String invalidHeader = "Invalid";
        when(jwtUtil.getUserIdFromToken(anyString()))
                .thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT missing or invalid"));

        // Act & Assert
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> addressService.getAddressesByUserId(invalidHeader));
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("JWT missing or invalid", exception.getReason());
        verify(jwtUtil).getUserIdFromToken("Invalid");
    }

    @Test
    void getAddressesByUserId_ShouldThrowNullPointerException_WhenAuthHeaderIsNull() {
        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> addressService.getAddressesByUserId(null));
        verify(jwtUtil, never()).getUserIdFromToken(anyString());
    }

    @Test
    void getAddressesByUserId_ShouldReturnMultipleAddresses() {
        // Arrange
        Address address2 = new Address();
        address2.setId("2");
        address2.setStreet("456 Oak Ave");
        address2.setCity("Chicago");
        address2.setState("IL");
        address2.setPincode(60601L);
        address2.setCity("USA");
        address2.setUserId(USER_ID);

        List<Address> addressList = Arrays.asList(address, address2);

        AddressDTO expectedDTO1 = new AddressDTO();
        expectedDTO1.setStreet("123 Main St");
        expectedDTO1.setCity("Springfield");
        expectedDTO1.setState("IL");
        expectedDTO1.setPincode(62701L);
        expectedDTO1.setCity("USA");

        AddressDTO expectedDTO2 = new AddressDTO();
        expectedDTO2.setStreet("456 Oak Ave");
        expectedDTO2.setCity("Chicago");
        expectedDTO2.setState("IL");
        expectedDTO2.setPincode(60601L);
        expectedDTO2.setCity("USA");

        when(jwtUtil.getUserIdFromToken(anyString())).thenReturn(USER_ID);
        when(addressRepository.findAllByUserId(USER_ID)).thenReturn(addressList);
        when(changeDTOs.changAddressToDTO(address)).thenReturn(expectedDTO1);
        when(changeDTOs.changAddressToDTO(address2)).thenReturn(expectedDTO2);

        // Act
        List<AddressDTO> result = addressService.getAddressesByUserId(VALID_TOKEN);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("123 Main St", result.get(0).getStreet());
        assertEquals("456 Oak Ave", result.get(1).getStreet());
        verify(jwtUtil).getUserIdFromToken("valid.jwt.token");
        verify(addressRepository).findAllByUserId(USER_ID);
        verify(changeDTOs, times(2)).changAddressToDTO(any(Address.class));
    }

    @Test
    void getAddressesByUserId_ShouldHandleAddressWithNullFields() {
        // Arrange
        Address addressWithNulls = new Address();
        addressWithNulls.setId("3");
        addressWithNulls.setStreet(null);
        addressWithNulls.setCity(null);
        addressWithNulls.setState(null);
        addressWithNulls.setPincode(null);
        addressWithNulls.setCity(null);
        addressWithNulls.setUserId(USER_ID);

        List<Address> addressList = Arrays.asList(addressWithNulls);

        AddressDTO expectedDTO = new AddressDTO();
        expectedDTO.setStreet(null);
        expectedDTO.setCity(null);
        expectedDTO.setState(null);
        expectedDTO.setPincode(null);
        expectedDTO.setCity(null);

        when(jwtUtil.getUserIdFromToken(anyString())).thenReturn(USER_ID);
        when(addressRepository.findAllByUserId(USER_ID)).thenReturn(addressList);
        when(changeDTOs.changAddressToDTO(addressWithNulls)).thenReturn(expectedDTO);

        // Act
        List<AddressDTO> result = addressService.getAddressesByUserId(VALID_TOKEN);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertNull(result.get(0).getStreet());
        assertNull(result.get(0).getCity());
        assertNull(result.get(0).getState());
        assertNull(result.get(0).getPincode());
        assertNull(result.get(0).getCity());
        verify(jwtUtil).getUserIdFromToken("valid.jwt.token");
        verify(addressRepository).findAllByUserId(USER_ID);
        verify(changeDTOs).changAddressToDTO(addressWithNulls);
    }

    @Test
    void getAddressesByUserId_ShouldHandleTokenWithLowercaseBearer() {
        // Arrange
        String authHeaderWithLowercase = "bearer valid.jwt.token";
        when(jwtUtil.getUserIdFromToken("bearer valid.jwt.token")).thenReturn(USER_ID);
        when(addressRepository.findAllByUserId(USER_ID)).thenReturn(Arrays.asList());

        // Act
        List<AddressDTO> result = addressService.getAddressesByUserId(authHeaderWithLowercase);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(jwtUtil).getUserIdFromToken("bearer valid.jwt.token");
        verify(addressRepository).findAllByUserId(USER_ID);
    }

    @Test
    void getAddressesByUserId_ShouldThrowException_WhenJwtUtilThrowsGenericException() {
        // Arrange
        String invalidHeader = "Bearer invalid.token";
        when(jwtUtil.getUserIdFromToken(anyString()))
                .thenThrow(new RuntimeException("JWT parsing error"));

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> addressService.getAddressesByUserId(invalidHeader));
        assertEquals("JWT parsing error", exception.getMessage());
        verify(jwtUtil).getUserIdFromToken("invalid.token");
        verify(addressRepository, never()).findAllByUserId(anyString());
    }

    @Test
    void updateAddress_ShouldThrowUnsupportedOperationException() {
        // Arrange
        AddressDTO updatedAddress = new AddressDTO();
        String authHeader = "Bearer token";

        // Act & Assert
        UnsupportedOperationException exception = assertThrows(
                UnsupportedOperationException.class,
                () -> addressService.updateAddress(updatedAddress, authHeader));
        assertEquals("Unimplemented method 'updateAddress'", exception.getMessage());
    }
}
