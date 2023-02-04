package com.backend.webapp.scheduler;

import com.backend.webapp.document.Chats;
import com.backend.webapp.document.Messages;
import com.backend.webapp.repository.ChatsRepository;
import com.backend.webapp.repository.MessagesRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

public class ChatsStagingScheduler {

    private static final Logger logger = LogManager.getLogger(ChatsStagingScheduler.class);

    @Autowired
    private ChatsRepository chatsRepository;

    @Autowired
    private MessagesRepository messagesRepository;

    @Scheduled(cron = "*/10 * * ? * * *") // every 10 seconds
    public void pushChatsToMessagesCollection() {
        try {
            chatsRepository.findAll().toStream().forEach((chat) -> {
                messagesRepository.save(this.mapChatToMessage(chat));
            });
        } catch (Exception e) {
            logger.debug("Exception occured in pushChatsToMessagesCollection()", e);
        }
    }

    private Messages mapChatToMessage(Chats chat) {
        Messages message = new Messages();
        message.setId(chat.getId());
        message.setMessageFrom(chat.getMessageFrom());
        message.setMessageTo(chat.getMessageTo());
        message.setMessage(chat.getMessage());
        message.setMessageStatus(chat.getMessageStatus());
        message.setTimestamp(chat.getTimestamp());
        return message;
    }

}
