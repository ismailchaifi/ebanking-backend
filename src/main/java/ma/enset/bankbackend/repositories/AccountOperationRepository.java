package ma.enset.bankbackend.repositories;

import ma.enset.bankbackend.dtos.AccountOperationDTO;
import ma.enset.bankbackend.entities.AccountOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {
    List<AccountOperation> findByBankAccountId(String accountID);
    Page<AccountOperation> findByBankAccountId(String accountID, Pageable pageable);
}
