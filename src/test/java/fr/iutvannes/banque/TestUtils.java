package fr.iutvannes.banque;

import java.math.BigDecimal;

/** Petites aides pour ecrire des tests lisibles. */
public final class TestUtils {

    private TestUtils() { }

    /** eur("12.50") : raccourci pour new BigDecimal("12.50"). */
    public static BigDecimal eur(String montant) {
        return new BigDecimal(montant);
    }
}
