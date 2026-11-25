package fr.iutvannes.banque.domaine;

import static fr.iutvannes.banque.TestUtils.eur;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CompteTest {

    @Test
    void crediter_augmenteLeSolde() {
        Compte compte = new Compte("FR76-001", eur("100.00"));

        compte.crediter(eur("50.00"));

        assertThat(compte.getSolde()).isEqualByComparingTo("150.00");
    }

    @Test
    void crediter_ajouteUneOperationALHistorique() {
        Compte compte = new Compte("FR76-001", eur("100.00"));

        compte.crediter(eur("50.00"));

        assertThat(compte.getOperations()).hasSize(1);
        assertThat(compte.getOperations().get(0).getType()).isEqualTo(TypeOperation.CREDIT);
    }

    @Test
    void debiter_diminueLeSolde() {
        Compte compte = new Compte("FR76-001", eur("100.00"));

        compte.debiter(eur("30.00"));

        assertThat(compte.getSolde()).isEqualByComparingTo("70.00");
    }
}
