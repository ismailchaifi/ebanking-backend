package ma.enset.bankbackend.dtos;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import ma.enset.bankbackend.entities.enums.AccountStatus;

import java.util.Date;

@Data
public class CurrentAccountDTO extends BankAccountDTO {
    private double overdraft;
}
