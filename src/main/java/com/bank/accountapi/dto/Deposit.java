package com.bank.accountapi.dto;

import com.bank.accountapi.TransactionType;
import com.bank.accountapi.entity.Transaction;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "deposit")
public class Deposit extends BaseAmount {

    public Transaction toEntity() {
        Transaction transaction = new Transaction();
        transaction.setAmount(getAmount());
        transaction.setType(TransactionType.DEPOSIT.getType());
        return transaction;
    }
}
