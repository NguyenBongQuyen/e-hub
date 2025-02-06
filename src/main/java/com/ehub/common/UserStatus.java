package com.ehub.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum UserStatus {
    @JsonProperty("none")
    NONE,
    @JsonProperty("active")
    ACTIVE,
    @JsonProperty("inactive")
    INACTIVE
}
