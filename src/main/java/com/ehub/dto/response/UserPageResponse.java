package com.ehub.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserPageResponse extends PageResponseAbstract {
    private List<UserResponse> users;
}
