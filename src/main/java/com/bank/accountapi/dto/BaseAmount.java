package com.bank.accountapi.dto;

import com.bank.accountapi.entity.Transaction;
import java.math.BigDecimal;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

abstract public class BaseAmount {

    @Digits(integer = Integer.MAX_VALUE, fraction = 2, message = "Amount must have at most 2 fractional digits")
    @Min(value = 0L, message = "The value must be positive")
    @NotNull
    private BigDecimal amount;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BaseAmount() {
    }

    public abstract Transaction toEntity();
}
