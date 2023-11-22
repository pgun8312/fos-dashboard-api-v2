package com.fos.api.model.response;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {
    private Integer id;

    private String deliveryAddress;

    private String city;
    private String postalCode;

    private String profilePicture;
    private String modifiedDate;

    private Integer userId;
}
