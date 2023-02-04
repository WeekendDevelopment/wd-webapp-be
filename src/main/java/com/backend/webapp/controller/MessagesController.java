package com.backend.webapp.controller;

import com.backend.webapp.api.MessagesApi;
import com.backend.webapp.document.Chats;
import com.backend.webapp.mapper.RequestMapper;
import com.backend.webapp.model.Error;
import com.backend.webapp.model.Message;
import com.backend.webapp.repository.ChatsRepository;
import com.backend.webapp.service.MessageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import static com.backend.webapp.constant.ErrorConstants.INTERNAL_SERVER_ERROR;

@RestController
public class MessagesController implements MessagesApi {

    private static final Logger logger = LogManager.getLogger(MessagesController.class);

    @Autowired
    private MessageService messageService;

    @Autowired
    private ChatsRepository chatsRepository;

    @Override
    public ResponseEntity getMessages(String currentUser, String otherUser, OffsetDateTime before) {
        try {
            LocalDateTime timestamp;
            if (before != null) {
                timestamp = before.toLocalDateTime();
            } else {
                timestamp = LocalDateTime.now();
            }
            return ResponseEntity.ok(messageService.getMessagesBetweenUsers(currentUser, otherUser, timestamp));
        } catch (Exception e) {
            logger.error("Error while fetching messages {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(new Error().message(INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping(value = "/chats/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Chats> streamMessages(@RequestHeader(value = "X-REQUEST-USER", required = true) String X_REQUEST_USER,
            @RequestParam String otherUser) {
        try {
            logger.debug("Opening a flux stream of chat messages");
            return chatsRepository.findByMessageFromAndMessageTo(X_REQUEST_USER, otherUser);
        } catch (Exception e) {
            logger.error("Error opening a flux stream of messages {}", e.getMessage(), e);
            return Flux.empty();
        }
    }

    @Override
    public ResponseEntity addMessage(Message message) {
        try {
            chatsRepository.save(RequestMapper.mapAddMessageRequest(message)).subscribe();
            logger.debug("Chat Message pushed to capped collection successfully");
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error while inserting message {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

}
