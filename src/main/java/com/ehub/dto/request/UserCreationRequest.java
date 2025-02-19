package com.ehub.dto.request;

import com.ehub.common.Gender;
import com.ehub.common.UserStatus;
import com.ehub.common.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class UserCreationRequest implements Serializable {
    @NotBlank(message = "firstName must be not blank")
    private String firstName;
    @NotBlank(message = "lastName must be not blank")
    private String lastName;
    private Gender gender;
    private Date birthday;
    private String username;
    private String password;
    @Email(message = "Email invalid")
    private String email;
    private String phone;
    private UserType type;
    private UserStatus status;
    private List<AddressRequest> addresses;
}
