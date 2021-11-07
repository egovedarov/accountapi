package com.bank.accountapi.repository;

import com.bank.accountapi.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface TransactionRepository extends CrudRepository<Transaction, Integer> {

    Page<Transaction> findAllByAccountId(Long accountId, Pageable pageable);
}
