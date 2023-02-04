package com.backend.webapp.repository;

import com.backend.webapp.constant.ApplicationConstants.MessageStatus;
import com.backend.webapp.document.Chats;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ChatsRepository extends ReactiveMongoRepository<Chats, String> {

    @Tailable
    @Query("{ messageFrom: { $in: [ ?0, ?1 ] }, messageTo: { $in: [ ?0, ?1 ] } }")
    public Flux<Chats> findChatsByMessageFromAndMessageTo(String messageFrom, String messageTo);

}
