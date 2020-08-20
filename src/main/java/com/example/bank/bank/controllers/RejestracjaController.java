package com.example.bank.bank.controllers;

import com.example.bank.bank.Utils.Szyfrowanie;
import com.example.bank.bank.dao.KlientDao;
import com.example.bank.bank.dao.KlientRepository;
import com.example.bank.bank.dao.RachunekRepository;
import com.example.bank.bank.models.Klient;
import com.example.bank.bank.models.Rachunek;
import com.example.bank.bank.models.RejestracjaViewModel;
import com.example.bank.bank.services.NumerKontaService;
import com.example.bank.bank.services.RejestracjaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Controller
public class RejestracjaController {

    @Autowired
    RejestracjaService rejestracjaService;

    @Autowired
    KlientRepository klientRepository;

    @Autowired
    RachunekRepository rachunekRepository;

    @Autowired
    KlientDao klientDao;
    @Autowired
    NumerKontaService numerKontaService;


    @RequestMapping(value = "/register", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView register(){


        return new ModelAndView("/unauthorised/register", "model", new RejestracjaViewModel());
    }
    @RequestMapping(value ="/register",method = RequestMethod.POST)
    public ModelAndView registerPost(@Valid @ModelAttribute ("model")RejestracjaViewModel model, final BindingResult result,
                                     final RedirectAttributes redirectAttributes){



        String hashHaslo = Szyfrowanie.zaszyfrujHaslo(model.getHaslo());

        if (!result.hasErrors()) {
            if (klientDao.zliczKlientow(model.getPesel())) {
                result.reject("error.notProjectMember", "Taki pesel jest już w bazie.");
            }
        }
        if (result.hasErrors()) {
            return new ModelAndView("/unauthorised/register","model",model);
        }


        model.setHaslo(hashHaslo);
        rejestracjaService.registration(model);
        Klient klient = klientRepository.findByPesel(model.getPesel());

        Rachunek rachunek = new Rachunek();
        rachunek.setNumerKonta(numerKontaService.numerKonta());
        rachunek.setKwota((double) 0);
        rachunek.setRachunekKlienta(klient);
        rachunekRepository.save(rachunek);


        List<String> infoMessages = new ArrayList<>();
        infoMessages.add("Rejestracja powiodła się! Teraz możesz sie zalogować.");

        redirectAttributes.addFlashAttribute("infos", infoMessages);

        return new ModelAndView("redirect:/login");

    }


}
