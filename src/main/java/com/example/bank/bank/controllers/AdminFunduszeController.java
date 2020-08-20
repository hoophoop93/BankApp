package com.example.bank.bank.controllers;

import com.example.bank.bank.dao.*;
import com.example.bank.bank.models.*;
import com.example.bank.bank.services.*;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Controller
public class AdminFunduszeController {

    @Inject
    AktualnyAdminService aktualnyAdminService;

    Administrator administrator;

    @Autowired
    KlientRepository klientRepository;

    @Autowired
    RachunekRepository rachunekRepository;

    @Autowired
    KlientDao klientDao;

    @Autowired
    RachunekDao rachunekDao;
    @Autowired
    TransakcjeWplataService transakcjeWplataService;
    @Autowired
    DatyDao datyDao;
    @Autowired
    DatyService datyService;

    @Autowired
    TypOperacjiService typOperacjiService;

    @Autowired
    DatyRepository datyRepository;
    @Autowired
    TypOperacjiDao typOperacjiDao;
    @Autowired
    TypOperacjiRepository typOperacjiRepository;


    @RequestMapping(value = "/adminFundusze", method = RequestMethod.GET)
    public ModelAndView adminFunduszeGet(Model model) {

        ModelAndView modelAndView = new ModelAndView();
        if (aktualnyAdminService.isAuthenticated()) {

            modelAndView.setViewName("/authorised/adminFundusze");
            System.out.println("klient z home " + aktualnyAdminService.getAdmin().getImie());
            System.out.println("Jest zalogowany - strona home");
        } else {
            System.out.println("Przekierowanie na logowanie");
            modelAndView.setViewName("redirect:/login");
        }
        List<Klient> klientList = (List<Klient>) klientRepository.findAll();
        List<Rachunek> rachunekList = (List<Rachunek>) rachunekRepository.findAll();

        administrator = aktualnyAdminService.getAdmin();


        model.addAttribute("administrator", administrator);
        model.addAttribute("rachunekList", rachunekList);
        model.addAttribute("klientList", klientList);


        return new ModelAndView("/authorised/adminFundusze", "model", new KwotaViewModel());
    }

    @RequestMapping(value = "/adminFundusze", method = RequestMethod.POST, params = "action=wplata")
    public ModelAndView adminFunduszePost(@Valid @ModelAttribute("model") KwotaViewModel model,
                                          final BindingResult result,
                                          final RedirectAttributes redirectAttributes) throws ParseException {

        Daty daty = null;
        TypOperacji typOperacji = null;
        String nazwaOperacji = "Wpłata";
        double kwotaDouble = 0;

        Klient klient =  klientRepository.findByPesel(model.getPesel());
        Rachunek rachunek = rachunekRepository.findByRachunekKlienta(klient);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date date = new Date();
        String dataDoZapytania = dateFormat.format(date);

        if (datyDao.zliczDaty(dataDoZapytania)) {

            daty = datyRepository.findBydata(dataDoZapytania);
            System.out.println("data jest juz w bazie" + daty.getIdDaty());
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

        if (!result.hasErrors()) {
            if (!klientDao.zliczKlientow(model.getPesel())) {
                result.reject("error.notProjectMember", "Nie ma klienta o takim numerze pesel.");
            }
        }


        if (result.hasErrors()) {
            administrator = aktualnyAdminService.getAdmin();
            System.out.println("sa błedy");
            ModelAndView modelAndView = new ModelAndView();
            List<Klient> klientList = (List<Klient>) klientRepository.findAll();
            List<Rachunek> rachunekList = (List<Rachunek>) rachunekRepository.findAll();

            modelAndView.addObject("rachunekList", rachunekList);
            modelAndView.addObject("administrator", administrator);
            modelAndView.addObject("klientList", klientList);
            modelAndView.addObject(new KwotaViewModel());
            modelAndView.setViewName("/authorised/adminFundusze");
            return modelAndView;

        }

        String kwota2 = model.getKwota();
        kwotaDouble = Double.parseDouble(kwota2);


        transakcjeWplataService.zapiszTransakcjeWplata(administrator,kwotaDouble, typOperacji,
                rachunek, daty,"brak");

        String kwota = model.getKwota();
        double kwotaKonweter = Double.parseDouble(kwota);
        double kwotaZKonta= rachunek.getKwota();
        rachunek.setKwota(kwotaZKonta+kwotaKonweter);
        rachunek.setNumerKonta(rachunek.getNumerKonta());
        rachunek.setRachunekKlienta(klient);
        rachunekRepository.save(rachunek);

        List<String> infoMessages = new ArrayList<>();
        infoMessages.add("Kwota "+ kwotaDouble +" została dodana do wybranego klienta.");

        redirectAttributes.addFlashAttribute("infos", infoMessages);



        return new ModelAndView("redirect:/admin");

    }

    @RequestMapping(value = "/adminFundusze", method = RequestMethod.POST,  params = "action=wyplata")
    public ModelAndView adminFunduszeWyplata(@Valid @ModelAttribute("model") KwotaViewModel model,
                                          final BindingResult result,
                                          final RedirectAttributes redirectAttributes) throws ParseException {

        Daty daty = null;
        TypOperacji typOperacji = null;
        String nazwaOperacji = "Wypłata";
        double kwotaDouble = 0;

        Klient klient =  klientRepository.findByPesel(model.getPesel());
        Rachunek rachunek = rachunekRepository.findByRachunekKlienta(klient);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date date = new Date();
        String dataDoZapytania = dateFormat.format(date);

        if (datyDao.zliczDaty(dataDoZapytania)) {

            daty = datyRepository.findBydata(dataDoZapytania);
            System.out.println("data jest juz w bazie" + daty.getIdDaty());
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

        if (!result.hasErrors()) {
            if (!klientDao.zliczKlientow(model.getPesel())) {
                result.reject("error.notProjectMember", "Nie ma klienta o takim numerze pesel.");
            }
        }

        if (!result.hasErrors()) {
            String kwota = model.getKwota();
            kwotaDouble = Double.parseDouble(kwota);
            if (rachunek.getKwota() < kwotaDouble) {
                result.reject("error.transferError", "Nie masz wystarczająco środków na koncie.");
            }
        }
        if (result.hasErrors()) {
            administrator = aktualnyAdminService.getAdmin();
            System.out.println("sa błedy");
            ModelAndView modelAndView = new ModelAndView();
            List<Klient> klientList = (List<Klient>) klientRepository.findAll();
            List<Rachunek> rachunekList = (List<Rachunek>) rachunekRepository.findAll();

            modelAndView.addObject("rachunekList", rachunekList);
            modelAndView.addObject("administrator", administrator);
            modelAndView.addObject("klientList", klientList);
            modelAndView.addObject(new KwotaViewModel());
            modelAndView.setViewName("/authorised/adminFundusze");
            return modelAndView;

        }

        String kwota2 = model.getKwota();
        kwotaDouble = Double.parseDouble(kwota2);


        transakcjeWplataService.zapiszTransakcjeWplata(administrator,kwotaDouble,
                typOperacji, rachunek, daty,"brak2");

        String kwota = model.getKwota();
        double kwotaKonweter = Double.parseDouble(kwota);
        double kwotaZKonta= rachunek.getKwota();
        rachunek.setKwota(kwotaZKonta-kwotaKonweter);
        rachunek.setNumerKonta(rachunek.getNumerKonta());
        rachunek.setRachunekKlienta(klient);
        rachunekRepository.save(rachunek);

        List<String> infoMessages = new ArrayList<>();
        infoMessages.add("Kwota "+ kwotaDouble +" została wypłacona.");

        redirectAttributes.addFlashAttribute("infos", infoMessages);



        return new ModelAndView("redirect:/admin");

    }
}
