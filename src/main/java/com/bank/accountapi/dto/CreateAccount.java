package com.bank.accountapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonRootName;
import java.math.BigDecimal;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@JsonRootName(value = "account")
public class CreateAccount {

    @JsonProperty(access = Access.READ_ONLY)
    private long id;

    @NotBlank
    private String name;

    @Digits(integer = Integer.MAX_VALUE, fraction = 2)
    @Min(value = 0L, message = "The value must be positive")
    private BigDecimal limit;

    @JsonProperty(access = Access.READ_ONLY)
    private String createdAt;

    public CreateAccount() {

    }

    public CreateAccount(com.bank.accountapi.entity.Account account) {
        id = account.getId();
        name = account.getName();
        limit = account.getLimit();
        createdAt = account.getCreatedAt().toString();
    }

    public com.bank.accountapi.entity.Account createEntity() {
        com.bank.accountapi.entity.Account account = new com.bank.accountapi.entity.Account();
        account.setName(this.getName());
        account.setLimit(this.getLimit());
        return account;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    public void setLimit(BigDecimal limit) {
        this.limit = limit;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
