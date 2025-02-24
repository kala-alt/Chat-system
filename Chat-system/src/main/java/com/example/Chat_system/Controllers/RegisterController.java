package com.example.Chat_system.Controllers;

import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.Chat_system.Entities.UserEntity;


@Controller
public class RegisterController {
    

    @GetMapping("/signUp")
    public String showSignUp(Model model) {
        model.addAttribute("user", new UserEntity());
        return "SignUp.html";
    }


    @PostMapping("/registered")
    public String postRegistered(Model model, @ModelAttribute UserEntity user) {

        System.out.println("user username= " + user.getUsername());
        //TODO Complete this method!!!
        Session session =  MainController.factory.getCurrentSession();

        session.beginTransaction();

        List<String> usernames = session.createQuery("SELECT username FROM UserEntity", String.class)
            .getResultList();

        if (usernames.contains(user.getUsername())){
            
            session.getTransaction().commit();
            session.close();
            model.addAttribute("userExists", true);

            return "SignUp.html";
        }

        session.persist(user);

        session.getTransaction().commit();
        session.close();

        model.addAttribute("user", new UserEntity());
        return "Login.html";
    }
}
