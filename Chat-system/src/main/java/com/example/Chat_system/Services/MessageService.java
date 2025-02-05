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

        List<MessageEntity> arr = session.createQuery("FROM MessageEntity WHERE authorid = :authorid AND recipientid = :recipientid", MessageEntity.class)
        .setParameter("authorid", authorId)
        .setParameter("recipientid", receiverId)
        .getResultList();

        session.getTransaction().commit();
        session.close();

        return arr;

    }

    public void addMessage(int authorId, int receiverId, String description){
        Session session = factory.getCurrentSession();
        session.beginTransaction();

        // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        MessageEntity messageEntity = new MessageEntity(description, authorId, receiverId, LocalDate.now(), LocalTime.now());

        session.persist(messageEntity);

        // session.createQuery("INSERT INTO MessageEntity VALUES (:authorid, :description, :date, :time, :recipientid)", MessageEntity.class)
        //     .setParameter("authorid", authorId)
        //     .setParameter("description", description)
        //     .setParameter("date", LocalDate.now().format(formatter))
        //     .setParameter("time", LocalTime.now())
        //     .setParameter("recipientid", receiverId).executeUpdate();
        
            session.getTransaction().commit();
            session.close();
    }

}
