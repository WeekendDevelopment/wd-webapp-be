package com.backend.webapp.service;

import com.backend.webapp.document.Messages;
import com.backend.webapp.repository.MessagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Component
public class MessageService {

    @Autowired
    private MessagesRepository messagesRepository;

    public List<Messages> getMessagesBetweenUsers(String currentUser, String otherUser, LocalDateTime timestamp)
            throws ExecutionException, InterruptedException {
        // List<Messages> messages = new ArrayList<>();
        // CompletableFuture<List<Messages>> messagesFromCurrentUser = retrieveMessagesFromDB(currentUser, otherUser,
        // timestamp);
        // CompletableFuture<List<Messages>> messagesToCurrentUser = retrieveMessagesFromDB(otherUser, currentUser,
        // timestamp);
        // CompletableFuture.allOf(messagesFromCurrentUser, messagesToCurrentUser).join();
        // messages.addAll(messagesFromCurrentUser.get());
        // messages.addAll(messagesToCurrentUser.get());
        // messages.sort(this.messagesComparator());
        List<Messages> messages = messagesRepository.findByMessageFromAndMessageTo(currentUser, otherUser, timestamp);
        return messages;
    }

    public Comparator<Messages> messagesComparator() {
        return (message1, message2) -> {
            if (message1.getTimestamp().isBefore(message2.getTimestamp())) {
                return -1;
            }
            if (message1.getTimestamp().isAfter(message2.getTimestamp())) {
                return 1;
            }
            return 0;
        };
    }

    @Async
    public CompletableFuture<List<Messages>> retrieveMessagesFromDB(String sender, String recipient,
            LocalDateTime timestamp) {
        List<Messages> messages = messagesRepository.findByMessageFromAndMessageTo(sender, recipient, timestamp);
        return CompletableFuture.completedFuture(messages);
    }

    @Async
    public Future<?> updateMessagesToRead() {
        return null;
    }

}
