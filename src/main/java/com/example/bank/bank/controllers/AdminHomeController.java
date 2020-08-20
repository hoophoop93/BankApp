package com.example.bank.bank.controllers;

import com.example.bank.bank.dao.KlientDao;
import com.example.bank.bank.dao.KlientRepository;
import com.example.bank.bank.dao.RachunekRepository;
import com.example.bank.bank.models.*;
import com.example.bank.bank.services.AktualnyAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@Controller
public class AdminHomeController {
    @Inject
    AktualnyAdminService aktualnyAdminService;

    Administrator administrator;

    @Autowired
    KlientRepository klientRepository;

    @Autowired
    RachunekRepository rachunekRepository;

    @Autowired
    KlientDao klientDao;

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public ModelAndView adminGet(@ModelAttribute("infos") final ArrayList<String> infos,
                                 final BindingResult bindingResult ,Model model) {

        ModelAndView modelAndView = new ModelAndView();
        if (aktualnyAdminService.isAuthenticated()) {

            modelAndView.setViewName("/authorised/admin");
            System.out.println("klient z home " + aktualnyAdminService.getAdmin().getImie());
            System.out.println("Jest zalogowany - strona home");
        } else {
            System.out.println("Przekierowanie na logowanie");
            modelAndView.setViewName("redirect:/login");
        }

        if ((infos != null) && (!bindingResult.hasErrors())) {
            modelAndView.addObject("infos", infos);
        }

        administrator = aktualnyAdminService.getAdmin();
        List<Klient> klientList = (List<Klient>) klientRepository.findAll();

        List<Rachunek> rachunekList = (List<Rachunek>) rachunekRepository.findAll();

        model.addAttribute("administrator", administrator);
        model.addAttribute("klientList", klientList);
        model.addAttribute("rachunekList", rachunekList);

        return new ModelAndView("/authorised/admin", "model", new ResetViewModel());
    }


    @RequestMapping(value = "/admin", method = RequestMethod.POST)
    public ModelAndView adminPost(@Valid @ModelAttribute("model") ResetViewModel model,
                                      final BindingResult result) {

        ModelAndView modelAndView = new ModelAndView();
        System.out.println("Przycisk odblokowania");

        List<Klient> klientList = (List<Klient>) klientRepository.findAll();
        List<Rachunek> rachunekList = (List<Rachunek>) rachunekRepository.findAll();
        modelAndView.addObject("administrator", administrator);
        modelAndView.addObject("klientList", klientList);
        modelAndView.addObject("rachunekList", rachunekList);

        if (!result.hasErrors()) {
            if (!klientDao.zliczKlientow(model.getPesel())) {
                result.reject("error.notProjectMember", "Nie ma klienta o takim numerze pesel.");
            }
        }

        if (result.hasErrors()) {

            modelAndView.addObject("administrator", administrator);
            modelAndView.addObject("klientList", klientList);
            modelAndView.addObject("rachunekList", rachunekList);
            modelAndView.addObject(new ResetViewModel());
            modelAndView.setViewName("/authorised/admin");
            return modelAndView;
        }



        Klient klient = klientRepository.findByPesel(model.getPesel());

        String peselZBazy = model.getPesel();
        int licznik = klient.getLicznik();

        if (licznik == 0) {
            result.reject("error.zablokowanie", "Konto o numerze pesel "+peselZBazy +" zostało zablokowane.");
            System.out.println("zablokowanie");
            klient.setLicznik(3);
            klientRepository.save(klient);
        } else {
            System.out.println("odblokowanie");
            result.reject("error.odblokowanie", "Konto o numerze pesel "+peselZBazy +" zostało odblokowane.");
            klient.setLicznik(0);
            klientRepository.save(klient);
        }
    model.setPesel("");

        modelAndView.setViewName("/authorised/admin");
        return modelAndView;

    }
}

