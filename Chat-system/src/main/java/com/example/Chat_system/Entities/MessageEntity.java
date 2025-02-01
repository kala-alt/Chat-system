package com.example.Chat_system.Entities;

import java.sql.Date;
import java.sql.Time;

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
    private Date date;
    private Time time;

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
