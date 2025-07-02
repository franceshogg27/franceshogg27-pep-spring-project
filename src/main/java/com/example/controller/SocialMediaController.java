package com.example.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    @Autowired
    AccountService accountService;
    @Autowired
    MessageService messageService;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    MessageRepository messageRepository;

    /* As a user, I should be able to create a new Account on the endpoint POST localhost:8080/register. The body will 
    contain a representation of a JSON Account, but will not contain an accountId.

- The registration will be successful if and only if the username is not blank, the password is at least 4 characters long, 
and an Account with that username does not already exist. If all these conditions are met, the response body should contain a 
JSON of the Account, including its accountId. The response status should be 200 OK, which is the default. The new account 
should be persisted to the database.
- If the registration is not successful due to a duplicate username, the response status should be 409. (Conflict)
- If the registration is not successful for some other reason, the response status should be 400. (Client error) */

    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Account account){
        Account dupChecker = accountRepository.findByUsername(account.getUsername());
        if (dupChecker != null) {
            return ResponseEntity.status(409).body(account);
        }
        if (account.getUsername() == null || account.getUsername().length() == 0 || account.getPassword().length() < 4) {
            return ResponseEntity.status(400).body(account);
        }
        return ResponseEntity.ok(accountService.createAccount(account));
    }

    /* As a user, I should be able to verify my login on the endpoint POST localhost:8080/login. The request body will 
    contain a JSON representation of an Account.

- The login will be successful if and only if the username and password provided in the request body JSON match a real 
account existing on the database. If successful, the response body should contain a JSON of the account in the response 
body, including its accountId. The response status should be 200 OK, which is the default.
- If the login is not successful, the response status should be 401. (Unauthorized) */

    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account account){
        Account savedAccount = accountRepository.findByUsername(account.getUsername());
        if (savedAccount == null || !savedAccount.getPassword().equals(account.getPassword())) {
            return ResponseEntity.status(401).body(account);
        }
        return ResponseEntity.ok(accountService.login(account.getUsername(), account.getPassword()));
    }

    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message){
        String text = message.getMessageText();
        if (text.length() > 0 && text.length() <= 255 && accountRepository.existsById(message.getPostedBy())) {
            return ResponseEntity.ok(messageService.addMessage(message));
        }
        return ResponseEntity.status(400).build();
    }

    @GetMapping("/messages")
    public @ResponseBody List<Message> getAllMessages(){
        return messageService.getAllMessages();
    }

    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer messageId){
        if (messageId == null || !messageRepository.existsById(messageId)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.ok(messageService.getMessageById(messageId));
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessageById(@PathVariable Integer messageId){
        if (messageId == null || !messageRepository.existsById(messageId)) {
            return ResponseEntity.ok().build();
        }
        messageService.deleteMessageById(messageId);
        return ResponseEntity.ok(1);
    }

    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> updateMessage(@PathVariable Integer messageId, @RequestBody Message message){
        String text = message.getMessageText();
        if (!messageRepository.existsById(messageId) || text == null || text.length() == 0 || text.length() > 255) {
            return ResponseEntity.status(400).body(0);
        }
        messageService.updateMessage(messageId, message);
        return ResponseEntity.ok(1);
    }

    @GetMapping("/accounts/{accountId}/messages")
    public @ResponseBody List<Message> getAllMessagesByUser(@PathVariable Integer accountId){
        return messageService.getMessagesByUser(accountId);
    }
}
