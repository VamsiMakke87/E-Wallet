package org.wallet.common;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionPayload {

    private Long id;

    private Long fromUserId;

    private Long toUserId;

    private Double amount;

    private String comment;

    private String requestId;

    private String fromUserMail;

    private String toUserMail;


}
