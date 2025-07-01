package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;

@Service
public class MessageService {

    MessageRepository messageRepository;

    public Message addMessage(Message message) {
        return messageRepository.save(message);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message getMessageById(Integer id) {
        return messageRepository.getById(id);
    }

    public Message deleteMessageById(Integer id) {
        Message message = messageRepository.getById(id);
        messageRepository.deleteById(id);
        return message;
    }

    public Message updateMessage(Integer id, Message message) {
        return messageRepository.save(message);
    }

    public List<Message> getMessagesByUser(String username) {
        return messageRepository.findByUsername(username);
    }

}
