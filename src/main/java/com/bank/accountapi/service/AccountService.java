package com.bank.accountapi.service;

import com.bank.accountapi.dto.Balance;
import com.bank.accountapi.dto.BaseAmount;
import com.bank.accountapi.dto.CreateAccount;
import com.bank.accountapi.dto.CreateTransaction;
import com.bank.accountapi.dto.Deposit;
import com.bank.accountapi.dto.Withdrawal;
import com.bank.accountapi.entity.Account;
import com.bank.accountapi.entity.Transaction;
import com.bank.accountapi.repository.AccountRepository;
import com.bank.accountapi.repository.TransactionRepository;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionRepository transactionRepository;

    public CreateAccount findById(long id) {
        Account accountEntity = accountRepository.findById(id).orElse(null);

        if (accountEntity == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }

        return new CreateAccount(accountEntity);
    }

    public Page<CreateAccount> findAll(Pageable pageable) {
        return accountRepository.findAll(pageable).map(CreateAccount::new);
    }

    public CreateAccount createAccount(CreateAccount accountDto) {
        Account accountEntity = accountDto.createEntity();
        accountRepository.save(accountEntity);

        accountDto.setId(accountEntity.getId());
        accountDto.setCreatedAt(accountEntity.getCreatedAt().toString());
        return accountDto;
    }

    public Balance handleDeposit(Long id, Deposit deposit) throws MethodArgumentNotValidException {
        Account accountEntity = accountRepository.findById(id)
            .orElse(null);

        if (accountEntity == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }

        if (accountEntity.getLimit().equals(BigDecimal.ZERO) &&
            deposit.getAmount().compareTo(accountEntity.getLimit()) > 0) {
            handleDepositAmountError(deposit,
                "Amount is bigger than the allowed limit for this account");
        }

        Transaction transaction = deposit.toEntity();
        transaction.setAccountId(accountEntity.getId());
        transactionRepository.save(transaction);

        accountEntity.setBalance(accountEntity.getBalance().add(deposit.getAmount()));
        accountRepository.save(accountEntity);
        return new Balance(accountEntity.getBalance());
    }

    public Balance handleWithdrawal(Long id, Withdrawal withdrawal)
        throws MethodArgumentNotValidException {
        Account accountEntity = accountRepository.findById(id)
            .orElse(null);

        if (accountEntity == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }

        if (accountEntity.getLimit().equals(BigDecimal.ZERO)
            && withdrawal.getAmount().compareTo(accountEntity.getLimit()) > 0) {
            handleDepositAmountError(withdrawal,
                "Amount is bigger than the allowed limit for this account");
        }

        if (withdrawal.getAmount().compareTo(accountEntity.getBalance()) > 0) {
            handleDepositAmountError(withdrawal,
                "Amount is bigger than the current balance for this account");
        }

        Transaction transaction = withdrawal.toEntity();
        transaction.setAccountId(accountEntity.getId());
        transactionRepository.save(transaction);

        accountEntity.setBalance(accountEntity.getBalance().subtract(withdrawal.getAmount()));
        accountRepository.save(accountEntity);
        return new Balance(accountEntity.getBalance());
    }

    public Balance getBalance(long id) {
        Account accountEntity = accountRepository.findById(id).orElse(null);

        if (accountEntity == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }

        return new Balance(accountEntity.getBalance());
    }

    public Page<CreateTransaction> getTransactions(long accountId, Pageable pageable) {
        Account accountEntity = accountRepository.findById(accountId).orElse(null);

        if (accountEntity == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }

        return transactionRepository.findAllByAccountId(accountId, pageable)
            .map(CreateTransaction::new);
    }

    private void handleDepositAmountError(BaseAmount baseAmount, String errorMessage)
        throws MethodArgumentNotValidException {
        BindingResult bindingResult = new MapBindingResult(new HashMap<>(), "baseAmount");
        bindingResult.addError(new FieldError("baseAmount", "amount",
            errorMessage));
        try {
            Method method = baseAmount.getClass().getMethod("getAmount", (Class<?>[]) null);
            MethodParameter parameter = new MethodParameter(method, -1);
            throw new MethodArgumentNotValidException(parameter, bindingResult);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
