package com.ehub.controller;

import com.ehub.common.Gender;
import com.ehub.dto.request.UserCreationRequest;
import com.ehub.dto.request.UserPasswordRequest;
import com.ehub.dto.request.UserUpdateRequest;
import com.ehub.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/mockup/users")
@Tag(name = "Mockup User Controller")
public class MockupUserController {
    @Operation(summary = "Get User List", description = "API retrieve user from database")
    @GetMapping("/list")
    public Map<String, Object> getList(@RequestParam(required = false) String keyword,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "20") int size) {
        UserResponse userResponse1 = UserResponse.builder()
                .id(1L)
                .firstName("Cristiano")
                .lastName("Ronaldo")
                .gender(Gender.MALE)
                .birthday(new Date())
                .username("admin")
                .email("admin@gmail.com")
                .phone("0123456789")
                .build();

        UserResponse userResponse2 = new UserResponse();
        userResponse2.setId(2L);
        userResponse2.setFirstName("Leo");
        userResponse2.setLastName("Messi");
        userResponse2.setGender(Gender.MALE);
        userResponse2.setBirthday(new Date());
        userResponse2.setUsername("user");
        userResponse2.setEmail("user@gmail.com");
        userResponse2.setPhone("0987654321");

        List<UserResponse> userList = List.of(userResponse1, userResponse2);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.OK.value());
        result.put("message", "User list");
        result.put("data", userList);

        return result;
    }

    @Operation(summary = "Get User Detail", description = "API retrieve user detail by ID from database")
    @GetMapping("/{userId}")
    public Map<String, Object> getUserDetail(@PathVariable Long userId) {
        UserResponse userDetail = UserResponse.builder()
                .id(1L)
                .firstName("Cristiano")
                .lastName("Ronaldo")
                .gender(Gender.MALE)
                .birthday(new Date())
                .username("admin")
                .email("admin@gmail.com")
                .phone("0123456789")
                .build();

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.OK.value());
        result.put("message", "User detail");
        result.put("data", userDetail);

        return result;
    }

    @Operation(summary = "Create User", description = "API add new user to database")
    @PostMapping("/add")
    public Map<String, Object> createUser(UserCreationRequest request) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.CREATED.value());
        result.put("message", "User created successfully");
        result.put("data", 3);

        return result;
    }

    @Operation(summary = "Update User", description = "API update user to database")
    @PutMapping("/upd")
    public Map<String, Object> updateUser(UserUpdateRequest request) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.ACCEPTED.value());
        result.put("message", "User updated successfully");
        result.put("data", "");

        return result;
    }

    @Operation(summary = "Change Password", description = "API change password for user to database")
    @PatchMapping("/change-pwd")
    public Map<String, Object> changePassword(UserPasswordRequest request) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.NO_CONTENT.value());
        result.put("message", "Password updated successfully");
        result.put("data", "");

        return result;
    }

    @Operation(summary = "Delete User", description = "API activate user from database")
    @DeleteMapping("/del/{userId}")
    public Map<String, Object> deleteUser(@PathVariable Long userId) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.RESET_CONTENT.value());
        result.put("message", "User deleted successfully");
        result.put("data", "");

        return result;
    }
}
