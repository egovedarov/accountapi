package com.bank.accountapi.dto;

/**
 * Workaround for issue https://github.com/springfox/springfox/issues/597
 */
public class OpenApiAccount {

    private CreateAccount account;

    public OpenApiAccount() {
    }

    public void setAccount(CreateAccount account) {
        this.account = account;
    }

    public CreateAccount getAccount() {
        return account;
    }
}
