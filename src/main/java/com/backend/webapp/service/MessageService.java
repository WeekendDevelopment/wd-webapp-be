package com.backend.webapp.service;

import com.backend.webapp.constant.ApplicationConstants.MessageStatus;
import com.backend.webapp.document.Messages;
import com.backend.webapp.document.Users;
import com.backend.webapp.exception.CustomError;
import com.backend.webapp.model.UserHistory;
import com.backend.webapp.repository.MessagesRepository;
import com.backend.webapp.repository.UsersRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class MessageService {

    @Autowired
    private MessagesRepository messagesRepository;

    @Autowired
    private UserService userService;

    public List<Messages> getMessagesBetweenUsers(String currentUser, String otherUser, LocalDateTime timestamp) {
        List<Messages> messages = messagesRepository.getMessagesBetweenUsers(currentUser, otherUser, timestamp);
        this.updateMessagesToRead(messages, currentUser, otherUser);
        return messages;
    }

    public List<UserHistory> getMessageHistory(String currentUser) throws CustomError {
        List<UserHistory> history = new ArrayList<>();
        List<String> users = userService.getUser(currentUser).getHistory();
        if (users != null && !users.isEmpty()) {
            users.parallelStream().forEach((otherUser) -> {
                this.fetchLastUnreadMessage(history, currentUser, otherUser);
            });
        }
        return history;
    }

    private void fetchLastUnreadMessage(List<UserHistory> history, String currentUser, String otherUser) {
        UserHistory userHistory = new UserHistory();
        userHistory.setEmail(otherUser);
        Optional<Messages> lastUnreadMessage = messagesRepository.getLastUnreadMessage(currentUser, otherUser);
        if (lastUnreadMessage.isPresent()) {
            userHistory
                    .setLastMessage(messagesRepository.getLastUnreadMessage(currentUser, otherUser).get().getMessage());
        }
        // TODO: Get noOfUnreadMessages
        history.add(userHistory);
    }

    /**
     * Updates incoming messages to READ
     *
     * @param messages
     * @param currentUser
     * @param otherUser
     */
    @Async
    public void updateMessagesToRead(List<Messages> messages, String currentUser, String otherUser) {
        messages.parallelStream().forEach((message) -> {
            if (otherUser.equalsIgnoreCase(message.getMessageFrom())
                    && currentUser.equalsIgnoreCase(message.getMessageTo())) {
                message.setMessageStatus(MessageStatus.READ);
                messagesRepository.save(message);
            }
        });
    }

}
