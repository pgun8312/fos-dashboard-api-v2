package com.fos.api.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {
    @NotNull(message = "User name is required")
    private String userName;
    @NotNull(message = "name is required")
    private String name;
    @NotNull(message = "phone number is required")
    private String phone;
    @NotNull(message = "email is required")
    @Email(message = "incorrect email format")
    private String email;
}
