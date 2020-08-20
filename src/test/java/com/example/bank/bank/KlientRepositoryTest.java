package com.example.bank.bank;

import com.example.bank.bank.dao.KlientDao;
import com.example.bank.bank.dao.KlientRepository;
import com.example.bank.bank.models.Klient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(classes={BankApplication.class})
public class KlientRepositoryTest {

    @Autowired
    KlientRepository klientRepository;
    @Autowired
    KlientDao klientDao;


    @Test
    public void findByPesel() throws Exception {

        Klient klient = klientRepository.findByPesel("93060209018");
        assertEquals("Sprawdź, czy pesel klienta zgadza się z podanym", klient.getPesel(), "93060209018");
        assertEquals("Sprawdz czy imie klienta zgadza sie z podanym", klient.getImie(), "Bogusław");
    }

    @Test
    public void zliczKlientow() throws Exception {

        boolean result = klientDao.zliczKlientow("93060209018");
        assertTrue("Jeśli pesel poprawny zwróci TRUE", result);

        boolean result2 = klientDao.zliczKlientow("13512674561");
        assertFalse("Jeśli pesel niepoprawny zwróci FALSE", result2);

    }
}
