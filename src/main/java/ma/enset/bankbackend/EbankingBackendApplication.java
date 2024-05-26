package ma.enset.bankbackend;

import ma.enset.bankbackend.dtos.CurrentAccountDTO;
import ma.enset.bankbackend.dtos.CustomerDTO;
import ma.enset.bankbackend.dtos.SavingsAccountDTO;
import ma.enset.bankbackend.entities.AccountOperation;
import ma.enset.bankbackend.entities.CurrentAccount;
import ma.enset.bankbackend.entities.Customer;
import ma.enset.bankbackend.entities.SavingsAccount;
import ma.enset.bankbackend.entities.enums.AccountStatus;
import ma.enset.bankbackend.entities.enums.OperationType;
import ma.enset.bankbackend.exceptions.AccountNotFoundException;
import ma.enset.bankbackend.exceptions.CustomerNotFoundException;
import ma.enset.bankbackend.exceptions.InsufficientBalanceException;
import ma.enset.bankbackend.mappers.BankAccountMapper;
import ma.enset.bankbackend.repositories.AccountOperationRepository;
import ma.enset.bankbackend.repositories.BankAccountRepository;
import ma.enset.bankbackend.repositories.CustomerRepository;
import ma.enset.bankbackend.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankingBackendApplication.class, args);
    }

    @Bean
    CommandLineRunner start(BankAccountService bankAccountService) {
        return args -> {
            Stream.of("customer1", "customer2", "customer3").forEach(name -> {
                CustomerDTO customerDTO = new CustomerDTO();
                customerDTO.setName(name);
                customerDTO.setEmail(name+"@gmail.com");
                bankAccountService.saveCustomer(customerDTO);
            });
            bankAccountService.listCustomers().forEach(customer -> {
                try {
                    bankAccountService.saveCurrentBankAccount(Math.random()*90000, 9000, customer.getId());
                    bankAccountService.saveSavingsBankAccount(Math.random()*90000, 5.5, customer.getId());

                } catch (CustomerNotFoundException e) {
                    throw new RuntimeException(e);
                }
            });
            bankAccountService.listBankAccounts().forEach(bankAccount -> {
                for (int i = 0; i < 5; i++) {
                    try {
                        String accountID;
                        if (bankAccount instanceof CurrentAccountDTO) {
                            accountID = ((CurrentAccountDTO) bankAccount).getId();
                        } else {
                            accountID = ((SavingsAccountDTO) bankAccount).getId();
                        }
                        bankAccountService.credit(accountID, 10000+Math.random()*120000, "Credit");
                        bankAccountService.debit(accountID, 1000+Math.random()*9000, "Debit");


                    } catch (AccountNotFoundException | InsufficientBalanceException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        };
    }

}
