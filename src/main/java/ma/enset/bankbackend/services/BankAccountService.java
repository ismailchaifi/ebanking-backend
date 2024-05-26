package ma.enset.bankbackend.services;

import ma.enset.bankbackend.dtos.*;
import ma.enset.bankbackend.entities.BankAccount;
import ma.enset.bankbackend.exceptions.AccountNotFoundException;
import ma.enset.bankbackend.exceptions.CustomerNotFoundException;
import ma.enset.bankbackend.exceptions.InsufficientBalanceException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BankAccountService {
    CustomerDTO saveCustomer(CustomerDTO customerDTO);
    List<CustomerDTO> listCustomers();
    CustomerDTO getCustomer(Long id) throws CustomerNotFoundException;
    void deleteCustomer(Long customerID);

    BankAccountDTO getBankAccount(String id) throws AccountNotFoundException;
    List<BankAccountDTO> listBankAccounts();
    CurrentAccountDTO saveCurrentBankAccount(double initialBalance, double overdraft, Long customerID) throws CustomerNotFoundException;
    SavingsAccountDTO saveSavingsBankAccount(double initialBalance, double interestRate, Long customerID) throws CustomerNotFoundException;
    DebitDTO debit(String accountID, double amount, String description) throws AccountNotFoundException, InsufficientBalanceException;
    CreditDTO credit(String accountID, double amount, String description) throws AccountNotFoundException;
    TransferDTO transfer(String accountIDSource, String accountIDDestination, double amount) throws InsufficientBalanceException, AccountNotFoundException;

    AccountHistoryDTO accountHistory(String accountID, int page, int size) throws AccountNotFoundException;

    List<CustomerDTO> searchCustomers(String keyword);
}
