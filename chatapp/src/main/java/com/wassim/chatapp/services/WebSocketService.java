package com.wassim.chatapp.services;

import com.wassim.chatapp.dto.ChatMessage;
import com.wassim.chatapp.entities.Chat;
import com.wassim.chatapp.entities.Message;
import com.wassim.chatapp.entities.User;
import com.wassim.chatapp.repositories.ChatRepository;
import com.wassim.chatapp.repositories.MessageRepository;
import com.wassim.chatapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WebSocketService {
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public ChatMessage sendMessage(String chatId,String userId, ChatMessage chatMessage){
        Chat chat = chatRepository.findById(chatId).get();

        User user = userRepository.findById(userId).get();
        String messageId = UUID.randomUUID().toString();
        Message message = new Message();
        message.setContent(chatMessage.getContent());
        message.setSender(user);
        message.setChat(chat);
        chat.getMessages().add(message);
        messageRepository.save(message);
        chatRepository.save(chat);

        chatMessage.setMessageId(messageId);
        return chatMessage;
    }
}
