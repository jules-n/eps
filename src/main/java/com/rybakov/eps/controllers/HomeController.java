package com.rybakov.eps.controllers;

import com.rybakov.eps.dao.IDAO;
import com.rybakov.eps.dao.MySQLDAO;
import com.rybakov.eps.models.Invitation;
import com.rybakov.eps.models.Participant;
import com.rybakov.eps.models.Rate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@SessionAttributes(value = "participant")
public class HomeController {

    private static Participant participant = null;
    private static IDAO mySQLDAO = new MySQLDAO();

    public static IDAO getMySQLDAO() {
        return mySQLDAO;
    }

    @ModelAttribute("participant")
    public static Participant getParticipant() {
        return participant;
    }

    @GetMapping("/")
    String index(Model model){
        if(this.participant!=null) {
            model.addAttribute("buttonName", "My page");
        } else {
            model.addAttribute("buttonName", "Log In");
        }
        return "index";
    }

    @GetMapping("logIn")
    String logIn(){
        if(participant!=null)
        if(this.participant.isOrganizer()) return "redirect:/organizer";
        else return "redirect:/participant";
        else return "login";
    }

    @GetMapping("/register")
    String register(Model model){
        List<Rate> list = mySQLDAO.readAll(new Rate());
        Collections.reverse(list);
        model.addAttribute("rate", list);
        return "register";
    }

    @GetMapping("/toMain")
    String toMain(){
        return "forward:/";
    }

    @PostMapping("/logIn")
    String logIn(Model model, @RequestParam String login, @RequestParam String password){
      this.participant = mySQLDAO.readParticipant(login,password);
      if(this.participant!=null){
        if(this.participant.isOrganizer()) return "redirect:/organizer";
        else return "redirect:/participant";}
      else return "login";
    }

    @GetMapping("/logOut")
    String logout(SessionStatus sessionStatus){
        sessionStatus.setComplete();
        participant = null;
        return "redirect:/";
    }

    @GetMapping("/getInvitations")
    String getInvitations(@ModelAttribute("participant") Participant participant,  Model model){
        var list = getMySQLDAO().readMyInvitation(participant.getIdParticipant()).stream().filter(
                el -> ((Invitation)el).getEvent().getDate().compareTo(Date.from(Instant.now()))!=-1
        ).collect(Collectors.toList());
        model.addAttribute("invitations",list);
        return "myInvitations";
    }

    @PostMapping("/decline")
    String decline(@ModelAttribute("participant") Participant participant, int idInvitation){
        mySQLDAO.confirm(participant.getIdParticipant(),idInvitation,false);
        return "redirect:/getInvitations";
    }
    @PostMapping("/accept")
    String accept(@ModelAttribute("participant") Participant participant, int idInvitation){
        mySQLDAO.confirm(participant.getIdParticipant(),idInvitation,true);
        return "redirect:/getInvitations";
    }

}
