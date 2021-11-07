package com.bank.accountapi.controller;

import com.bank.accountapi.dto.Balance;
import com.bank.accountapi.dto.CreateAccount;
import com.bank.accountapi.dto.CreateTransaction;
import com.bank.accountapi.dto.Deposit;
import com.bank.accountapi.dto.OpenApiAccount;
import com.bank.accountapi.dto.OpenApiBalance;
import com.bank.accountapi.dto.OpenApiDeposit;
import com.bank.accountapi.dto.OpenApiWithdrawal;
import com.bank.accountapi.dto.TransactionList;
import com.bank.accountapi.dto.Withdrawal;
import com.bank.accountapi.service.AccountService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    AccountService accountService;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/{id}")
    @ApiResponses({
        @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OpenApiAccount.class))),
        @ApiResponse(responseCode = "404", description = "Account not found")})
    public CreateAccount getAccount(@PathVariable Long id) {
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        return accountService.findById(id);
    }

    @GetMapping()
    public TransactionList<CreateAccount> getAccounts(
        @PageableDefault(page = 0, size = 5) Pageable pageable) {
        objectMapper.disable(SerializationFeature.WRAP_ROOT_VALUE);
        return new TransactionList<>(accountService.findAll(pageable));
    }

    @PostMapping()
    @Operation(requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = {
        @Content(mediaType = "application/json", schema = @Schema(implementation = OpenApiAccount.class))}))
    @ApiResponse(responseCode = "200",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = OpenApiAccount.class)))
    public CreateAccount createAccount(@Valid @RequestBody CreateAccount account) {
        objectMapper.disable(DeserializationFeature.UNWRAP_ROOT_VALUE);
        return accountService.createAccount(account);
    }

    @PostMapping("/{id}/deposit")
    @Operation(requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = {
        @Content(mediaType = "application/json", schema = @Schema(implementation = OpenApiDeposit.class))}))
    @ApiResponses({
        @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = OpenApiBalance.class))),
        @ApiResponse(responseCode = "404", description = "Account not found")})
    public Balance makeDeposit(@PathVariable Long id, @Valid @RequestBody Deposit deposit)
        throws MethodArgumentNotValidException {
        objectMapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        return accountService.handleDeposit(id, deposit);
    }

    @PostMapping("/{id}/withdrawal")
    @Operation(requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = {
        @Content(mediaType = "application/json", schema = @Schema(implementation = OpenApiWithdrawal.class))}))
    @ApiResponses({
        @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = OpenApiBalance.class))),
        @ApiResponse(responseCode = "404", description = "Account not found")})
    public Balance makeWithdrawal(@PathVariable Long id, @Valid @RequestBody Withdrawal withdrawal)
        throws MethodArgumentNotValidException {
        objectMapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        return accountService.handleWithdrawal(id, withdrawal);
    }

    @GetMapping("/{id}/balance")
    @ApiResponses({
        @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = OpenApiBalance.class))),
        @ApiResponse(responseCode = "404", description = "Account not found")})
    public Balance getBalance(@PathVariable Long id)
        throws MethodArgumentNotValidException {
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        return accountService.getBalance(id);
    }

    @GetMapping("/{id}/transactions")
    public TransactionList<CreateTransaction> getTransactions(@PathVariable Long id,
        @PageableDefault(size = 5) Pageable pageable)
        throws MethodArgumentNotValidException {
        objectMapper.disable(SerializationFeature.WRAP_ROOT_VALUE);
        return new TransactionList<>(accountService.getTransactions(id, pageable));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
        MethodArgumentNotValidException exception) {
        objectMapper.disable(SerializationFeature.WRAP_ROOT_VALUE);
        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
