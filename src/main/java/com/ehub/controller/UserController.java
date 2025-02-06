package com.ehub.controller;

import com.ehub.dto.request.UserCreationRequest;
import com.ehub.dto.request.UserPasswordRequest;
import com.ehub.dto.request.UserUpdateRequest;
import com.ehub.dto.response.UserResponse;
import com.ehub.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Tag(name = "User Controller")
@Slf4j(topic = "USER-CONTROLLER")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;

    @Operation(summary = "Get User List", description = "API retrieve user from database")
    @GetMapping("/list")
    public Map<String, Object> getList(@RequestParam(required = false) String keyword,
                                       @RequestParam(required = false) String sort,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "20") int size) {
        log.info("Get user list");

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.OK.value());
        result.put("message", "User list");
        result.put("data", userService.findAll(keyword, sort, page, size));

        return result;
    }

    @Operation(summary = "Get User Detail", description = "API retrieve user detail by ID from database")
    @GetMapping("/{userId}")
    public Map<String, Object> getUserDetail(@PathVariable @Min(value = 1, message = "ID must be equals or greater than {value}") Long userId) {
        log.info("Get user detail by ID: {}", userId);

        UserResponse userDetail = userService.findById(userId);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.OK.value());
        result.put("message", "user");
        result.put("data", userDetail);

        return result;
    }

    @Operation(summary = "Create User", description = "API add new user to database")
    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Valid UserCreationRequest request) {
        log.info("Create User: {}", request);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.CREATED.value());
        result.put("message", "User created successfully");
        result.put("data", userService.save(request));

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @Operation(summary = "Update User", description = "API update user to database")
    @PutMapping
    public Map<String, Object> updateUser(@RequestBody @Valid UserUpdateRequest request) {
        log.info("Updating user: {}", request);
        userService.update(request);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.ACCEPTED.value());
        result.put("message", "User updated successfully");
        result.put("data", "");

        return result;
    }

    @Operation(summary = "Change Password", description = "API change password for user to database")
    @PatchMapping("/change-pwd")
    public Map<String, Object> changePassword(@RequestBody @Valid UserPasswordRequest request) {
        log.info("Changing password for user: {}", request);
        userService.changePassword(request);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.NO_CONTENT.value());
        result.put("message", "Password updated successfully");
        result.put("data", "");

        return result;
    }

    @Operation(summary = "Delete User", description = "API activate user from database")
    @DeleteMapping("/{userId}")
    public Map<String, Object> deleteUser(@PathVariable @Min(value = 1, message = "ID must be equals or greater than {value}") Long userId) {
        log.info("Deleting user: {}", userId);
        userService.delete(userId);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.NO_CONTENT.value());
        result.put("message", "User deleted successfully");
        result.put("data", "");

        return result;
    }
}
