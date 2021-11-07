package com.bank.accountapi.dto;

/**
 * Workaround for issue https://github.com/springfox/springfox/issues/597
 */
public class OpenApiDeposit {

    private Deposit deposit;

    public OpenApiDeposit() {
    }

    public void setDeposit(Deposit deposit) {
        this.deposit = deposit;
    }

    public Deposit getDeposit() {
        return deposit;
    }
}
