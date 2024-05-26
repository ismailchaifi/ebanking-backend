package ma.enset.bankbackend.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("SAV")
@EqualsAndHashCode(callSuper = true)
@Data @NoArgsConstructor @AllArgsConstructor
public class SavingsAccount extends BankAccount {
    private double interestRate;
}
