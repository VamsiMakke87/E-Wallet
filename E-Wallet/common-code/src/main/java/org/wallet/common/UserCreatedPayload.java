package org.wallet.common;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreatedPayload {

    private Long userId;
    private String username;

    private String email;

    private String requestId;

    private NotificationStatusEnum notificationStatus;



}
