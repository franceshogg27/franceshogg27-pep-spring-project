package com.example.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

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
@Controller
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;
    AccountRepository accountRepository;
    MessageRepository messageRepository;

    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Account account){
        if (accountRepository.findByUsername(account.getUsername()) != null) {
            return ResponseEntity.status(400).body(account);
        }
        if (account.getUsername() == null || account.getPassword().length() < 4) {
            return ResponseEntity.status(409).body(account);
        }
        return ResponseEntity.ok(accountService.createAccount(account));
    }

    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account account){
        Account savedAccount = accountRepository.findByUsername(account.getUsername());
        if (savedAccount.getUsername() == null || savedAccount.getUsername() != account.getPassword()) {
            return ResponseEntity.status(401).body(account);
        }
        return ResponseEntity.ok(accountService.login(account.getUsername(), account.getPassword()));
    }

    @PostMapping("/messages")
    public ResponseEntity<Message> creatMessages(@RequestBody Message message){
        String text = message.getMessageText();
        if (text != null && text.length() > 0 && text.length() <= 255 && accountRepository.findById(message.getPostedBy()) != null) {
            return ResponseEntity.ok(messageService.addMessage(message));
        }
        return ResponseEntity.status(400).body(messageService.addMessage(message));
    }

    @GetMapping("/messages")
    public @ResponseBody List<Message> getAllMessages(){
        return messageService.getAllMessages();
    }

    @GetMapping("/messages/{messageId}")
    public @ResponseBody Message getMessageById(@PathVariable Integer id){
        return messageService.getMessageById(id);
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessageById(@PathVariable Integer id){
        Optional<Message> message = messageRepository.findById(id);
        if (message == null) {
            return ResponseEntity.ok().build();
        }
        messageService.deleteMessageById(id);
        return ResponseEntity.ok(1);
    }

    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> updateMessage(@PathVariable Integer id, @RequestBody Message message){
        String text = message.getMessageText();
        if (messageRepository.findById(id) == null || text != null && text.length() > 0 && text.length() <= 255) {
            return ResponseEntity.status(400).body(0);
        }
        messageService.updateMessage(id, message);
        return ResponseEntity.ok(1);
    }

    @GetMapping("/accounts/{accountId}/messages")
    public @ResponseBody List<Message> getAllMessagesByUser(@PathVariable String username){
        return messageService.getMessagesByUser(username);
    }
}
