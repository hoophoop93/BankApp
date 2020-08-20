package com.example.bank.bank.controllers;

import com.example.bank.bank.dao.*;
import com.example.bank.bank.models.*;
import com.example.bank.bank.services.*;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
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
import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class PrzelewCyklicznyController {


    private static final SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
    @Inject
    AktualnyKlientService aktualnyKlientService;

    @Autowired
    RachunekRepository rachunekRepository;

    @Autowired
    KlientRepository klientRepository;

    @Autowired
    RachunekDao rachunekDao;

    @Autowired
    DatyService datyService;

    @Autowired
    TypOperacjiService typOperacjiService;

    @Autowired
    DatyDao datyDao;

    @Autowired
    TransakcjeService transakcjeService;

    @Autowired
    DatyRepository datyRepository;
    @Autowired
    TypOperacjiDao typOperacjiDao;
    @Autowired
    ZleceniaStaleDao zleceniaStaleDao;
    @Autowired
    TypOperacjiRepository typOperacjiRepository;
    @Autowired
    StaliOdbiorcyService staliOdbiorcyService;
    @Autowired
    StaliOdbiorcyRepository staliOdbiorcyRepository;
    @Autowired
    CyklicznyService cyklicznyService;
    Klient klient;
    Rachunek rachunek;


    @RequestMapping(value = "/cykliczne",method = RequestMethod.GET)
    public ModelAndView przelewCykliczny(@ModelAttribute("staliOdbiorcy") StaliOdbiorcy staliOdbiorcy,Model model){
        ModelAndView modelAndView = new ModelAndView();


        if (aktualnyKlientService.isAuthenticated()) {
            modelAndView.setViewName("/authorised/transfer");
            System.out.println("klient z home " + aktualnyKlientService.getKlient().getImie());
            System.out.println("Jest zalogowany - strona home");

        } else {
            System.out.println("Przekierowanie na logowanie");
            modelAndView.setViewName("redirect:/login");
        }
        klient = aktualnyKlientService.getKlient();
        List<StaliOdbiorcy> staliOdbiorcyList = staliOdbiorcyRepository.findByklientPierwszy(klient);
        rachunek = rachunekRepository.findByRachunekKlienta(klient);


        model.addAttribute("klient", klient);
        model.addAttribute("rachunek", rachunek);
        model.addAttribute("staliOdbiorcyList",staliOdbiorcyList);

        System.out.println("Przycik został klikniety get");
        return new ModelAndView("/authorised/cykliczne", "model", new CyklicznyViewModel());
    }

    @RequestMapping(value = "/cykliczne",method = RequestMethod.POST)
    public ModelAndView przelewCyklicznyPost(@Valid @ModelAttribute("model") CyklicznyViewModel model,
                                             final BindingResult result,final RedirectAttributes redirectAttributes)
            throws ParseException {
        ModelAndView modelAndView = new ModelAndView();
        Rachunek rachunek2 = null;
        Klient klient2 = null;
        Klient klient3 = null;
        double kwotaDouble = 0;

        String numerKonta = model.getNumerRachunku();

        String nrkonta = "";
        int licznik = 0;
        for (int i = 0; i < numerKonta.length(); i++) {

            nrkonta += numerKonta.charAt(i);

            if (i == 1) nrkonta += " ";
            if (i > 1) licznik++;
            if (licznik == 4) {
                nrkonta += " ";
                licznik = 0;
            }
        }

        if (!result.hasErrors()) {
            if (rachunekDao.sprawdzCzyNumerKontaJestWBazie(nrkonta)) {
                rachunek2 = rachunekRepository.findBynumerKonta(nrkonta);
                System.out.println(rachunek2.getIdRachunek());

                klient2 = klientRepository.findByrachunekiDanegoKlienta(rachunek2);
                System.out.println("klinet2 " + klient2);
            } else {
                result.reject("error.transferError", "Nie ma klienta o takim numerze konta.");
            }
        }

        klient3 = klientRepository.findByrachunekiDanegoKlienta(rachunek2);
        if (!result.hasErrors()) {
            if (klient3.getImie().equals(model.getImie())) {
            } else {
                result.reject("error.transferError", "Ten numer konta nie pasuje do osoby o takim imieniu.");
            }
        }
        if (!result.hasErrors()) {
            if (klient3.getNazwisko().equals(model.getNazwisko())) {
            } else {
                result.reject("error.transferError", "Ten numer konta nie pasuje do osoby o takim nazwisku.");
            }
        }
        try {
        String dni = model.getIleDni();
        int ileDni = Integer.parseInt(dni);
        Date dt = new Date();
        DateTime dtOrg = new DateTime(dt);
        DateTime dtPlusOne = dtOrg.plusDays(ileDni);

        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
        String dtStr = fmt.print(dtPlusOne);

        if (!result.hasErrors()) {
            if (zleceniaStaleDao.zliczDaty(dtStr)) {
                System.out.println("jest juz data tu");
                result.reject("error.transferError", "W tym dniu zostało dadane już zlecenie stałe.");
            } else {

                System.out.println("jest juz data tu sdafsdfdasfdasfdsfdasfdsfadsfdsf");

                String kwota = model.getKwota();
                kwotaDouble = Double.parseDouble(kwota);


                cyklicznyService.dodajPrzelewCyliczny(model.getTytuł(), kwotaDouble, ileDni, klient, klient2, rachunek2, dtStr);


                List<String> infoMessages = new ArrayList<>();
                infoMessages.add("Przelew cykliczny został ustawiony");

                redirectAttributes.addFlashAttribute("infos", infoMessages);

                System.out.println("Przycik został klikniety post");
            }
        }
        } catch(NumberFormatException e) {
            System.err.println("This is not a number!");
        }
        if (result.hasErrors()) {
            klient = aktualnyKlientService.getKlient();
            List<StaliOdbiorcy> staliOdbiorcyList = staliOdbiorcyRepository.findByklientPierwszy(klient);
            rachunek = rachunekRepository.findByRachunekKlienta(klient);
            modelAndView.addObject("klient", klient);
            modelAndView.addObject("rachunek", rachunek);
            modelAndView.addObject("staliOdbiorcyList",staliOdbiorcyList);
            modelAndView.addObject(new StaliOdbiorcy());
            modelAndView.setViewName("/authorised/cykliczne");
            return modelAndView;
        }

        return new ModelAndView("redirect:/user");
    }
    @RequestMapping(value = "/cykliczne", method = RequestMethod.GET, params = "action=wstawOdbiorce")
    public ModelAndView uzupelnijOdbiorce(@ModelAttribute("staliOdbiorcy") StaliOdbiorcy staliOdbiorcy,
                                          @ModelAttribute("model") CyklicznyViewModel model,final BindingResult result
    ) {
        ModelAndView modelAndView = new ModelAndView();
        try {

            System.out.println("id klienta ----------- " + staliOdbiorcy.getKlientDrugi().getIdKlient());
            Klient klient2 = klientRepository.findByIdKlient(staliOdbiorcy.getKlientDrugi().getIdKlient());
            Rachunek rachunek2 = rachunekRepository.findByRachunekKlienta(klient2);
            System.out.println(klient2 + "  " + rachunek2.getNumerKonta());
            model.setImie(klient2.getImie());
            model.setNazwisko(klient2.getNazwisko());

            String numerKonta = rachunek2.getNumerKonta().replace(" ", "");

            model.setNumerRachunku(numerKonta);

            List<StaliOdbiorcy> staliOdbiorcyList = staliOdbiorcyRepository.findByklientPierwszy(klient);
            modelAndView.addObject("klient", klient);
            modelAndView.addObject("rachunek", rachunek);
            modelAndView.addObject("staliOdbiorcyList", staliOdbiorcyList);


        } catch (NullPointerException nullPointer)
        {
            modelAndView.addObject("klient", klient);
            modelAndView.addObject("rachunek", rachunek);
            result.reject("error.transferError", "Lista odbiorców jest pusta");
        }
        modelAndView.setViewName("/authorised/cykliczne");
        return modelAndView;
    }
}
