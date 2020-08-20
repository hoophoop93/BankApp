package com.example.bank.bank.controllers;


import com.example.bank.bank.dao.RachunekRepository;
import com.example.bank.bank.dao.StaliOdbiorcyRepository;
import com.example.bank.bank.dao.ZleceniaStaleRepository;
import com.example.bank.bank.models.Klient;
import com.example.bank.bank.models.Rachunek;
import com.example.bank.bank.models.StaliOdbiorcy;
import com.example.bank.bank.models.ZlecenieStale;
import com.example.bank.bank.services.AktualnyKlientService;
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
public class UsuwanieStalegoZleceniaController {

    @Inject
    AktualnyKlientService aktualnyKlientService;

    @Autowired
    RachunekRepository rachunekRepository;

    Klient klient;
    Rachunek rachunek;

    @Autowired
    ZleceniaStaleRepository zleceniaStaleRepository;


    @RequestMapping(value = "/zleceniaStale", method = RequestMethod.GET)
    public ModelAndView staliOdbiorcyGet(@ModelAttribute("zlecenieStale") ZlecenieStale zlecenieStale, Model model) {

        ModelAndView modelAndView = new ModelAndView();

        if (aktualnyKlientService.isAuthenticated()) {
            modelAndView.setViewName("/authorised/zleceniaStale");
            System.out.println("klient z home " + aktualnyKlientService.getKlient().getImie());
            System.out.println("Jest zalogowany - strona home");

        } else {
            System.out.println("Przekierowanie na logowanie");
            modelAndView.setViewName("redirect:/login");
        }
        klient = aktualnyKlientService.getKlient();
        rachunek = rachunekRepository.findByRachunekKlienta(klient);

        List<ZlecenieStale> zlecenieStalesList = zleceniaStaleRepository.findByklientZleceniaStale(klient);



        model.addAttribute("klient", klient);
        model.addAttribute("rachunek", rachunek);
        model.addAttribute("zlecenieStalesList", zlecenieStalesList);

        System.out.println("get od transferu");

        return new ModelAndView("/authorised/zleceniaStale");
    }

    @RequestMapping(value = "/zleceniaStale", method = RequestMethod.POST)
    public ModelAndView staliOdbiorcyPost(@ModelAttribute("zlecenieStale") ZlecenieStale zlecenieStale,
                                          final BindingResult result, Model model,
                                          final RedirectAttributes redirectAttributes) {


        List<ZlecenieStale> zlecenieStalesList = zleceniaStaleRepository.findByklientZleceniaStale(klient);


        if (zlecenieStalesList.isEmpty()) {

            List<String> infoMessages = new ArrayList<>();
            infoMessages.add("Brak zleceń do usunięcia.");

            redirectAttributes.addFlashAttribute("infos", infoMessages);

        }else {
            zleceniaStaleRepository.deleteById(zlecenieStale.getIdZlecenia());

            List<String> infoMessages = new ArrayList<>();
            infoMessages.add("Wybrane zlecenie stałe  zostało uzunięte.");

            redirectAttributes.addFlashAttribute("infos", infoMessages);

        }
        return new ModelAndView("redirect:/user");
    }
}
