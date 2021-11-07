package com.bank.accountapi.dto;

/**
 * Workaround for issue https://github.com/springfox/springfox/issues/597
 */
public class OpenApiWithdrawal {

    private Withdrawal withdrawal;

    public OpenApiWithdrawal() {
    }

    public void setWithdrawal(Withdrawal withdrawal) {
        this.withdrawal = withdrawal;
    }

    public Withdrawal getWithdrawal() {
        return withdrawal;
    }
}
