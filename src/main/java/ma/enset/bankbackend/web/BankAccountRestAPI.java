package ma.enset.bankbackend.web;

import ma.enset.bankbackend.dtos.*;
import ma.enset.bankbackend.exceptions.AccountNotFoundException;
import ma.enset.bankbackend.exceptions.InsufficientBalanceException;
import ma.enset.bankbackend.services.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BankAccountRestAPI {
    private BankAccountService bankAccountService;

    public BankAccountRestAPI(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @GetMapping("/bankAccounts/{accountID}")
    public BankAccountDTO getBankAccount(@PathVariable String accountID) throws AccountNotFoundException {
        return bankAccountService.getBankAccount(accountID);
    }

    @GetMapping("/bankAccounts")
    public List<BankAccountDTO> bankAccounts() {
        return bankAccountService.listBankAccounts();
    }

    @GetMapping("/bankAccounts/{accountID}/operations")
    public AccountHistoryDTO getHistory(@PathVariable String accountID,
                                        @RequestParam(name = "page", defaultValue = "0") int page,
                                        @RequestParam(name = "size", defaultValue = "5") int size) throws AccountNotFoundException {
        return bankAccountService.accountHistory(accountID, page, size);
    }

    @PostMapping("/bankAccounts/{accountID}/debit")
    public DebitDTO debit(@PathVariable String accountID,
                          @RequestParam(name = "amount") double amount,
                          @RequestParam(name = "description", defaultValue = "Debit") String description) throws InsufficientBalanceException, AccountNotFoundException {
        return bankAccountService.debit(accountID, amount, description);
    }

    @PostMapping("/bankAccounts/{accountID}/credit")
    public CreditDTO credit(@PathVariable String accountID,
                               @RequestParam(name = "amount") double amount,
                               @RequestParam(name = "description", defaultValue = "Credit") String description) throws InsufficientBalanceException, AccountNotFoundException {
        return bankAccountService.credit(accountID, amount, description);
    }

    @PostMapping("/bankAccounts/transfer")
    public TransferDTO transfer(@RequestParam(name = "sourceAccount") String accountIDSource,
                                @RequestParam(name = "destAccount") String accountIDDestination,
                            @RequestParam(name = "amount") double amount) throws InsufficientBalanceException, AccountNotFoundException {
        return bankAccountService.transfer(accountIDSource, accountIDDestination, amount);
    }

}
