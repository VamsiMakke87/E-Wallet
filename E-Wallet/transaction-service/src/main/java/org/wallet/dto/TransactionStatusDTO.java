package org.wallet.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.wallet.TransactionStatusEnum;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionStatusDTO {

    private TransactionStatusEnum status;

    private String message;


}
