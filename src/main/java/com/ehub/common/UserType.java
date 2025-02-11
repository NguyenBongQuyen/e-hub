package com.ehub.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum UserType {
    @JsonProperty("owner")
    OWNER,
    @JsonProperty("admin")
    ADMIN,
    @JsonProperty("manager")
    MANAGER,
    @JsonProperty("user")
    USER
}
