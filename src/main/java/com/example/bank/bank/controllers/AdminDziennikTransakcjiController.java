package com.example.bank.bank.controllers;

import com.example.bank.bank.dao.KlientRepository;
import com.example.bank.bank.dao.RachunekRepository;
import com.example.bank.bank.dao.TransakcjeRepository;
import com.example.bank.bank.models.*;
import com.example.bank.bank.services.AktualnyAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminDziennikTransakcjiController {

    @Inject
    AktualnyAdminService aktualnyAdminService;

    Administrator administrator;

    @Autowired
    KlientRepository klientRepository;

    @Autowired
    RachunekRepository rachunekRepository;
    @Autowired
    TransakcjeRepository transakcjeRepository;

    @RequestMapping(value = "/adminTransakcje", method = RequestMethod.GET)
    public ModelAndView adminFunduszeGet(Model model) {

        ModelAndView modelAndView = new ModelAndView();
        if (aktualnyAdminService.isAuthenticated()) {

            modelAndView.setViewName("/authorised/adminTransakcje");
            System.out.println("klient z home " + aktualnyAdminService.getAdmin().getImie());
            System.out.println("Jest zalogowany - strona home");
        } else {
            System.out.println("Przekierowanie na logowanie");
            modelAndView.setViewName("redirect:/login");
        }

        List<Transakcje> transakcjeList = (List<Transakcje>) transakcjeRepository.findAll();

        List<Daty> datyList = new ArrayList<>();
        List<Klient> klientList = new ArrayList<>();
        List<TypOperacji> typOperacjiList = new ArrayList<>();
        List<Klient> klientList2 = new ArrayList<>();

        for(int i =0; i< transakcjeList.size(); i++) {
            Transakcje t = transakcjeList.get(i);
            datyList.add(t.getDatyTransakcje());
            typOperacjiList.add(t.getTypOperacjiTransakcje());
            klientList.add(t.getRachunekTransakcje().getRachunekKlienta());
            klientList2.add(t.getRachunekTransakcje2().getRachunekKlienta());

        }
        administrator = aktualnyAdminService.getAdmin();


        model.addAttribute("administrator", administrator);
        model.addAttribute("klientList", klientList);
        model.addAttribute("klientList2", klientList2);
        model.addAttribute("datyList", datyList);
        model.addAttribute("typOperacjiList", typOperacjiList);
        model.addAttribute("transakcjeList", transakcjeList);

        return new ModelAndView("/authorised/adminTransakcje");
    }
}
