package com.example.Chat_system.Services;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.example.Chat_system.Entities.UserEntity;

public class UserService {

    private static SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(UserEntity.class).buildSessionFactory();

    public UserEntity findUserViaUsername(String username){

        Session session = factory.getCurrentSession();
        session.beginTransaction();

        UserEntity user = session.createQuery("FROM UserEntity WHERE username = :username", UserEntity.class)
            .setParameter("username", username)
            .uniqueResult();


        session.getTransaction().commit();
        session.close();


        return user;
    } 

    
}
