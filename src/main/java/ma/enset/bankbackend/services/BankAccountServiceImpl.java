package ma.enset.bankbackend.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.bankbackend.dtos.*;
import ma.enset.bankbackend.entities.*;
import ma.enset.bankbackend.entities.enums.AccountStatus;
import ma.enset.bankbackend.entities.enums.OperationType;
import ma.enset.bankbackend.exceptions.AccountNotFoundException;
import ma.enset.bankbackend.exceptions.CustomerNotFoundException;
import ma.enset.bankbackend.exceptions.InsufficientBalanceException;
import ma.enset.bankbackend.mappers.BankAccountMapper;
import ma.enset.bankbackend.repositories.AccountOperationRepository;
import ma.enset.bankbackend.repositories.BankAccountRepository;
import ma.enset.bankbackend.repositories.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService{
    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
    private BankAccountMapper mapper;

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Saving new customer...");
        Customer customer = mapper.fromCustomerDTO(customerDTO);
        return mapper.fromCustomer(customerRepository.save(customer));
    }

    @Override
    public CurrentAccountDTO saveCurrentBankAccount(double initialBalance, double overdraft, Long customerID) throws CustomerNotFoundException {
        CurrentAccount account = new CurrentAccount(overdraft);
        CustomerDTO customerDTO = getCustomer(customerID);
        account.setBalance(initialBalance);
        account.setCreationDate(new Date());
        account.setStatus(AccountStatus.CREATED);
        account.setCustomer(mapper.fromCustomerDTO(customerDTO));
        return mapper.fromCurrentAccount(bankAccountRepository.save(account));
    }

    @Override
    public SavingsAccountDTO saveSavingsBankAccount(double initialBalance, double interestRate, Long customerID) throws CustomerNotFoundException {
        SavingsAccount account = new SavingsAccount(interestRate);
        CustomerDTO customerDTO = getCustomer(customerID);
        account.setBalance(initialBalance);
        account.setCreationDate(new Date());
        account.setStatus(AccountStatus.CREATED);
        account.setCustomer(mapper.fromCustomerDTO(customerDTO));
        return mapper.fromSavingsAccount(bankAccountRepository.save(account));
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream().map(customer -> mapper.fromCustomer(customer)).toList();
    }

    @Override
    public List<BankAccountDTO> listBankAccounts() {
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
        return bankAccounts.stream().map(account -> {
            if (account instanceof CurrentAccount) {
                return mapper.fromCurrentAccount((CurrentAccount) account);
            } else {
                return mapper.fromSavingsAccount((SavingsAccount) account);
            }
        }).toList();
    }

    @Override
    public CustomerDTO getCustomer(Long id) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(()->new CustomerNotFoundException("Customer not found"));
        return mapper.fromCustomer(customer);
    }

    @Override
    public void deleteCustomer(Long customerID) {
        customerRepository.deleteById(customerID);
    }

    @Override
    public BankAccountDTO getBankAccount(String id) throws AccountNotFoundException {
        BankAccount account =  bankAccountRepository.findById(id)
                .orElseThrow(()->new AccountNotFoundException("Account not found"));
        if (account instanceof SavingsAccount) {
            return mapper.fromSavingsAccount((SavingsAccount) account);
        } else {
            return mapper.fromCurrentAccount((CurrentAccount) account);
        }
    }

    @Override
    @Transactional
    public DebitDTO debit(String accountID, double amount, String description) throws AccountNotFoundException, InsufficientBalanceException {
        DebitDTO debitDTO = new DebitDTO(accountID, amount, description);
        BankAccount account =  bankAccountRepository.findById(accountID)
                .orElseThrow(()->new AccountNotFoundException("Account not found"));
        double balance = account.getBalance();
        if (balance < amount) {
            throw new InsufficientBalanceException("Insufficient balance");
        } else {
            AccountOperation operation = new AccountOperation();
            operation.setType(OperationType.DEBIT);
            operation.setAmount(amount);
            operation.setDescription(description);
            operation.setOperationDate(new Date());
            operation.setBankAccount(account);
            accountOperationRepository.save(operation);

            account.setBalance(balance - amount);
            bankAccountRepository.save(account);
        }
        return debitDTO;
    }

    @Override
    @Transactional
    public CreditDTO credit(String accountID, double amount, String description) throws AccountNotFoundException {
        CreditDTO creditDTO = new CreditDTO(accountID, amount, description);
        BankAccount account =  bankAccountRepository.findById(accountID)
                .orElseThrow(()->new AccountNotFoundException("Account not found"));
        double balance = account.getBalance();
        AccountOperation operation = new AccountOperation();
        operation.setType(OperationType.CREDIT);
        operation.setAmount(amount);
        operation.setDescription(description);
        operation.setOperationDate(new Date());
        operation.setBankAccount(account);
        accountOperationRepository.save(operation);

        account.setBalance(balance + amount);
        bankAccountRepository.save(account);

        return creditDTO;
    }

    @Override
    @Transactional
    public TransferDTO transfer(String accountIDSource, String accountIDDestination, double amount) throws InsufficientBalanceException, AccountNotFoundException {
        BankAccountDTO sourceAccount = this.getBankAccount(accountIDSource);
        BankAccountDTO destAccount = this.getBankAccount(accountIDDestination);
        TransferDTO transferDTO = new TransferDTO(accountIDSource,
                accountIDDestination,
                amount,
                "Transfer from " + sourceAccount.getCustomerDTO().getName() +
                        " to " + destAccount.getCustomerDTO().getName());
        debit(accountIDSource, amount, "Transfer to " + accountIDDestination);
        credit(accountIDDestination, amount, "Transfer from " + accountIDSource);
        return transferDTO;
    }

    @Override
    public AccountHistoryDTO accountHistory(String accountID, int page, int size) throws AccountNotFoundException {
        AccountHistoryDTO historyDTO = new AccountHistoryDTO();
        BankAccount account =  bankAccountRepository.findById(accountID)
                .orElseThrow(()->new AccountNotFoundException("Account not found"));
        Page<AccountOperation> operations = accountOperationRepository.findByBankAccountId(
                accountID,
                PageRequest.of(page, size));
        List<AccountOperationDTO> accountOperationDTOS = operations.stream().map(operation ->
                mapper.fromAccountOperation(operation)).toList();
        historyDTO.setOperationDTOS(accountOperationDTOS);
        historyDTO.setAccountId(account.getId());
        historyDTO.setBalance(account.getBalance());
        historyDTO.setCurrentPage(page);
        historyDTO.setPageSize(size);
        historyDTO.setTotalPages(operations.getTotalPages());
        return historyDTO;
    }

    @Override
    public List<CustomerDTO> searchCustomers(String keyword) {
        List<Customer> customers = customerRepository.searchCustomers(keyword);
        return customers.stream().map(customer -> mapper.fromCustomer(customer)).toList();
    }

}
