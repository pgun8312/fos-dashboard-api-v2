package com.fos.api.model.response;

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
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Integer id;
    private String userSub;
    private String userName;
    private String name;
    private String phone;
    private String email;
    private Constants.UserStatus status;
    private Constants.UserRole role;
    private String createdDate;
    private String modifiedDate;
}
