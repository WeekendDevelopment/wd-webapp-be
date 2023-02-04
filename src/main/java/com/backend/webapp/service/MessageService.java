package com.backend.webapp.service;

import com.backend.webapp.constant.ApplicationConstants.MessageStatus;
import com.backend.webapp.document.Messages;
import com.backend.webapp.repository.MessagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class MessageService {

    @Autowired
    private MessagesRepository messagesRepository;

    public List<Messages> getMessagesBetweenUsers(String currentUser, String otherUser, LocalDateTime timestamp) {
        List<Messages> messages = messagesRepository.findByMessageFromAndMessageTo(currentUser, otherUser, timestamp);
        this.updateMessagesToRead(messages);
        return messages;
    }

    @Async
    public void updateMessagesToRead(List<Messages> messages) {
        messages.parallelStream().forEach((message) -> {
            message.setMessageStatus(MessageStatus.READ);
            messagesRepository.save(message);
        });
    }

}
