package com.bank.accountapi;

public enum TransactionType {
    DEPOSIT("deposit"),
    WITHDRAWAL("withdrawal");

    private String type;

    TransactionType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
