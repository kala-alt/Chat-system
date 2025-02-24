package com.example.Chat_system.Controllers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.Chat_system.Entities.MessageEntity;
import com.example.Chat_system.Entities.UserEntity;
import com.example.Chat_system.Services.MessageService;
import com.example.Chat_system.Services.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    public void messageCenter(Model model, String showUsername, HttpSession httpSession){

        if (showUsername.isBlank() == false){
            model.addAttribute("showUsernameData", showUsername);
            UserEntity user = (UserEntity) userService.findUserViaUsername(showUsername);

            UserEntity loggedUser = (UserEntity) httpSession.getAttribute("loggedUser");
        
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


    //TODO Merge this two methods

    @GetMapping("/getUserMessagesViaUsername")
    @ResponseBody
    public List<MessageEntity> getUserMessagesViaUsername(Model model, @RequestParam String receiverUsername, HttpSession httpSession){
        System.out.println("getUserMessagesViaUsername() is running... " + receiverUsername);
        UserEntity loggedUser = (UserEntity) httpSession.getAttribute("loggedUser");
        List<MessageEntity> arr = messageService.getUserMessages(loggedUser.getId(), userService.findUserViaUsername(receiverUsername).getId());
        arr.addAll(messageService.getUserMessages(userService.findUserViaUsername(receiverUsername).getId(), loggedUser.getId()));
        arr.sort(Comparator.comparing((MessageEntity m ) -> m.getDate()).thenComparing(m -> m.getTime()));

        return arr;
    }


    @GetMapping("/getUserMessages")
    private List<MessageEntity> getUserMessages(Model model, int receiverId, HttpSession httpSession){
        System.out.println("getUserMessages() is running...");
        UserEntity loggedUser = (UserEntity) httpSession.getAttribute("loggedUser");
        List<MessageEntity> arr = messageService.getUserMessages(loggedUser.getId(), receiverId);
        arr.sort(Comparator.comparing((MessageEntity m) -> m.getDate()).thenComparing(m -> m.getTime()));

        return arr;
    }
    
}
