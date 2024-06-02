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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
        LocalDateTime sentAt = LocalDateTime.now();
        List<Integer> sentAtArray = convertDateToArray(sentAt);
        Message message = new Message();
        message.setContent(chatMessage.getContent());
        message.setSender(user);
        message.setChat(chat);
        chat.getMessages().add(message);
        messageRepository.save(message);
        chatRepository.save(chat);

        chatMessage.setMessageId(messageId);
        chatMessage.setSentAt(sentAtArray);
        return chatMessage;
    }

    private List<Integer> convertDateToArray(LocalDateTime dateTime) {
        List<Integer> dateArray = new ArrayList<>();
        dateArray.add(dateTime.getYear());
        dateArray.add(dateTime.getMonthValue());
        dateArray.add(dateTime.getDayOfMonth());
        dateArray.add(dateTime.getHour());
        dateArray.add(dateTime.getMinute());
        dateArray.add(dateTime.getSecond());
        dateArray.add(dateTime.getNano() ); // Convert nanoseconds to milliseconds
        return dateArray;
    }
}
