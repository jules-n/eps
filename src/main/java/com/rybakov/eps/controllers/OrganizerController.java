package com.rybakov.eps.controllers;

import com.rybakov.eps.models.Event;
import com.rybakov.eps.models.Participant;
import com.rybakov.eps.models.Type;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@SessionAttributes(value = "participant")
public class OrganizerController{

    private HomeController homeController;
    private Participant participant;
    @ModelAttribute("participant")
    public Participant getParticipant() {
        return participant;
    }

//@ModelAttribute("participant") Participant participant
    @GetMapping("/organizer")
    String test(Model model){
        if(homeController.getParticipant()!=null)
        {this.participant = homeController.getParticipant();
        model.addAttribute("participant",participant);
        return "organizer";}
        else return "login";
    }

    @GetMapping("/addEvent")
    String event(Model model){
        model.addAttribute("types",HomeController.getMySQLDAO().readAll(new Type()));
        return "event";
    }

    @PostMapping("/addEvent")
    String insertEvent(@ModelAttribute("participant") Participant participant, @RequestParam String name, @RequestParam String place, @RequestParam String date, @RequestParam String time, @RequestParam int type, @RequestParam(required = false) String description){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date _date = dateFormat.parse(date+" "+time+":00");
            HomeController.getMySQLDAO().create(new Event(name,place,_date,description,1,type,participant.getIdParticipant()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "organizer";
    }

    @GetMapping("/manageEvents")
    String manage(Model model, @ModelAttribute("participant") Participant participant){
        List<Event> list = HomeController.getMySQLDAO().readAllBy(new Event(),participant.getIdParticipant());
        model.addAttribute("events",list.stream().map( event ->
                {
                    event.getDate().setHours(event.getDate().getHours()-3);
                    return event;
                }
                ).collect(Collectors.toList()));
        return "manage";
    }

    @PostMapping("/archive")
    String decline(@ModelAttribute("participant") Participant participant){
        HomeController.getMySQLDAO().archiveEvents(participant.getIdParticipant());
        return "redirect:/manageEvents";
    }

    @PostMapping("/cancel")
    String cancel(@RequestParam int idEvent){
        HomeController.getMySQLDAO().update(new Event(),idEvent);
        return "redirect:/manageEvents";
    }

    @PostMapping("/editRedirect")
    String edit(Model model, @RequestParam int idEvent) {
        model.addAttribute("idEvent", idEvent);
        model.addAttribute("types",HomeController.getMySQLDAO().readAll(new Type()));
        model.addAttribute("event", ((Event)HomeController.getMySQLDAO().readByKey(new Event(), idEvent)));
        return "editEvent";
    }

    @PostMapping("/edit")
    String edit(@ModelAttribute("participant") Participant participant, @RequestParam int idEvent, @RequestParam String name, @RequestParam String place, @RequestParam String date, @RequestParam String time, @RequestParam int type, @RequestParam(required = false) String description) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date _date = dateFormat.parse(date+" "+time+":00");
            Event event = new Event(
                    idEvent, name, place,_date,description,1,type,participant.getIdParticipant()
            );
            HomeController.getMySQLDAO().updateAllEventData(event, idEvent);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "redirect:/manageEvents";
    }

    @GetMapping("/sendInvitation")
    String prepare(Model model, @ModelAttribute("participant") Participant participant){
        model.addAttribute("events",HomeController.getMySQLDAO().readAllBy(new Event(),participant.getIdParticipant()));
        model.addAttribute("participants",HomeController.getMySQLDAO().readAllBy(new Participant(), participant.getIdParticipant()));
        return "sendInvitation";
    }

    @PostMapping("/sendInvitation")
    String send(Model model, @ModelAttribute("participant") Participant participant, @RequestParam int event, @RequestParam int[] participants, @RequestParam(required = false) String text){
        HomeController.getMySQLDAO().create(participants,event,text,participant.getIdParticipant());
        return "redirect:/organizer";
    }


}
