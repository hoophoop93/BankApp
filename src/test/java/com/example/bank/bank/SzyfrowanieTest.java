package com.example.bank.bank;

import com.example.bank.bank.Utils.Szyfrowanie;
import org.junit.Test;

import static org.junit.Assert.*;

public class SzyfrowanieTest {

    @Test
    public void hashingTest() throws Exception {
        String hashA = Szyfrowanie.zaszyfrujHaslo("hashA");
        String hashB = Szyfrowanie.zaszyfrujHaslo("hashB");

        String sameString = "sameString";
        String sameHashA = Szyfrowanie.zaszyfrujHaslo(sameString);
        String sameHashB = Szyfrowanie.zaszyfrujHaslo(sameString);

        assertEquals("Wszystkie hashe powinny mieć taką samą długość.", hashA.length(), hashB.length());

        assertEquals("Wszystkie hashe powinny mieć długość = 60.", hashA.length(), 60);

        assertNotEquals("Hashe do tego samego tekstu powinny być inne", sameHashA, sameHashB);

        assertNotEquals("Hash z haszowanego łańcucha znaków nie powinien być oryginalnym łańcuchem.", "hashA",
                Szyfrowanie.zaszyfrujHaslo(Szyfrowanie.zaszyfrujHaslo("hashA")));
    }

    @Test
    public void hashingValidationTest() throws Exception {
        String password = "password";

        String hash = Szyfrowanie.zaszyfrujHaslo(password);

        String hashAgain = Szyfrowanie.zaszyfrujHaslo(password);

        assertTrue("Walidacja dla tego samego tekstu powinna zwrócić wartość PRAWDA.", Szyfrowanie.sprawdzPoporawnoscHasla(password, hash));

        assertTrue("Sprawdzanie poprawności hash z losową solą dla tego samego tekstu powinno zwrócić wartość TRUE.", Szyfrowanie.sprawdzPoporawnoscHasla(password, hashAgain));

        assertFalse("Sprawdzanie poprawności hashu dla różnych tekstów powinno zwracać FALSE.", Szyfrowanie.sprawdzPoporawnoscHasla((password + password), hash));

        assertFalse("Sprawdzanie poprawności hasła zawierającego małe litery do hasłą posiadające duże litery powinno zwrócić FALSE",Szyfrowanie.sprawdzPoporawnoscHasla(password.toUpperCase(), hash));
    }
}
