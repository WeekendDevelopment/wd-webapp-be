package com.backend.webapp.repository;

import com.backend.webapp.document.Messages;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public interface MessagesRepository extends MongoRepository<Messages, String> {

    @Aggregation(pipeline = { "{$sort: {timestamp: 1}}", "{$limit: 50}" })
    @Query("{ 'messageFrom': { $in: [ ?0, ?1 ] }, 'messageTo': { $in: [ ?0, ?1 ] }, 'timestamp': { $lt: ?2 } }")
    List<Messages> findByMessageFromAndMessageTo(String messageFrom, String messageTo, LocalDateTime timestamp);

}
