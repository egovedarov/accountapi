package com.bank.accountapi.dto;

import org.springframework.data.domain.Page;

public class TransactionList<E> {

    private final Page<E> transactions;

    public TransactionList(Page<E> transactions) {
        this.transactions = transactions;
    }

    public Page<E> getTransactions() {
        return transactions;
    }
}
