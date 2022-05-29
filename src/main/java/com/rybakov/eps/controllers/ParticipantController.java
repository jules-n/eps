package com.rybakov.eps.controllers;

import com.rybakov.eps.models.Participant;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;


@Controller
@SessionAttributes(value = "participant")
public class ParticipantController {
    private HomeController homeController;

    private Participant participant;
    @ModelAttribute("participant")
    public Participant getParticipant() {
        return participant;
    }

    @GetMapping("/participant")
    String test(Model model){
        if(homeController.getParticipant()!=null)
        {this.participant = homeController.getParticipant();
            model.addAttribute("participant",participant);
            return "participant";}
        else return "login";
    }
    
}
