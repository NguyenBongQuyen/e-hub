package com.ehub.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class UserPasswordRequest implements Serializable {
    @NotNull(message = "id must be not null")
    private Long id;
    @NotBlank(message = "password must be not blank")
    private String password;
    @NotBlank(message = "confirmPassword must be not blank")
    private String confirmPassword;
}
