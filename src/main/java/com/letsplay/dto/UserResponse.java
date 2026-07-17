package com.letsplay.dto;

import com.letsplay.model.Role;
import com.letsplay.model.User;

public record UserResponse(String id, String username, String email, Role role) {

    public static UserResponse fromUser(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
    }
}
