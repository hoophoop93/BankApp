package com.example.bank.bank.controllers;


import com.example.bank.bank.Utils.PasswordGenerator;
import com.example.bank.bank.Utils.Szyfrowanie;
import com.example.bank.bank.dao.KlientRepository;
import com.example.bank.bank.models.Klient;
import com.example.bank.bank.models.ResetViewModel;
import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@Controller
public class ResetController {
    @Autowired
    private JavaMailSender sender;
    @Autowired
    KlientRepository klientRepository;

    @RequestMapping(value = "/reset",method = RequestMethod.GET)
    public ModelAndView reset(@ModelAttribute("model")ResetViewModel model){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/unauthorised/reset");
        return modelAndView;
    }

    @RequestMapping(value="/reset",method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView reset(@Valid @ModelAttribute("model")ResetViewModel model, final BindingResult result,
                              final RedirectAttributes redirectAttributes) throws MessagingException, javax.mail.MessagingException {

        ModelAndView modelAndView = new ModelAndView();

        Klient klient = klientRepository.findByPesel(model.getPesel());
        if(klient!=null){
            String nowehaslo=PasswordGenerator.GenerateRandomString(8,25,3,1,1,1);
            String hashHaslo = Szyfrowanie.zaszyfrujHaslo(nowehaslo);

            System.out.println(nowehaslo);
            System.out.println(hashHaslo);

            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setTo(klient.getEmail());
            helper.setSubject("Resetowanie hasła");
            helper.setText("Twoje nowe hasło to: "+nowehaslo);


            sender.send(message);

            klient.setHaslo(hashHaslo);
            klientRepository.save(klient);


            /*result.reject("error.loginError", "Nowe hasło zostało wysłane.");
            model.setPesel("");*/
        }
        else{
            result.reject("error.loginError", "Brak użytkownika o takim PESELU.");
        }
        List<String> infoMessages = new ArrayList<>();
        infoMessages.add("Hasło zostało wysłane na E-mail.");

        redirectAttributes.addFlashAttribute("infos", infoMessages);
        return new ModelAndView("redirect:/login");
    }

}
