package org.wallet.common;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionCompletePayload {

    private Long id;

    private boolean isSuccess;

    private String reason;

    private String requestId;

    private Long fromUserId;

    private Long toUserId;

    private Double amount;


    private NotificationStatusEnum notificationStatus;

}
