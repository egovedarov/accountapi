package com.bank.accountapi.dto;

/**
 * Workaround for issue https://github.com/springfox/springfox/issues/597
 */
public class OpenApiBalance {

    private Balance balance;

    public OpenApiBalance() {
    }

    public void setBalance(Balance balance) {
        this.balance = balance;
    }

    public Balance getBalance() {
        return balance;
    }
}
