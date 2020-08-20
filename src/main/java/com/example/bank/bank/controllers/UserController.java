package com.example.bank.bank.controllers;

import com.example.bank.bank.dao.*;
import com.example.bank.bank.models.*;
import com.example.bank.bank.services.AktualnyKlientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    @Inject
    AktualnyKlientService aktualnyKlientService;

    @Autowired
    KlientRepository klientRepository;

    @Autowired
    RachunekRepository rachunekRepository;

    @Autowired
    TransakcjeRepository transakcjeRepository;

    @Autowired
    TypOperacjiRepository typOperacjiRepository;

    @Autowired
    TransakcjeDao transakcjeDao;
    @Autowired
    DatyRepository datyRepository;

    @RequestMapping(value = "/user",method = RequestMethod.GET)
    public ModelAndView userPanel(@ModelAttribute("infos") final ArrayList<String> infos, Model model
    ,final BindingResult bindingResult){
        ModelAndView modelAndView = new ModelAndView();

        if(aktualnyKlientService.isAuthenticated()) {
            modelAndView.setViewName("/authorised/user");
            System.out.println("klient z home "+ aktualnyKlientService.getKlient().getImie());
            System.out.println("Jest zalogowany - strona home");

        } else {
            System.out.println("Przekierowanie na logowanie");
            modelAndView.setViewName("redirect:/login");
        }
        if ((infos != null) && (!bindingResult.hasErrors())) {
            modelAndView.addObject("infos", infos);
        }

        Klient klient = aktualnyKlientService.getKlient();
        Rachunek rachunek = rachunekRepository.findByRachunekKlienta(klient);
        System.out.println("Aktualny rachunek"+ rachunek.getNumerKonta());
        List<Transakcje> transakcjeList = transakcjeRepository.findByrachunekTransakcje(rachunek);
        List<Transakcje> transakcjeListOtrzymane = transakcjeRepository.findByrachunekTransakcje2(rachunek);

        for(int i = 0; i < transakcjeList.size(); i++) {
            if (transakcjeList.get(i).getTytulPrzelewu().trim().equals("brak")) {
                System.out.println("wchodzi tu czy nie ");
                transakcjeList.remove(i);
                i--;
            }

        }
        for(int i = 0; i < transakcjeListOtrzymane.size(); i++) {
            if (transakcjeListOtrzymane.get(i).getTytulPrzelewu().trim().equals("brak2")) {
                System.out.println("wchodzi tu czy nie ");
                transakcjeListOtrzymane.remove(i);
                i--;
            }

        }

        List<Daty> datyList = new ArrayList<>();
        List<Klient> klientList = new ArrayList<>();
        List<TypOperacji> typOperacjiList = new ArrayList<>();

        List<Daty> datyList2 = new ArrayList<>();
        List<Klient> klientList2 = new ArrayList<>();
        List<TypOperacji> typOperacjiList2 = new ArrayList<>();

        for(int i =0; i< transakcjeList.size(); i++) {
            Transakcje t = transakcjeList.get(i);
           datyList.add(t.getDatyTransakcje());
           typOperacjiList.add(t.getTypOperacjiTransakcje());
           klientList.add(t.getRachunekTransakcje2().getRachunekKlienta());

        }
        for(int i =0; i< transakcjeListOtrzymane.size(); i++) {
            Transakcje t2 = transakcjeListOtrzymane.get(i);
            datyList2.add(t2.getDatyTransakcje());
            typOperacjiList2.add(t2.getTypOperacjiTransakcje());
            klientList2.add(t2.getRachunekTransakcje2().getRachunekKlienta());

        }

        System.out.println("Kwota na koncie klienta "+rachunek.getKwota());
        model.addAttribute("klient",klient);
        model.addAttribute("rachunek",rachunek);
        model.addAttribute("listaTransakcji",transakcjeList);
        model.addAttribute("transakcjeListOtrzymane",transakcjeListOtrzymane);
        model.addAttribute("datyList",datyList);
        model.addAttribute("typOperacjiList",typOperacjiList);
        model.addAttribute("datyList2",datyList2);
        model.addAttribute("typOperacjiList2",typOperacjiList2);
        model.addAttribute("klientList2",klientList2);
        model.addAttribute("klientList",klientList);



        return modelAndView;

    }


}
