package com.ehub.service;

import com.ehub.common.Gender;
import com.ehub.common.UserStatus;
import com.ehub.common.UserType;
import com.ehub.dto.request.AddressRequest;
import com.ehub.dto.request.UserCreationRequest;
import com.ehub.dto.request.UserPasswordRequest;
import com.ehub.dto.request.UserUpdateRequest;
import com.ehub.dto.response.UserPageResponse;
import com.ehub.dto.response.UserResponse;
import com.ehub.exception.ResourceNotFoundException;
import com.ehub.model.UserEntity;
import com.ehub.repository.AddressRepository;
import com.ehub.repository.UserRepository;
import com.ehub.service.impl.UserServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private UserService userService;

    private @Mock UserRepository userRepository;
    private @Mock AddressRepository addressRepository;
    private @Mock PasswordEncoder passwordEncoder;
    private @Mock EmailService emailService;

    private static UserEntity newbie;
    private static UserEntity learnJava;

    @BeforeAll
    static void beforeAll() {
        //Chuan bi du lieu
        newbie = new UserEntity();
        newbie.setId(1L);
        newbie.setFirstName("Newbie");
        newbie.setLastName("");
        newbie.setGender(Gender.MALE);
        newbie.setBirthday(new Date());
        newbie.setEmail("newbie@gmail.com");
        newbie.setPhone("0987654321");
        newbie.setUsername("newbie");
        newbie.setPassword("123456");
        newbie.setType(UserType.USER);
        newbie.setStatus(UserStatus.ACTIVE);

        learnJava = new UserEntity();
        learnJava.setId(2L);
        learnJava.setFirstName("Learn");
        learnJava.setLastName("Java");
        learnJava.setGender(Gender.MALE);
        learnJava.setBirthday(new Date());
        learnJava.setEmail("learnjava@gmail.com");
        learnJava.setPhone("0123456789");
        learnJava.setUsername("learnjava");
        learnJava.setPassword("123456");
        learnJava.setType(UserType.USER);
        learnJava.setStatus(UserStatus.ACTIVE);
    }

    @BeforeEach
    void setUp() {
        // khoi tao buoc trien khai la UserService
        userService = new UserServiceImpl(userRepository, addressRepository, passwordEncoder, emailService);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getListUsers_Success() {
        // Gia lap phuong thuc
        Page<UserEntity> userEntityPage = new PageImpl<>(Arrays.asList(newbie, learnJava));
        Mockito.when(userRepository.findAll(any(Pageable.class))).thenReturn(userEntityPage);

        // Goi phuong thuc can test
        UserPageResponse result = userService.findAll(null, null, 0, 20);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.getTotalElements());
    }

    @Test
    void searchUsers_Success() {
        // Gia lap phuong thuc
        Page<UserEntity> userEntityPage = new PageImpl<>(Arrays.asList(newbie, learnJava));
        Mockito.when(userRepository.searchByKeyword(any(), any(Pageable.class))).thenReturn(userEntityPage);

        // Goi phuong thuc can test
        UserPageResponse result = userService.findAll("java", null, 0, 20);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.getTotalElements());
    }

    @Test
    void getListUsers_Empty() {
        // Gia lap phuong thuc
        Page<UserEntity> userEntityPage = new PageImpl<>(List.of());
        Mockito.when(userRepository.findAll(any(Pageable.class))).thenReturn(userEntityPage);

        // Goi phuong thuc can test
        UserPageResponse result = userService.findAll(null, null, 0, 20);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(0, result.getTotalElements());
    }

    @Test
    void getUserById_Success() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(learnJava));

        UserResponse result = userService.findById(1L);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
    }

    @Test
    void getUserById_Failure() {
        ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class, () -> userService.findById(1L));
        Assertions.assertEquals("User not found", exception.getMessage());
    }

    @Test
    void getByUsername_Success() {
        Mockito.when(userRepository.findByUsername("newbie")).thenReturn(newbie);

        UserResponse result = userService.findByUsername("newbie");
        Assertions.assertNotNull(result);
        Assertions.assertEquals("newbie", result.getUsername());
    }

    @Test
    void getByUsername_Failure() {
        ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class, () -> userService.findByUsername("newbie"));
        Assertions.assertEquals("User not found", exception.getMessage());
    }

    @Test
    void getByEmail_Success() {
        Mockito.when(userRepository.findByEmail("newbie@gmail.com")).thenReturn(newbie);

        UserResponse result = userService.findByEmail("newbie@gmail.com");
        Assertions.assertNotNull(result);
        Assertions.assertEquals("newbie@gmail.com", result.getEmail());
    }

    @Test
    void getByEmail_Failure() {
        ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class, () -> userService.findByEmail("newbie@gmail.com"));
        Assertions.assertEquals("User not found", exception.getMessage());
    }

    @Test
    void saveUser_Success() {
        Mockito.when(userRepository.save(any(UserEntity.class))).thenReturn(newbie);

        UserCreationRequest userCreationRequest = new UserCreationRequest();
        userCreationRequest.setFirstName("Newbie");
        userCreationRequest.setLastName("LearnJava");
        userCreationRequest.setGender(Gender.MALE);
        userCreationRequest.setBirthday(new Date());
        userCreationRequest.setEmail("newbielearnjava@gmail.com");
        userCreationRequest.setPhone("0987654321");
        userCreationRequest.setUsername("newbielearnjava");

        AddressRequest addressRequest = new AddressRequest();
        addressRequest.setApartmentNumber("ApartmentNumber");
        addressRequest.setFloor("Floor");
        addressRequest.setBuilding("Building");
        addressRequest.setStreetNumber("StreetNumber");
        addressRequest.setStreet("Street");
        addressRequest.setCity("City");
        addressRequest.setCountry("Country");
        addressRequest.setAddressType(1);

        userCreationRequest.setAddresses(List.of(addressRequest));

        long userId = userService.save(userCreationRequest);

        Assertions.assertEquals(1L, userId);

    }

    @Test
    void updateUser_Success() {
        Long userId = 2L;

        UserEntity updatedUser = new UserEntity();
        updatedUser.setId(userId);
        updatedUser.setFirstName("Java");
        updatedUser.setLastName("learn");
        updatedUser.setGender(Gender.MALE);
        updatedUser.setBirthday(new Date());
        updatedUser.setEmail("learnjava@gmail.com");
        updatedUser.setPhone("0123456789");
        updatedUser.setUsername("learnjava");
        updatedUser.setType(UserType.USER);
        updatedUser.setStatus(UserStatus.ACTIVE);

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(learnJava));
        Mockito.when(userRepository.save(any(UserEntity.class))).thenReturn(updatedUser);

        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setId(userId);
        userUpdateRequest.setFirstName("JAVA");
        userUpdateRequest.setLastName("LEARN");
        userUpdateRequest.setGender(Gender.MALE);
        userUpdateRequest.setBirthday(new Date());
        userUpdateRequest.setEmail("LEARNJAVA@gmail.com");
        userUpdateRequest.setPhone("0123456789");
        userUpdateRequest.setUsername("LEARNJAVA");

        AddressRequest addressRequest = new AddressRequest();
        addressRequest.setApartmentNumber("ApartmentNumber");
        addressRequest.setFloor("Floor");
        addressRequest.setBuilding("Building");
        addressRequest.setStreetNumber("StreetNumber");
        addressRequest.setStreet("Street");
        addressRequest.setCity("City");
        addressRequest.setCountry("Country");
        addressRequest.setAddressType(1);

        userUpdateRequest.setAddresses(List.of(addressRequest));

        userService.update(userUpdateRequest);
        UserResponse result = userService.findById(userId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("JAVA", result.getFirstName());
        Assertions.assertEquals("LEARN", result.getLastName());
    }

    @Test
    void changePassword_Success() {
        UserPasswordRequest request = new UserPasswordRequest();
        request.setId(1L);
        request.setPassword("newPassword");
        request.setConfirmPassword("newPassword");

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(newbie));
        Mockito.when(passwordEncoder.encode("newPassword")).thenReturn("newPasswordHash");
        userService.changePassword(request);

        Assertions.assertEquals("newPasswordHash", newbie.getPassword());
        // Kiểm tra phương thức save đã được gọi đúng 1 lần
        Mockito.verify(userRepository, Mockito.times(1)).save(newbie);
    }

    @Test
    void deleteUser_Success() {
        Long userId = 2L;

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(learnJava));
        userService.delete(userId);

        Assertions.assertEquals(UserStatus.INACTIVE, learnJava.getStatus());
        Mockito.verify(userRepository, Mockito.times(1)).save(learnJava);
    }
}