package org.wallet.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wallet.entity.User;

public interface IUserRepo extends JpaRepository<User,Long> {
}
