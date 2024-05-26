package ma.enset.bankbackend.mappers;

import ma.enset.bankbackend.dtos.AccountOperationDTO;
import ma.enset.bankbackend.dtos.CurrentAccountDTO;
import ma.enset.bankbackend.dtos.CustomerDTO;
import ma.enset.bankbackend.dtos.SavingsAccountDTO;
import ma.enset.bankbackend.entities.AccountOperation;
import ma.enset.bankbackend.entities.CurrentAccount;
import ma.enset.bankbackend.entities.Customer;
import ma.enset.bankbackend.entities.SavingsAccount;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class BankAccountMapper {
    public CustomerDTO fromCustomer(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);
        return customerDTO;
    }
    public Customer fromCustomerDTO(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        return customer;
    }

    public CurrentAccountDTO fromCurrentAccount(CurrentAccount currentAccount) {
        CurrentAccountDTO currentAccountDTO = new CurrentAccountDTO();
        BeanUtils.copyProperties(currentAccount, currentAccountDTO);
        currentAccountDTO.setCustomerDTO(fromCustomer(currentAccount.getCustomer()));
        currentAccountDTO.setType(currentAccount.getClass().getSimpleName());
        return currentAccountDTO;
    }

    public CurrentAccount fromCurrentAccountDTO(CurrentAccountDTO currentAccountDTO) {
        CurrentAccount currentAccount = new CurrentAccount();
        BeanUtils.copyProperties(currentAccountDTO, currentAccount);
        currentAccount.setCustomer(fromCustomerDTO(currentAccountDTO.getCustomerDTO()));
        return currentAccount;
    }

    public SavingsAccountDTO fromSavingsAccount(SavingsAccount savingsAccount) {
        SavingsAccountDTO savingsAccountDTO = new SavingsAccountDTO();
        BeanUtils.copyProperties(savingsAccount, savingsAccountDTO);
        savingsAccountDTO.setCustomerDTO(fromCustomer(savingsAccount.getCustomer()));
        savingsAccountDTO.setType(savingsAccount.getClass().getSimpleName());
        return savingsAccountDTO;
    }

    public SavingsAccount fromSavingsAccountDTO(SavingsAccountDTO savingsAccountDTO) {
        SavingsAccount savingsAccount = new SavingsAccount();
        BeanUtils.copyProperties(savingsAccountDTO, savingsAccount);
        savingsAccount.setCustomer(fromCustomerDTO(savingsAccountDTO.getCustomerDTO()));
        return savingsAccount;
    }

    public AccountOperationDTO fromAccountOperation(AccountOperation accountOperation) {
        AccountOperationDTO accountOperationDTO = new AccountOperationDTO();
        BeanUtils.copyProperties(accountOperation, accountOperationDTO);
        return accountOperationDTO;
    }

    public AccountOperation fromAccountOperationDTO(AccountOperationDTO accountOperationDTO) {
        AccountOperation accountOperation = new AccountOperation();
        BeanUtils.copyProperties(accountOperationDTO, accountOperation);
        return accountOperation;
    }

}
