package com.example.Chat_system.Entities;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;




@Document(collection = "Messages")
public class Message {
    

    private String message, senderId, recipientId;
    private LocalDateTime dateTime;

    


}
