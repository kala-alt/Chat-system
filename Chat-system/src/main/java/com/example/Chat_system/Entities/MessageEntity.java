package com.example.Chat_system.Entities;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;



@Entity
@Table(name = "\"Messages\"")
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
    private int id;

    private String description;
    private int authorid, recipientid;
    private LocalDate date;
    private LocalTime time;


    public MessageEntity(){
    
    }


    public MessageEntity(String description, int authorid, int recipientid, LocalDate date, LocalTime time) {
        this.description = description;
        this.authorid = authorid;
        this.recipientid = recipientid;
        this.date = date;
        this.time = time;
    }


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public int getAuthorid() {
        return authorid;
    }
    public void setAuthorid(int authorid) {
        this.authorid = authorid;
    }
    public int getRecipientid() {
        return recipientid;
    }
    public void setRecipientId(int recipientid) {
        this.recipientid = recipientid;
    }
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public LocalTime getTime() {
        return time;
    }
    public void setTime(LocalTime time) {
        this.time = time;
    }

}
