package com.rybakov.eps.controllers;

import com.rybakov.eps.models.Participant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistrationController {

    private boolean isPremium = false;
    private HomeController homeController;
    @PostMapping("/register")
    String createParticipant(@RequestParam String name, @RequestParam String surname, @RequestParam Long phone,
                             @RequestParam String login, @RequestParam String password){
        if(homeController.getMySQLDAO().create(new Participant(name,surname,isPremium,phone,login,password))){
        return "login";}
        else return "register";
    }

    @PostMapping ("/premium")
    String setPremium(@RequestParam String name, @RequestParam String surname, @RequestParam Long phone,
                      @RequestParam String login, @RequestParam String password){
        isPremium = true;
        if(homeController.getMySQLDAO().create(new Participant(name,surname,isPremium,phone,login,password)))
            return "login";
        else return "register";
    }
}
