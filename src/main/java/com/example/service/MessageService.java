package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {

    @Autowired
    MessageRepository messageRepository;
    @Autowired
    AccountRepository accountRepository;

    public Message addMessage(Message message) {
        return messageRepository.save(message);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message getMessageById(Integer id) {
        if (messageRepository.getById(id) == null) {
            return null;
        }
        return messageRepository.getById(id);
    }

    public void deleteMessageById(Integer id) {
        messageRepository.deleteById(id);
    }

    public Message updateMessage(Integer id, Message message) {
        return messageRepository.save(message);
    }

    public List<Message> getMessagesByUser(Integer accountId) {
        String username = accountRepository.getById(accountId).getUsername();
        Account account = accountRepository.findByUsername(username);
        return messageRepository.findByMessageId(account.getAccountId());
    }
}
