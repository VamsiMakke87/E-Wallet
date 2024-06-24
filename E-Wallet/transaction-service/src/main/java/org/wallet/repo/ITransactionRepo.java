package org.wallet.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wallet.entity.Transaction;

public interface ITransactionRepo extends JpaRepository<Transaction,Long> {
}
