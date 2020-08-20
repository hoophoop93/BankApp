package com.example.bank.bank.controllers;


import com.example.bank.bank.Utils.PasswordGenerator;
import com.example.bank.bank.Utils.Szyfrowanie;
import com.example.bank.bank.dao.KlientDao;
import com.example.bank.bank.dao.KlientRepository;
import com.example.bank.bank.dao.RachunekRepository;
import com.example.bank.bank.models.Administrator;
import com.example.bank.bank.models.Klient;
import com.example.bank.bank.models.Rachunek;
import com.example.bank.bank.models.RejestracjaViewModel;
import com.example.bank.bank.services.AktualnyAdminService;
import com.example.bank.bank.services.AktualnyKlientService;
import com.example.bank.bank.services.NumerKontaService;
import com.example.bank.bank.services.RejestracjaService;
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
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@Controller
public class AdminNowyKlientController {

    @Inject
    AktualnyAdminService aktualnyAdminService;

    @Autowired
    private JavaMailSender sender;

    Administrator administrator;

    @Autowired
    KlientRepository klientRepository;
    @Autowired
    NumerKontaService numerKontaService;
    @Autowired
    KlientDao klientDao;
    @Autowired
    RejestracjaService rejestracjaService;
    @Autowired
    RachunekRepository rachunekRepository;

    @RequestMapping(value = "/adminAdd", method = RequestMethod.GET)
    public ModelAndView adminNowyKlientGet(@ModelAttribute("model") RejestracjaViewModel model) {

        ModelAndView modelAndView = new ModelAndView();

        if (aktualnyAdminService.isAuthenticated()) {
            modelAndView.setViewName("/authorised/adminAdd");
            System.out.println("klient z home " + aktualnyAdminService.getAdmin().getImie());
            System.out.println("Jest zalogowany - strona home");
        } else {
            System.out.println("Przekierowanie na logowanie");
            modelAndView.setViewName("redirect:/login");
        }

        administrator = aktualnyAdminService.getAdmin();
        modelAndView.addObject("administrator", administrator);

        modelAndView.setViewName("/authorised/adminAdd");
        return modelAndView;
    }

    @RequestMapping(value ="/adminAdd",method = RequestMethod.POST)
    public ModelAndView adminNowyKlient(@Valid @ModelAttribute("model")RejestracjaViewModel model, final BindingResult result,
                                     final RedirectAttributes redirectAttributes) throws MessagingException, javax.mail.MessagingException {


        String nowehaslo=PasswordGenerator.GenerateRandomString(8,25,3,1,1,1);
        String hashHaslo = Szyfrowanie.zaszyfrujHaslo(nowehaslo);

        if (!result.hasErrors()) {
            if (klientDao.zliczKlientow(model.getPesel())) {
                result.reject("error.notProjectMember", "Taki pesel jest już w bazie.");
            }
        }
        if (result.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("administrator", administrator);
            modelAndView.setViewName("/authorised/adminAdd");
            return modelAndView;
        }


        model.setHaslo(hashHaslo);
        rejestracjaService.registration(model);

        Klient klient = klientRepository.findByPesel(model.getPesel());

        Rachunek rachunek = new Rachunek();
        rachunek.setNumerKonta(numerKontaService.numerKonta());
        rachunek.setKwota((double) 0);
        rachunek.setRachunekKlienta(klient);
        rachunekRepository.save(rachunek);

        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setTo(klient.getEmail());
        helper.setSubject("BANK - Login i hasło");
        helper.setText("Twoj login: "+klient.getPesel()+ "\n"+ "Twoje hasło: "+nowehaslo);


        sender.send(message);
        List<String> infoMessages = new ArrayList<>();
        infoMessages.add("Konto stworzone prawidłowo. Login i hasło zostało wysłane na E-mail.");

        redirectAttributes.addFlashAttribute("infos", infoMessages);


        return new ModelAndView("redirect:/admin");


    }
}
