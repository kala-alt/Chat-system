package com.example.Chat_system.Services;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Service;

import com.example.Chat_system.Entities.UserEntity;
import com.example.Chat_system.Entities.MessageEntity;


@Service
public class MessageService {
    
 private static SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(UserEntity.class).buildSessionFactory();

    public List<MessageEntity> getUserMessages(int authorId, int receiverId){

        Session session = factory.getCurrentSession();
        session.beginTransaction();

        List<MessageEntity> arr = session.createQuery("FROM MessageEntity WHERE authorId = :authorId AND recipientId = :recipientId", MessageEntity.class)
        .setParameter("authorId", authorId)
        .setParameter("recipientId", receiverId)
        .getResultList();

        session.getTransaction().commit();
        session.close();

        return arr;

    }


}
