package fr.iutvannes.banque.web;

import fr.iutvannes.banque.domaine.Compte;
import fr.iutvannes.banque.domaine.Devise;
import fr.iutvannes.banque.persistance.CompteRepository;
import fr.iutvannes.banque.securite.Utilisateurs;
import fr.iutvannes.banque.service.CompteInconnuException;
import fr.iutvannes.banque.service.Releve;
import fr.iutvannes.banque.service.ReleveService;
import fr.iutvannes.banque.service.TauxChangeClient;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.time.YearMonth;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CompteController {

    public record UtilisateurDto(String identifiant, String nom) { }
    public record CompteDto(String numero, String type, String devise, BigDecimal solde) { }
    public record SoldeDto(String numero, BigDecimal solde, String devise) { }
    public record OperationDto(String type, BigDecimal montant, String date) { }
    public record ReleveDto(String numero, String mois, List<OperationDto> operations, BigDecimal totalDebits) { }

    private final CompteRepository comptes;
    private final TauxChangeClient taux;
    private final ReleveService releves;
    private final Utilisateurs utilisateurs;

    public CompteController(CompteRepository comptes, TauxChangeClient taux,
                            ReleveService releves, Utilisateurs utilisateurs) {
        this.comptes = comptes;
        this.taux = taux;
        this.releves = releves;
        this.utilisateurs = utilisateurs;
    }

    /** L'utilisateur connecte. */
    @GetMapping("/moi")
    public UtilisateurDto moi(Principal utilisateur) {
        return new UtilisateurDto(utilisateur.getName(), utilisateurs.nom(utilisateur.getName()));
    }

    /** Les comptes de l'utilisateur connecte. */
    @GetMapping("/comptes")
    public List<CompteDto> mesComptes(Principal utilisateur) {
        return comptes.findByTitulaireOrderByNumero(utilisateur.getName()).stream()
            .map(c -> new CompteDto(c.getNumero(), c.getType().name(), c.getDevise().name(), c.getSolde()))
            .toList();
    }

    @GetMapping("/comptes/{numero}/solde")
    public SoldeDto solde(@PathVariable String numero, @RequestParam(defaultValue = "EUR") Devise devise) {
        Compte compte = charger(numero);
        BigDecimal solde = compte.getSolde();
        if (devise != compte.getDevise()) {
            solde = solde.multiply(taux.taux(compte.getDevise(), devise)).setScale(2, RoundingMode.HALF_EVEN);
        }
        return new SoldeDto(numero, solde, devise.name());
    }

    @GetMapping("/comptes/{numero}/releve")
    public ReleveDto releve(@PathVariable String numero, @RequestParam YearMonth mois) {
        charger(numero);
        Releve releve = releves.releve(numero, mois);
        return new ReleveDto(numero, mois.toString(),
            releve.operations().stream()
                  .map(o -> new OperationDto(o.getType().name(), o.getMontant(), o.getDate().toString()))
                  .toList(),
            releve.totalDebits());
    }

    private Compte charger(String numero) {
        return comptes.findByNumero(numero)
                      .orElseThrow(() -> new CompteInconnuException("Compte inconnu : " + numero));
    }
}
