package com.example.Chat_system.Controllers;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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

    private UserService userService = new UserService();
    private MessageService messageService = new MessageService();
    

    private static SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(UserEntity.class).buildSessionFactory();

    @PostMapping("/logged")
    public String checkLogin(Model model, @ModelAttribute UserEntity user) throws Exception{

        Session session = factory.openSession();
        session.beginTransaction();

        System.err.println("Email: " + user.getEmail());

        UserEntity findUser = session.createQuery("FROM UserEntity WHERE username = :username AND password = :password", UserEntity.class)
                    .setParameter("username", user.getUsername())
                    .setParameter("password", user.getPassword())
                    .uniqueResult();

        session.getTransaction().commit();
        session.close();

        if (findUser == null){
            model.addAttribute("loginFail", true);
            return "Login.html";
        }
        else{
            model.addAttribute("loginFail", false);
            model.addAttribute("loggedUser", findUser);

            return "redirect:/";
        }
    }

    

    
    @GetMapping("/")
    public String showHomePage(Model model) {
       UserEntity loggedUser = (UserEntity) model.getAttribute("loggedUser");

       if (loggedUser == null)
            return "redirect:/login";

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



    private void messageCenter(Model model, String showUsername){
        if (showUsername.isBlank() == false){
            model.addAttribute("showUsernameData", showUsername);
            UserEntity user = (UserEntity) userService.findUserViaUsername(showUsername);

            UserEntity loggedUser = (UserEntity) model.getAttribute("loggedUser");
        
            if (user != null){

                List<MessageEntity> arr =new ArrayList<>();

                arr = messageService.getUserMessages(loggedUser.getId(), user.getId());
                
                arr.addAll(messageService.getUserMessages(user.getId(), loggedUser.getId()));
                
                arr.sort((Comparator.comparing((MessageEntity m) -> m.getDate())
                           .thenComparing(m -> m.getTime())));

                model.addAttribute("messages", arr);
                model.addAttribute("loggedUserId", loggedUser.getId());

                System.out.println("\n\n*************Debug print**************** Logged user id= " + loggedUser.getId());
                for (MessageEntity m : arr)
                    System.out.println(m.getDate() + "\ttime:" + m.getTime() + "\tAuthorId: " + m.getAuthorid());

                System.out.println("-------------------------------------\n");

            }
        }
    }

    @GetMapping("/getUserMessagesViaUsername")
    @ResponseBody
    public List<MessageEntity> getUserMessagesViaUsername(Model model, @RequestParam String receiverUsername){
        System.out.println("getUserMessagesViaUsername() is running... " + receiverUsername);
        UserEntity loggedUser = (UserEntity) model.getAttribute("loggedUser");
        List<MessageEntity> arr = messageService.getUserMessages(loggedUser.getId(), userService.findUserViaUsername(receiverUsername).getId());
        arr.addAll(messageService.getUserMessages(userService.findUserViaUsername(receiverUsername).getId(), loggedUser.getId()));
        arr.sort(Comparator.comparing((MessageEntity m ) -> m.getDate()).thenComparing(m -> m.getTime()));

        return arr;

    }


    @GetMapping("/getUserMessages")
    private List<MessageEntity> getUserMessages(Model model, int receiverId){
        System.out.println("getUserMessages() is running...");
        UserEntity loggedUser = (UserEntity) model.getAttribute("loggedUser");
        List<MessageEntity> arr = messageService.getUserMessages(loggedUser.getId(), receiverId);
        arr.sort(Comparator.comparing((MessageEntity m) -> m.getDate()).thenComparing(m -> m.getTime()));

        return arr;
    }

    

    @PostMapping("/")
    private String searchBar(Model model, @RequestParam String searchUser, @RequestParam String showUsername){

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

        messageCenter(model, showUsername);

        // model.addAttribute("message", "This is Test Message!");
        return "HomePage.html";
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

        System.out.println("\n\n*********************\n\n/chatting is working!!!!");

        UserEntity user = (UserEntity) model.getAttribute("loggedUser");
        String reciver = (String) model.getAttribute("recipientUser");

        messageService.addMessage(user.getId(), userService.findUserViaUsername(reciver).getId(), message);   
    }


    @GetMapping("/getLoggedUserId")
    @ResponseBody
    public String getLoggedUserId(Model model) {
        System.out.println("getLoggedUserId method is running.....");
        return String.valueOf(((UserEntity) model.getAttribute("loggedUser")).getId());
    }
    
}