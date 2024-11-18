package rest.repositories;

import models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAccountJpaRepository extends JpaRepository<Account, Integer> {
    Account getAccountByUserId(String id);
}
