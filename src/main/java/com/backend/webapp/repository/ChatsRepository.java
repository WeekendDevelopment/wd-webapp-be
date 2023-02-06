package com.backend.webapp.repository;

import com.backend.webapp.document.Chats;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ChatsRepository extends ReactiveMongoRepository<Chats, String> {

    @Tailable
    @Query("{ $or: [{messageFrom: ?0, messageTo: ?1}, {messageFrom: ?1, messageTo: ?0}] }")
    public Flux<Chats> findByMessageFromAndMessageTo(String messageFrom, String messageTo);

}
