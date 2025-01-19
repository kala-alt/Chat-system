package com.example.Chat_system.Controllers;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.w3c.dom.Text;

import com.example.Chat_system.Entities.UserEntity;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.hibernate.cfg.Configuration;



@Controller
@SessionAttributes("loggedUser")
public class MainController {

    private static SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(UserEntity.class).buildSessionFactory();

    @PostMapping("/logged")
    public String checkLogin(Model model, @ModelAttribute UserEntity user){

        Session session = factory.getCurrentSession();
        session.beginTransaction();

        System.err.println("Email: " + user.getEmail());

        UserEntity findUser = session.createQuery("FROM UserEntity WHERE email = :email AND password = :password", UserEntity.class)
                    .setParameter("email", user.getEmail())
                    .setParameter("password", user.getPassword())
                    .uniqueResult();

        session.getTransaction().commit();
        session.close();


        if (findUser == null)
            model.addAttribute("loginFail", true);
        else{
            model.addAttribute("loginFail", false);
            model.addAttribute("loggedUser", findUser);
        }
        return "Login.html";
    }
    
    
    
    
    // List<UserEntity> arr = session.createQuery("FROM UserEntity WHERE username = :username",  UserEntity.class)
    // .setParameter("username", "")

    @GetMapping("/")
    public String showHomePage(Model model) {
       
        Session session = factory.getCurrentSession();
        session.beginTransaction();

        List<UserEntity> arr = session.createQuery("FROM UserEntity",  UserEntity.class)
        .getResultList();

        if (arr.isEmpty())
            model.addAttribute("noUsers", true);
        
        model.addAttribute("arr", arr);
       
        session.getTransaction().commit();
        session.close();

       return "HomePage.html";



       
        // UserEntity user = (UserEntity) model.getAttribute("loggedUser");

        // if (user == null){
        //     // model.addAttribute("user", new UserEntity())
        //     // TODO go to the home page (dashboard) 

        //     System.out.println("User is NULL!");
        //     return "Login.html";
        // }else{
        //     System.out.println("User is NOT NULL!");
        //     return "SignUp.html";
        // }
    }

    

    @ResponseBody
    private void searchBar(Model model, @ModelAttribute String searchUser){

        Session session = factory.getCurrentSession();
        session.beginTransaction();

        List<UserEntity> findUsers = session.createQuery("FROM Users WHERE username = :username", UserEntity.class)
            .setParameter("username", searchUser)
            .getResultList();

        if (findUsers.isEmpty()) 
            model.addAttribute("notFound", true);


        model.addAttribute("arr", findUsers);
        
        session.getTransaction().commit();
        session.close();
    }


    
    @PostMapping("/registered")
    public String postRegistered(Model model, @ModelAttribute UserEntity user) {

        System.out.println("user username= " + user.getUsername());
        //TODO Complete this method!!!
        Session session =  factory.getCurrentSession();

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