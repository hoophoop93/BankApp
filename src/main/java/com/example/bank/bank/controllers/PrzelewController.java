package com.example.bank.bank.controllers;


import com.example.bank.bank.dao.*;
import com.example.bank.bank.models.*;
import com.example.bank.bank.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.inject.Inject;
import javax.validation.Valid;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class PrzelewController {

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
    TypOperacjiRepository typOperacjiRepository;
    @Autowired
    StaliOdbiorcyService staliOdbiorcyService;
    @Autowired
    StaliOdbiorcyRepository staliOdbiorcyRepository;

    Klient klient;
    Rachunek rachunek;

    @RequestMapping(value = "/transfer", method = RequestMethod.GET)
    public ModelAndView transfer(@ModelAttribute("staliOdbiorcy") StaliOdbiorcy staliOdbiorcy,Model model) {

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
        rachunek = rachunekRepository.findByRachunekKlienta(klient);

        List<StaliOdbiorcy> staliOdbiorcyList = staliOdbiorcyRepository.findByklientPierwszy(klient);
        for(StaliOdbiorcy staliOdbiorcy2 : staliOdbiorcyList){
            System.out.println(staliOdbiorcyList.size()+"pokaze coś czy nie "+staliOdbiorcy2.getKlientDrugi().getImie());
        }



        model.addAttribute("klient", klient);
        model.addAttribute("rachunek", rachunek);
        model.addAttribute("staliOdbiorcyList",staliOdbiorcyList);

        System.out.println("get od transferu");

        return new ModelAndView("/authorised/transfer", "model", new PrzelewViewModel());
    }

    @RequestMapping(value = "/transfer", method = RequestMethod.POST, params = "action=wyslijPrzelew")
    public ModelAndView wyslijPrzelew(@Valid @ModelAttribute("model") PrzelewViewModel model,
                                      final BindingResult result,
                                      final RedirectAttributes redirectAttributes) throws ParseException {
        Rachunek rachunek2 = null;
        Klient klient2;
        Klient klient3 = null;
        Daty daty = null;
        TypOperacji typOperacji = null;
        String nazwaOperacji = "Przelew";
        double kwotaDouble = 0;

        System.out.println("działa ten przycisk nowy");
        Klient klient = aktualnyKlientService.getKlient();
        Rachunek rachunek = rachunekRepository.findByRachunekKlienta(klient);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date date = new Date();
        String dataDoZapytania = dateFormat.format(date);

        System.out.println(datyDao.zliczDaty(dataDoZapytania));


        if (datyDao.zliczDaty(dataDoZapytania)) {

            daty = datyRepository.findBydata(dataDoZapytania);
            System.out.println("data" + daty.getIdDaty());
        } else {
            System.out.println("zapisuje nowa date do bazy");
            datyService.zapiszDate(dataDoZapytania);
            daty = datyRepository.findBydata(dataDoZapytania);
        }

        System.out.println(typOperacjiDao.zliczRodzajeOperacji(nazwaOperacji));

        if (typOperacjiDao.zliczRodzajeOperacji(nazwaOperacji)) {
            typOperacji = typOperacjiRepository.findByrodzajOperacji(nazwaOperacji);
            System.out.println("typ operacji " + typOperacji);
        } else {
            typOperacjiService.zapiszRodzajOperacji(nazwaOperacji);
            typOperacji = typOperacjiRepository.findByrodzajOperacji(nazwaOperacji);
        }


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

        if (!result.hasErrors()) {
            String kwota = model.getKwota();
            kwotaDouble = Double.parseDouble(kwota);
            System.out.println("kwota pobrana i zmieniona na double" + kwotaDouble);
            if (rachunek.getKwota() <= kwotaDouble) {
                result.reject("error.transferError", "Nie masz wystarczająco środków na koncie.");
            }
        }

        if (result.hasErrors()) {
            List<StaliOdbiorcy> staliOdbiorcyList = staliOdbiorcyRepository.findByklientPierwszy(klient);


            System.out.println("sa błedy");
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("klient", klient);
            modelAndView.addObject("rachunek", rachunek);
            modelAndView.addObject("staliOdbiorcyList",staliOdbiorcyList);
            modelAndView.addObject(new StaliOdbiorcy());
            modelAndView.setViewName("/authorised/transfer");
            return modelAndView;

        }

        transakcjeService.zapiszTransakcje(model.getTytuł(), kwotaDouble, typOperacji, rachunek, rachunek2, daty);
        double srodkiNaKoncie = rachunek.getKwota();
        double pozostalo = srodkiNaKoncie - kwotaDouble;
        rachunek.setKwota(pozostalo);
        rachunekRepository.save(rachunek);

        double srodkiNaKoncieOdbiorcy = rachunek2.getKwota();
        double nowaKwotaOdbiorcy = srodkiNaKoncieOdbiorcy + kwotaDouble;
        rachunek2.setKwota(nowaKwotaOdbiorcy);
        rachunekRepository.save(rachunek2);

        return new ModelAndView("redirect:/user");

    }

    @RequestMapping(value = "/transfer", method = RequestMethod.POST, params = "action=dodajOdbiorce")
    public ModelAndView dodajOdbiorce(@Valid @ModelAttribute("model") PrzelewViewModel model,
                                      final BindingResult result,
                                      final RedirectAttributes redirectAttributes) {

        ModelAndView modelAndView = new ModelAndView();
        Rachunek rachunek2 = null;
        Klient klient2;
        Klient klient3 = null;
        String numerKonta = model.getNumerRachunku();
        String brak = "brak";


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


        if (result.hasErrors()) {
            List<StaliOdbiorcy> staliOdbiorcyList = staliOdbiorcyRepository.findByklientPierwszy(klient);
            modelAndView.addObject("klient", klient);
            modelAndView.addObject("rachunek", rachunek);
            modelAndView.addObject("staliOdbiorcyList",staliOdbiorcyList);
            modelAndView.addObject(new StaliOdbiorcy());
            modelAndView.setViewName("/authorised/transfer");
            return modelAndView;

        }

        staliOdbiorcyService.zapiszStalegoOdiorce(klient, klient3);

        List<String> infoMessages = new ArrayList<>();
        infoMessages.add("Klient został zapisany do listy stałych odbiorców.");

        redirectAttributes.addFlashAttribute("infos", infoMessages);

        return new ModelAndView("redirect:/user");
    }

    @RequestMapping(value = "/transfer", method = RequestMethod.GET, params = "action=wstawOdbiorce")
    public ModelAndView uzupelnijOdbiorce(@ModelAttribute("staliOdbiorcy") StaliOdbiorcy staliOdbiorcy,
                                          @ModelAttribute("model") PrzelewViewModel model,final BindingResult result
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
        modelAndView.setViewName("/authorised/transfer");
        return modelAndView;
    }
}
