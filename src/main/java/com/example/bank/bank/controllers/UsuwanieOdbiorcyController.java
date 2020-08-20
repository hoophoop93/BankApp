package com.example.bank.bank.controllers;

import com.example.bank.bank.dao.*;
import com.example.bank.bank.models.Klient;
import com.example.bank.bank.models.PrzelewViewModel;
import com.example.bank.bank.models.Rachunek;
import com.example.bank.bank.models.StaliOdbiorcy;
import com.example.bank.bank.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UsuwanieOdbiorcyController {

    @Inject
    AktualnyKlientService aktualnyKlientService;

    @Autowired
    RachunekRepository rachunekRepository;

    @Autowired
    StaliOdbiorcyRepository staliOdbiorcyRepository;

    Klient klient;
    Rachunek rachunek;

    @RequestMapping(value = "/staliOdbiorcy", method = RequestMethod.GET)
    public ModelAndView staliOdbiorcyGet(@ModelAttribute("staliOdbiorcy") StaliOdbiorcy staliOdbiorcy, Model model) {

        ModelAndView modelAndView = new ModelAndView();

        if (aktualnyKlientService.isAuthenticated()) {
            modelAndView.setViewName("/authorised/staliOdbiorcy");
            System.out.println("klient z home " + aktualnyKlientService.getKlient().getImie());
            System.out.println("Jest zalogowany - strona home");

        } else {
            System.out.println("Przekierowanie na logowanie");
            modelAndView.setViewName("redirect:/login");
        }
        klient = aktualnyKlientService.getKlient();
        rachunek = rachunekRepository.findByRachunekKlienta(klient);

        List<StaliOdbiorcy> staliOdbiorcyList = staliOdbiorcyRepository.findByklientPierwszy(klient);
        for (StaliOdbiorcy staliOdbiorcy2 : staliOdbiorcyList) {
            System.out.println(staliOdbiorcyList.size() + "pokaze coś czy nie " + staliOdbiorcy2.getKlientDrugi().getImie());
        }


        model.addAttribute("klient", klient);
        model.addAttribute("rachunek", rachunek);
        model.addAttribute("staliOdbiorcyList", staliOdbiorcyList);

        System.out.println("get od transferu");

        return new ModelAndView("/authorised/staliOdbiorcy");
    }

    @RequestMapping(value = "/staliOdbiorcy", method = RequestMethod.POST)
    public ModelAndView staliOdbiorcyPost(@ModelAttribute("staliOdbiorcy") StaliOdbiorcy staliOdbiorcy,
                                          final BindingResult result, Model model,
                                          final RedirectAttributes redirectAttributes) {


        List<StaliOdbiorcy> staliOdbiorcyList = staliOdbiorcyRepository.findByklientPierwszy(klient);

        if (staliOdbiorcyList.isEmpty()) {

            List<String> infoMessages = new ArrayList<>();
            infoMessages.add("Brak odbiorców do usunięcia.");

            redirectAttributes.addFlashAttribute("infos", infoMessages);

        }else {
            staliOdbiorcyRepository.deleteById(staliOdbiorcy.getIdOdbiorcy());

            List<String> infoMessages = new ArrayList<>();
            infoMessages.add("Wybrany odbiorca został uzunięty.");

            redirectAttributes.addFlashAttribute("infos", infoMessages);

        }
        return new ModelAndView("redirect:/user");
    }
}
