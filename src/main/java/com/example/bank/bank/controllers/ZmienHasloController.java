package com.example.bank.bank.controllers;



import com.example.bank.bank.Utils.Szyfrowanie;
import com.example.bank.bank.dao.KlientRepository;
import com.example.bank.bank.dao.RachunekRepository;
import com.example.bank.bank.models.Klient;
import com.example.bank.bank.models.Rachunek;
import com.example.bank.bank.models.ZmienHasloViewModel;
import com.example.bank.bank.services.AktualnyKlientService;
import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
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


@Controller
public class ZmienHasloController {

    @Inject
    AktualnyKlientService aktualnyKlientService;

    @Autowired
    RachunekRepository rachunekRepository;

    @Autowired
    KlientRepository klientRepository;

    @RequestMapping(value = "/settings",method = RequestMethod.GET)
    public ModelAndView settings(Model model){
        ModelAndView modelAndView = new ModelAndView();

        Klient klient = aktualnyKlientService.getKlient();
        Rachunek rachunek = rachunekRepository.findByRachunekKlienta(klient);
        model.addAttribute("klient", klient);
        model.addAttribute("rachunek", rachunek);



        return new ModelAndView("/authorised/settings", "model", new ZmienHasloViewModel());

    }

    @RequestMapping(value="/settings",method = RequestMethod.POST)
    public ModelAndView settingsPost(@Valid @ModelAttribute("model")ZmienHasloViewModel model, final BindingResult result,
                                     final RedirectAttributes redirectAttributes) throws MessagingException, javax.mail.MessagingException {

        ModelAndView modelAndView = new ModelAndView();

        Klient klient = aktualnyKlientService.getKlient();
        Rachunek rachunek = rachunekRepository.findByRachunekKlienta(klient);

        if (!result.hasErrors()) {
            if (klient != null) {
                if (!Szyfrowanie.sprawdzPoporawnoscHasla(model.getHasloStare(), klient.getHaslo())) {
                    result.reject("error.loginError", "Aktualne hasło się nie zgadza.");
                    System.out.println("hasała sa inne");
                } else if(!model.getHasloNowe().trim().equals(model.getHasloNowe2().trim())){
                    System.out.println("Nowe hasła nie są takie same");
                    result.reject("error.loginError", "Nowe hasła nie są takie same");
                }
                else{
                    System.out.println("Nowe hasła są takie same");
                    String hashHaslo = Szyfrowanie.zaszyfrujHaslo(model.getHasloNowe().trim());
                    klient.setHaslo(hashHaslo);
                    klientRepository.save(klient);
                    result.reject("error.loginError", "Hasło zostało zmienione");
                }
            }
        }

        if (result.hasErrors()) {
            modelAndView.addObject("klient", klient);
            modelAndView.addObject("rachunek",rachunek);
            modelAndView.addObject(new ZmienHasloController());
            modelAndView.setViewName("/authorised/settings");
            return modelAndView;
        }



        return new ModelAndView("redirect:/user");
    }

}