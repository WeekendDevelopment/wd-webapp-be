package com.backend.webapp.repository;

import com.backend.webapp.document.Messages;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface MessagesRepository extends MongoRepository<Messages, String> {

    @Aggregation(pipeline = { "{$match: {$or: [{messageFrom: ?0, messageTo: ?1}, {messageFrom: ?1, messageTo: ?0}]}},"
            + "{$match: {timestamp: {$lt: ?2}}}," + "{$sort: {timestamp: 1}}," + "{$limit: 50}" })
    List<Messages> getMessagesBetweenUsers(String messageFrom, String messageTo, LocalDateTime timestamp);

}
