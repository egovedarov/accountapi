package com.bank.accountapi.repository;

import com.bank.accountapi.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Long> {

    Page<Account> findAll(Pageable pageable);
}
