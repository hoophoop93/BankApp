package com.example.bank.bank.controllers;

import com.example.bank.bank.Utils.PasswordGenerator;
import com.example.bank.bank.Utils.Szyfrowanie;
import com.example.bank.bank.dao.AdminRepository;
import com.example.bank.bank.dao.KlientRepository;
import com.example.bank.bank.models.*;
import com.example.bank.bank.services.AktualnyAdminService;
import com.example.bank.bank.services.AktualnyKlientService;
import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class LoginController {

    @Autowired
    KlientRepository klientRepository;

    @Autowired
    AdminRepository adminRepository;

    @Inject
    AktualnyKlientService aktualnyKlientService;

    @Inject
    AktualnyAdminService aktualnyAdminService;

    public static String numerPesel;

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public ModelAndView login(@ModelAttribute("infos") final ArrayList<String> infos,
                              final BindingResult bindingResult){

        if (aktualnyKlientService.isAuthenticated() || aktualnyAdminService.isAuthenticated() ) {
            return new ModelAndView("redirect:/");
        }

        ModelAndView modelAndView = new ModelAndView();
        if ((infos != null) && (!bindingResult.hasErrors())) {
            modelAndView.addObject("infos", infos);
        }

        System.out.println("Przycik został klikniety get");
        return new ModelAndView("/unauthorised/login","model",new LoginViewModel());
    }

    @ResponseBody
    @RequestMapping(value ="/login",method = RequestMethod.POST)
    public ModelAndView loginPost(@Valid @ModelAttribute("model")LoginViewModel model, final BindingResult result,
                                  final RedirectAttributes redirectAttributes){


        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("model",model);

        if(model.getPesel().trim().length()>6 && model.getPesel().substring(0,6).trim().equalsIgnoreCase("admin:")) {
            System.out.println(model.getPesel().substring(6));
            Administrator admin = adminRepository.findByPesel(model.getPesel().substring(6));
            System.out.println(admin);

//           System.out.println(":"+admin.getHaslo()+":");
       //     System.out.println(":"+model.getHaslo()+":");
            if (!result.hasErrors()) {
                if (admin == null) {
                    result.reject("error.loginError", "Niepoprawny login lub hasło.");
                    System.out.println("nie ma takiego admina");
                } else if (!model.getHaslo().equalsIgnoreCase(admin.getHaslo())) {
                    result.reject("error.loginError", "Niepoprawny login lub hasło.");
                    System.out.println("hasło sie nie zgadza");
                }

            }


            if (result.hasErrors()) {
                modelAndView.setViewName("/unauthorised/login");
                return modelAndView;
            }

            aktualnyAdminService.setAdmin(admin);
            return new ModelAndView("redirect:/admin");


        }
        else {
            if (aktualnyKlientService.isAuthenticated()) {
                return new ModelAndView("redirect:/");
            }

            String zmienna = model.getHaslo();

            Klient klient = klientRepository.findByPesel(model.getPesel());
            System.out.println(klient);


            if(klient != null && klient.getLicznik()==3){
                result.reject("error.loginError", "Konto zostało zablokowane. Skontaktuj sie z obsługą banku.");
            }

            else{
                if (!result.hasErrors()) {
                    if (klient == null) {
                        result.reject("error.loginError", "Niepoprawny login.");
                        System.out.println("nie ma takiego kilenta");
                    } else if (!Szyfrowanie.sprawdzPoporawnoscHasla(model.getHaslo(), klient.getHaslo())) {
                        result.reject("error.loginError", "Błędne hasło");
                        System.out.println("hasło sie nie zgadza");
                        int licznik=klient.getLicznik();
                        klient.setLicznik(++licznik);
                        klientRepository.save(klient);
                    }

                }}

            if (result.hasErrors()) {
                modelAndView.setViewName("/unauthorised/login");
                return modelAndView;
            }

            klient.setLicznik(0);
            numerPesel = model.getPesel();
            klientRepository.save(klient);
            aktualnyKlientService.setKlient(klient);
            return new ModelAndView("redirect:/user");
        }

       // modelAndView.setViewName("/unauthorised/login");
       // return modelAndView;
    }
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpSession session) {
        aktualnyKlientService.logOut();
        session.invalidate();

        return "redirect:/";
    }

}
