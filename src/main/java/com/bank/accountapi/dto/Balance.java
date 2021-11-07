package com.bank.accountapi.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import java.math.BigDecimal;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;

@JsonRootName(value = "balance")
public class Balance {

    @Digits(integer = Integer.MAX_VALUE, fraction = 2)
    @Min(value = 0L, message = "The value must be positive")
    private BigDecimal amount;

    public Balance() {
    }

    public Balance(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
