package com.example.bank.bank.Utils;



import com.example.bank.bank.controllers.LoginController;
import com.example.bank.bank.dao.*;
import com.example.bank.bank.models.*;
import com.example.bank.bank.services.*;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import org.springframework.stereotype.Component;


import javax.inject.Inject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class ZadaniaCykliczne {
    //private static final Logger log = LoggerFactory.getLogger(ScheduledTask.class);
    //private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private static final SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    ZleceniaStaleRepository zleceniaStaleRepository;
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


    Rachunek rachunek;

    @Scheduled(fixedRate = 60000)
    public void executeTask() {
        Daty daty = null;
        TypOperacji typOperacji = null;
        String nazwaOperacji = "Przelew cykliczny";

        Date currentDate = new Date();
        String aktualnaData=date.format(currentDate);



        List<ZlecenieStale> zlecenieStalesDaty = (List<ZlecenieStale>) zleceniaStaleRepository.findAll();
        System.out.println("Data: "+aktualnaData);



        for(int i =0; i< zlecenieStalesDaty.size(); i++)
        if(aktualnaData.trim().equals(zlecenieStalesDaty.get(i).getDataWykoniani().trim())){
            System.out.println("daty sa takie same");

            if (datyDao.zliczDaty(aktualnaData)) {

                daty = datyRepository.findBydata(aktualnaData);
                System.out.println("data" + daty.getIdDaty());
            } else {
                System.out.println("zapisuje nowa date do bazy");
                datyService.zapiszDate(aktualnaData);
                daty = datyRepository.findBydata(aktualnaData);
            }

            System.out.println(typOperacjiDao.zliczRodzajeOperacji(nazwaOperacji));

            if (typOperacjiDao.zliczRodzajeOperacji(nazwaOperacji)) {
                typOperacji = typOperacjiRepository.findByrodzajOperacji(nazwaOperacji);
                System.out.println("typ operacji " + typOperacji);
            } else {
                typOperacjiService.zapiszRodzajOperacji(nazwaOperacji);
                typOperacji = typOperacjiRepository.findByrodzajOperacji(nazwaOperacji);
            }

            ZlecenieStale zlecenieStale = zleceniaStaleRepository.findBydataWykoniani(aktualnaData);
            Rachunek rachunek = rachunekRepository.findByRachunekKlienta(zlecenieStale.getKlientZleceniaStale());
            Rachunek rachunek2 = rachunekRepository.findByRachunekKlienta(zlecenieStale.getKlientZleceniaStaleOdbiorca());


                if (rachunek.getKwota() <= zlecenieStale.getKwota()) {

                    System.out.println("nie ma pieniedzy");
                }else{
                    try {
                        System.out.println("dodało transakcje");
                        transakcjeService.zapiszTransakcje(zlecenieStale.getTytul(), zlecenieStale.getKwota(), typOperacji, rachunek,rachunek2, daty);
                        double srodkiNaKoncie = rachunek.getKwota();
                        double pozostalo = srodkiNaKoncie - zlecenieStale.getKwota();
                        rachunek.setKwota(pozostalo);
                        rachunekRepository.save(rachunek);

                        double srodkiNaKoncieOdbiorcy = rachunek2.getKwota();
                        double nowaKwotaOdbiorcy = srodkiNaKoncieOdbiorcy + zlecenieStale.getKwota();
                        rachunek2.setKwota(nowaKwotaOdbiorcy);
                        rachunekRepository.save(rachunek2);

                        Date dt = new Date();
                        DateTime dtOrg = new DateTime(dt);
                        System.out.println(dtOrg);
                        String ileDni = Long.toString(zlecenieStale.getLiczbaDni());
                        System.out.println("ile dni z bazy"+ ileDni);
                        int dniDoZapytania = Integer.parseInt(ileDni);

                        DateTime dtPlusOne = dtOrg.plusDays(dniDoZapytania);
                        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
                        String dtStr = fmt.print(dtPlusOne);
                        System.out.println(dtStr);

                        zlecenieStale.setDataWykoniani(dtStr);
                        zleceniaStaleRepository.save(zlecenieStale);
                        System.out.println("koniec");
                    } catch(NumberFormatException e) {
                        System.err.println("This is not a number!");
                    }

                }

        }
        else{
            System.out.println("daty sa inne");
        }
    }
}
