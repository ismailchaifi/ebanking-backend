package ma.enset.bankbackend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class DebitDTO {
    private String accountId;
    private double amount;
    private String description;
}