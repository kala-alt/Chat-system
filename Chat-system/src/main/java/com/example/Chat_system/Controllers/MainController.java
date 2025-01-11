package com.example.Chat_system.Controllers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.example.Chat_system.Entities.UserEntity;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;



@Controller
@SessionAttributes("loggedUser")
public class MainController {

    private static SessionFactory factory = 
    new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(UserEntity.class).buildSessionFactory();


    @GetMapping("/")
    public String showHomePage(Model model) {
        UserEntity user = (UserEntity) model.getAttribute("loggedUser");

        if (user == null){
            // model.addAttribute("login", new UserEntity())

            return "Login.html";
        }else{
            // model.addAttribute("registerUser", new UserEntity());
            // TODO go to the home page (dashboard) 
            return "Register.html";
        }
    }

    @PostMapping("/registered")
    public String postRegistered(Model model, @ModelAttribute UserEntity user) {
        
        //TODO Complete this method!!!
        Session session = factory.getCurrentSession();
        session.persist(user);

        model.addAttribute("user", new UserEntity());
        return "Login.html";
    }
    
    



    @ModelAttribute
    private void justrun(Model model){
        try{
            UserEntity loggedUser = (UserEntity) model.getAttribute("loggedUser");
            if (loggedUser != null)
                System.err.println("There is someone who already logged in!");
            else{
                System.out.println("No logged user!");
            }
                showSignUp(model);
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }

    @GetMapping("/login")
    public String showLogin( Model model, HttpSession httpSession) {
        model.addAttribute("user", new UserEntity());
        return "Login.html";
    }


    @GetMapping("/signUp")
    public String showSignUp(Model model) {
        model.addAttribute("user", new UserEntity());
        return "SignUp.html";
    }

    
    
    
    @GetMapping("/time")
    public String showDate() throws SQLException{
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        LocalDateTime date = LocalDateTime.now();
        formatter.format(date);
        System.out.println(formatter.format(date));

        return "index.html";
    }
}