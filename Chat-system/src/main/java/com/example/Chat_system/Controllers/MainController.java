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

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;




@Controller
public class MainController {





    @GetMapping("/login")
    public String showLogin(Model model) {
        // model.addAttribute()

        return "Login.html";
    }


    @GetMapping("/signUp")
    public String showSignUp(Model model) {
        // model.addAttribute()

        return "SignUp.html";
    }
    
    
    @GetMapping("/time")
    public String showDate() throws SQLException{
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        LocalDateTime date = LocalDateTime.now();
        formatter.format(date);
        System.out.println(formatter.format(date));

        Connection connection = PostgreSQLJDBC.getConnection();
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM public.\"Users\";");


        System.out.println("\n*********************");
        while (result.next()) {
            System.out.println("id: " + result.getInt("id"));
            System.out.println("username: " + result.getString("Username"));
            System.out.println("password: " + result.getString("Password"));
            System.out.println("email: " + result.getString("Email"));
            System.out.println();
        }

        return "index.html";
    }
}