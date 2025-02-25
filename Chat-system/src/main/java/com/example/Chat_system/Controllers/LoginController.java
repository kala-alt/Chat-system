package com.example.Chat_system.Controllers;
import org.hibernate.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.Chat_system.Entities.UserEntity;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLogin(Model model) {
        model.addAttribute("user", new UserEntity());
        return "Login.html";
    }

    @PostMapping("/logged")
    public String checkLogin(Model model, @ModelAttribute UserEntity user, HttpSession httpSession) throws Exception{

        Session session = MainController.factory.openSession();
        session.beginTransaction();

        UserEntity findUser = session.createQuery("FROM UserEntity WHERE username = :username AND password = :password", UserEntity.class)
                    .setParameter("username", user.getUsername())
                    .setParameter("password", user.getPassword())
                    .uniqueResult();

        session.getTransaction().commit();
        session.close();

        if (findUser == null){
            model.addAttribute("loginFail", true);
            return "Login.html";
        }else{
            System.err.println("User id: " + findUser.getId());
            System.err.println("User email: " + findUser.getEmail());
            System.err.println("User username: " + findUser.getUsername());
            System.err.println("User password: " + findUser.getPassword());

            model.addAttribute("loginFail", false);
            httpSession.setAttribute("loggedUser", findUser);

            return "redirect:/";
        }
    }
}
