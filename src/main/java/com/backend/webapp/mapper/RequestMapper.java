package com.backend.webapp.mapper;

import com.backend.webapp.constant.ApplicationConstants.MessageStatus;
import com.backend.webapp.document.Chats;
import com.backend.webapp.document.Users;
import com.backend.webapp.model.Message;
import com.backend.webapp.model.SignupRequest;
import com.backend.webapp.model.User;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

public final class RequestMapper {

    private RequestMapper() {
        // private constructor to avoid instantiation
    }

    public static Users mapToUsers(SignupRequest signupRequest) {
        Users user = new Users();
        user.setEmail(signupRequest.getEmail());
        user.setPasswordHash(signupRequest.getPasswordHash());
        user.setRole(signupRequest.getRole());
        user.setFullName(signupRequest.getFullName());
        return user;
    }

    public static Users mapPatchUserRequest(Users user, User userUpdate) {
        if (StringUtils.isNotBlank(userUpdate.getFullName())) {
            user.setFullName(userUpdate.getFullName());
        }
        if (StringUtils.isNotBlank(userUpdate.getAbout())) {
            user.setAbout(userUpdate.getAbout());
        }
        if (StringUtils.isNotBlank(userUpdate.getCountry())) {
            user.setCountry(userUpdate.getCountry());
        }
        if (StringUtils.isNotBlank(userUpdate.getPhoneNumber())) {
            user.setPhoneNumber(userUpdate.getPhoneNumber());
        }
        if (StringUtils.isNotBlank(userUpdate.getStatus())) {
            user.setStatus(userUpdate.getStatus());
        }
        return user;
    }

    public static Chats mapAddMessageRequest(Message message) {
        Chats chat = new Chats();
        chat.setMessageFrom(message.getMessageFrom());
        chat.setMessageTo(message.getMessageTo());
        chat.setMessage(message.getMessage());
        chat.setMessageStatus(MessageStatus.valueOf(message.getMessageStatus().toString()));
        chat.setTimestamp(ZonedDateTime.parse(message.getTimestamp()).toLocalDateTime());
        return chat;
    }

}
