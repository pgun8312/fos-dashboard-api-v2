package com.fos.api.model.request;

import com.fos.api.common.Constants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequest {
    @NotNull(message = "User Sub is required")
    private String userSub;
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
