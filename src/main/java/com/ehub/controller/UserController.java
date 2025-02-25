package com.ehub.controller;

import com.ehub.dto.request.UserCreationRequest;
import com.ehub.dto.request.UserPasswordRequest;
import com.ehub.dto.request.UserUpdateRequest;
import com.ehub.dto.response.ApiResponse;
import com.ehub.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    @PreAuthorize("hasAnyAuthority('Admin', 'Manager')")
//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ApiResponse getList(@RequestParam(required = false) String keyword,
                               @RequestParam(required = false) String sort,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "20") int size) {
        log.info("Get user list");

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("User list")
                .data(userService.findAll(keyword, sort, page, size))
                .build();
    }

    @Operation(summary = "Get User Detail", description = "API retrieve user detail by ID from database")
    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('Admin', 'Manager', 'User')")
    public ApiResponse getUserDetail(@PathVariable @Min(value = 1, message = "ID must be equals or greater than {value}") Long userId) {
        log.info("Get user detail by ID: {}", userId);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("user")
                .data(userService.findById(userId))
                .build();
    }

    @Operation(summary = "Create User", description = "API add new user to database")
    @PostMapping
    @PreAuthorize("hasAuthority('Admin')")
    public ApiResponse createUser(@RequestBody @Valid UserCreationRequest request) {
        log.info("Create User: {}", request);

        return ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message("User created successfully")
                .data(userService.save(request))
                .build();
    }

    @Operation(summary = "Update User", description = "API update user to database")
    @PutMapping
    @PreAuthorize("hasAnyAuthority('Admin', 'Manager', 'User')")
    public ApiResponse updateUser(@RequestBody @Valid UserUpdateRequest request) {
        log.info("Updating user: {}", request);
        userService.update(request);

        return ApiResponse.builder()
                .status(HttpStatus.ACCEPTED.value())
                .message("User updated successfully")
                .data("")
                .build();
    }

    @Operation(summary = "Change Password", description = "API change password for user to database")
    @PatchMapping("/change-pwd")
    @PreAuthorize("hasAuthority('User')")
    public ApiResponse changePassword(@RequestBody @Valid UserPasswordRequest request) {
        log.info("Changing password for user: {}", request);
        userService.changePassword(request);

        return ApiResponse.builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Password updated successfully")
                .data("")
                .build();
    }

    @Operation(summary = "Delete User", description = "API activate user from database")
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('Admin')")
    public ApiResponse deleteUser(@PathVariable @Min(value = 1, message = "ID must be equals or greater than {value}") Long userId) {
        log.info("Deleting user: {}", userId);
        userService.delete(userId);

        return ApiResponse.builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("User deleted successfully")
                .data("")
                .build();
    }
}
