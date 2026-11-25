package fr.iutvannes.banque.service;

import fr.iutvannes.banque.domaine.Compte;
import fr.iutvannes.banque.persistance.CompteRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import org.springframework.stereotype.Service;

/** Virements entre comptes. Les regles V1 a V6 sont decrites dans le sujet du TP3. */
@Service
public class VirementService {

    static final BigDecimal SEUIL_NOTIFICATION = new BigDecimal("1000.00");

    private final CompteRepository comptes;
    private final TauxChangeClient taux;
    private final NotificationService notifications;
    private final Clock horloge;

    public VirementService(CompteRepository comptes, TauxChangeClient taux,
                           NotificationService notifications, Clock horloge) {
        this.comptes = comptes;
        this.taux = taux;
        this.notifications = notifications;
        this.horloge = horloge;
    }

    public void virer(String numeroSource, String numeroCible, BigDecimal montant) {
        if (numeroSource == null || numeroCible == null) {
            throw new IllegalArgumentException("Les deux comptes sont obligatoires");
        }
        if (numeroSource.equals(numeroCible)) {
            throw new IllegalArgumentException("Virement vers le même compte impossible");
        }
        if (LocalDate.now(horloge).getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new VirementImpossibleException("Aucun virement n'est accepté le dimanche");
        }
        Compte source = charger(numeroSource);
        Compte cible = charger(numeroCible);

        source.debiter(montant);

        BigDecimal montantCredite = montant;
        if (source.getDevise() != cible.getDevise()) {
            try {
                montantCredite = montant.multiply(taux.taux(cible.getDevise(), source.getDevise()))
                                        .setScale(2, RoundingMode.HALF_EVEN);
            } catch (TauxIndisponibleException e) {
                throw new VirementImpossibleException("Taux de change indisponible", e);
            }
        }
        comptes.save(source);

        cible.crediter(montantCredite);
        comptes.save(cible);

        if (montant.compareTo(SEUIL_NOTIFICATION) >= 0) {
            notifications.notifier(numeroSource,
                    "Virement de " + montant + " € effectué vers " + numeroCible);
        }
    }

    private Compte charger(String numero) {
        return comptes.findByNumero(numero)
                      .orElseThrow(() -> new CompteInconnuException("Compte inconnu : " + numero));
    }
}
