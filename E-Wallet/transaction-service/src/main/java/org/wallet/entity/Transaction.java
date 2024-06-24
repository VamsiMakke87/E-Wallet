package org.wallet.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.wallet.TransactionStatusEnum;

import java.time.OffsetDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false,unique = true,updatable = false)
    private String transactionId;

    @Column(nullable = false,updatable = false)
    private Long fromUserId;

    @Column(nullable = false,updatable = false)
    private Long toUserId;

    private Double amount;

    private String comment;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionStatusEnum status;

    @CreationTimestamp
    @Column(nullable = false,updatable = false)
    private OffsetDateTime time;


}
