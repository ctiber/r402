package fr.iutvannes.banque.job;

import fr.iutvannes.banque.domaine.Compte;
import fr.iutvannes.banque.infrastructure.SmsNotificationService;
import fr.iutvannes.banque.persistance.CompteRepository;
import fr.iutvannes.banque.persistance.JdbcCompteRepository;
import fr.iutvannes.banque.service.NotificationService;
import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * Executee chaque matin par un ordonnanceur externe : previent par SMS les titulaires
 * des comptes a decouvert, sauf le week-end (les alertes sont alors envoyees le lundi).
 */
public class AlerteDecouvertJob {

    public int executer() {
        CompteRepository comptes = new JdbcCompteRepository(
            "jdbc:postgresql://localhost:5432/banque", "banque", "secret");
        NotificationService sms =
            new SmsNotificationService("https://api.sms.example", "CLE-API");

        DayOfWeek jour = LocalDate.now().getDayOfWeek();
        if (jour == DayOfWeek.SATURDAY || jour == DayOfWeek.SUNDAY) {
            return 0;
        }
        int nbAlertes = 0;
        for (Compte compte : comptes.findAll()) {
            if (compte.getSolde().signum() < 0) {
                sms.notifier(compte.getNumero(),
                             "Votre compte est à découvert le " + LocalDate.now());
                nbAlertes++;
            }
        }
        return nbAlertes;
    }
}
