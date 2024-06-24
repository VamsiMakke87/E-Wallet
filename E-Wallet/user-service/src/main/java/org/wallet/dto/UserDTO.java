package org.wallet.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class UserDTO {

    private Long id;


    @NotNull
    @Size(max=255)
    private String name;

    @NotNull
    @Size(max=255)
    private String email;


    @NotNull
    @Size(max=255)
    private String phone;

    private String kycNumber;

}
