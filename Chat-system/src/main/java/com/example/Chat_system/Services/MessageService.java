package com.example.Chat_system.Services;

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

        List<MessageEntity> arr = session.createQuery("FROM MessageEntity WHERE authorid = :authorid AND recipientid = :recipientid", MessageEntity.class)
        .setParameter("authorid", authorId)
        .setParameter("recipientid", receiverId)
        .getResultList();

        session.getTransaction().commit();
        session.close();

        return arr;

    }


}
