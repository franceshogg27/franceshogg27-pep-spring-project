package com.example.service;

import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;


@Service
public class AccountService {

    AccountRepository accountRepository;

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public Account login(String username, String password) {
        return accountRepository.findByUsername(username);
    }



}
