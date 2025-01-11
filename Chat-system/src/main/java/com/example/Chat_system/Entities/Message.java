package com.example.Chat_system.Entities;

import java.sql.Date;
import java.sql.Time;

import org.springframework.data.mongodb.core.mapping.Document;




@Document(collection = "Messages")
public class Message {
    
    private String message, senderId, recipientId;
    private Date date;
    private Time time;

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getSenderId() {
        return senderId;
    }
    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
    public String getRecipientId() {
        return recipientId;
    }
    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public Time getTime() {
        return time;
    }
    public void setTime(Time time) {
        this.time = time;
    }



    


}
