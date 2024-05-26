package ma.enset.bankbackend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class TransferDTO {
    private String accountSource;
    private String accountDestination;
    private double amount;
    private String description;
}