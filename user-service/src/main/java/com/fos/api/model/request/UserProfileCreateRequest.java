package com.fos.api.model.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileCreateRequest {
    @NotNull
    @Size(max = 100)
    private String deliveryAddress;

    @NotNull
    private String city;

    @NotNull
    private String postalCode;

    @Size(max = 255)
    private String profilePicture;
    @NotNull
    private Integer userId;

}
