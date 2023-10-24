package com.fos.api.model.request;

import com.fos.api.common.Constants;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderUpdateRequest {
    @NotNull(message = "status is required")
    private Constants.OrderStatus status;
}
