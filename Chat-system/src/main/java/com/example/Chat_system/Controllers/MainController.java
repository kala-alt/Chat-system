package com.example.Chat_system.Controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.example.Chat_system.Entities.MessageEntity;
import com.example.Chat_system.Entities.UserEntity;
import com.example.Chat_system.Services.MessageService;
import com.example.Chat_system.Services.UserService;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.hibernate.cfg.Configuration;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@SessionAttributes({"loggedUser", "recipientUser"})
public class MainController {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageController messageController;

    @Autowired
    private RegisterController registerController;


    public static SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(UserEntity.class).buildSessionFactory();

    @GetMapping("/")
    public String showHomePage(Model model) {

       System.out.println("@GetMapping(\"/\") is now working...");
       UserEntity loggedUser = (UserEntity) model.getAttribute("loggedUser");

       if (loggedUser == null)
            return "redirect:/login";

        System.out.println("Logged user username: " + loggedUser.getUsername());

        Session session = factory.getCurrentSession();
        session.beginTransaction();

        List<UserEntity> arr = session.createQuery("FROM UserEntity",  UserEntity.class)
        .getResultList();

        for (int i=0; i<arr.size(); i++)
            if (arr.get(i).getId() == loggedUser.getId())
                arr.remove(i);
        
        if (arr.isEmpty())
            model.addAttribute("noUsers", true);
        
        model.addAttribute("arr", arr);
        model.addAttribute("searchUser", "");
        model.addAttribute("showUsername", "");
        
        model.addAttribute("sended", new ArrayList<MessageEntity>());
        model.addAttribute("received", new ArrayList<MessageEntity>());

        session.getTransaction().commit();
        session.close();

       return "HomePage.html";
    }

    
    @PostMapping("/")
    private String searchBar(Model model, @RequestParam String searchUser, @RequestParam String showUsername, HttpSession httpSession){

        Session session = factory.getCurrentSession();
        session.beginTransaction();

        System.out.println("searchUser= " + searchUser + "\t\tshow Username= " + showUsername);
        List<UserEntity> findUsers;

        if (searchUser.isBlank() == false)
            findUsers = session.createQuery("FROM UserEntity WHERE lower(username) like lower(:username)", UserEntity.class)
                .setParameter("username", "%" + searchUser + "%")
                .getResultList();
        else
            findUsers = session.createQuery("FROM UserEntity", UserEntity.class)
            .getResultList();

            for (int i=0; i<findUsers.size(); i++)
                if (findUsers.get(i).getId() == ((UserEntity) model.getAttribute("loggedUser")) .getId())
                    findUsers.remove(i);


        if (findUsers.isEmpty()) 
            model.addAttribute("notFound", true);

        model.addAttribute("arr", findUsers);
        
        session.getTransaction().commit();
        session.close();

        messageController.messageCenter(model, showUsername, httpSession);

        return "HomePage.html";
    }
    

    @ModelAttribute
    private void justrun(Model model) {
        registerController.showSignUp(model);
    }


    //Fetch methods that work with JavaScript
    @PostMapping("/setReceiver")
    @ResponseBody
    public void setReceiver(Model model, @RequestBody String username) {
        model.addAttribute("recipientUser", username);
        System.out.println("\n****************\nNew recipientUser= " + username);
    }


    @PostMapping("/chatting")
    @ResponseBody
    public void chat(Model model, @RequestParam String message) {

        if(message.isBlank())
            return;
        
        UserEntity user = (UserEntity) model.getAttribute("loggedUser");
        String reciver = (String) model.getAttribute("recipientUser");

        messageService.addMessage(user.getId(), userService.findUserViaUsername(reciver).getId(), message);   
    }


    @GetMapping("/getLoggedUserId")
    @ResponseBody
    public String getLoggedUserId(Model model) {
        return String.valueOf(((UserEntity) model.getAttribute("loggedUser")).getId());
    }

}