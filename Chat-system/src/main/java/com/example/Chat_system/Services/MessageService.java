package com.example.Chat_system.Services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.example.Chat_system.Entities.MessageEntity;

public class MessageService {
    
 private static SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(MessageEntity.class).buildSessionFactory();

    public List<MessageEntity> getUserMessages(int authorId, int receiverId){

        Session session = factory.getCurrentSession();
        session.beginTransaction();

        List<MessageEntity> arr = session.createQuery("FROM MessageEntity WHERE authorid = :authorid AND recipientid = :recipientid ORDER BY id DESC", MessageEntity.class)
        .setParameter("authorid", authorId)
        .setParameter("recipientid", receiverId)
        .setMaxResults(10)
        .getResultList();

        session.getTransaction().commit();
        session.close();
        
        System.out.println("Message Array size = " + arr.size());

        return arr;
    }

    public void addMessage(int authorId, int receiverId, String description){
        Session session = factory.getCurrentSession();
        session.beginTransaction();

        MessageEntity messageEntity = new MessageEntity(description, authorId, receiverId, LocalDate.now(), LocalTime.now());

        session.persist(messageEntity);
        
            session.getTransaction().commit();
            session.close();
    }

}
