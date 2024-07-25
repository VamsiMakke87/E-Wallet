package org.wallet.common;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TransactionCompletePayload {

    private Long id;

    private String fromUserEmail;

    private String toUserEmail;

    private String txnId;

    private boolean isSuccess;

    private String reason;

    private String requestId;

    private Long fromUserId;

    private Long toUserId;

    private Double amount;

    private Double fromUserBalance;

    private Double toUserBalance;

}
