package ma.enset.bankbackend.dtos;

import lombok.Data;
import ma.enset.bankbackend.entities.enums.AccountStatus;

import java.util.Date;

@Data
public class BankAccountDTO {
    private String id;
    private double balance;
    private Date creationDate;
    private AccountStatus status;
    private CustomerDTO customerDTO;
    private String type;
}
