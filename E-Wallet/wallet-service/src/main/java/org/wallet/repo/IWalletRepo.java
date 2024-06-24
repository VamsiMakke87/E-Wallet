package org.wallet.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wallet.entity.Wallet;

public interface IWalletRepo extends JpaRepository<Wallet,Long> {
}
