package com.example.bank.bank.controllers;


import com.example.bank.bank.dao.KlientRepository;
import com.example.bank.bank.models.Klient;
import com.example.bank.bank.services.AktualnyAdminService;
import com.example.bank.bank.services.AktualnyKlientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import java.util.ArrayList;


@Controller
public class HomeController {

    @Inject
    AktualnyKlientService aktualnyKlientService;

    @Inject
    AktualnyAdminService aktualnyAdminService;

    @RequestMapping("/")
    public ModelAndView home(){
        ModelAndView modelAndView = new ModelAndView();

        if(aktualnyKlientService.isAuthenticated()) {
            modelAndView.setViewName("redirect:/user");
            System.out.println("klient z home "+ aktualnyKlientService.getKlient().getImie());
            System.out.println("Jest zalogowany - strona home");

        }
       else if(aktualnyAdminService.isAuthenticated()) {
            modelAndView.setViewName("redirect:/admin");
        }
        else {
            modelAndView.setViewName("redirect:/login");
        }



    return modelAndView;

    }
}
