package com.bank.accountapi.dto;

import com.bank.accountapi.entity.Transaction;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonRootName;
import java.math.BigDecimal;

@JsonRootName(value = "transaction")
public class CreateTransaction {

    @JsonProperty(access = Access.READ_ONLY)
    private BigDecimal amount;

    @JsonProperty(access = Access.READ_ONLY)
    private String type;

    public CreateTransaction() {
    }

    public CreateTransaction(Transaction transaction) {
        this.amount = transaction.getAmount();
        this.type = transaction.getType();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
