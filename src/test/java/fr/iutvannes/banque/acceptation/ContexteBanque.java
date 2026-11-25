package fr.iutvannes.banque.acceptation;

import fr.iutvannes.banque.domaine.Compte;
import fr.iutvannes.banque.domaine.Devise;
import fr.iutvannes.banque.doublures.CompteRepositoryEnMemoire;
import fr.iutvannes.banque.doublures.HorlogeModifiable;
import fr.iutvannes.banque.service.TauxChangeClient;
import fr.iutvannes.banque.service.VirementService;
import io.cucumber.spring.ScenarioScope;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * L'etat partage par les etapes d'un scenario : des comptes en memoire, un VirementService
 * pret a l'emploi (sans base de donnees ni reseau), une horloge que l'on peut fixer.
 * Recevez-le dans le constructeur de vos classes d'etapes.
 */
@Component
@ScenarioScope
public class ContexteBanque {

    private final CompteRepositoryEnMemoire comptes = new CompteRepositoryEnMemoire();
    private final HorlogeModifiable horloge = new HorlogeModifiable(LocalDate.of(2027, 3, 1));
    private final Map<Devise, BigDecimal> tauxDepuisEuro = new EnumMap<>(Devise.class);
    private final List<String> notifications = new ArrayList<>();
    private final VirementService virementService;
    private Exception derniereErreur;

    public ContexteBanque() {
        TauxChangeClient taux = (de, vers) -> tauxDepuisEuro.getOrDefault(vers, BigDecimal.ONE);
        virementService = new VirementService(comptes, taux,
                (numero, message) -> notifications.add(numero + " : " + message), horloge);
    }

    public void ajouterCompte(Compte compte) { comptes.save(compte); }
    public Compte compte(String numero) { return comptes.findByNumero(numero).orElseThrow(); }
    public VirementService virementService() { return virementService; }

    public void fixerDate(LocalDate date) { horloge.fixerDate(date); }
    public HorlogeModifiable horloge() { return horloge; }

    /** Taux de change simule : 1 euro vaut {@code taux} dans la devise donnee. */
    public void fixerTaux(Devise devise, BigDecimal taux) { tauxDepuisEuro.put(devise, taux); }

    public List<String> notifications() { return notifications; }

    public Exception getDerniereErreur() { return derniereErreur; }
    public void setDerniereErreur(Exception erreur) { this.derniereErreur = erreur; }
}
