package fr.iutvannes.banque.infrastructure;

import fr.iutvannes.banque.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Envoi de SMS par un fournisseur externe.
 * Dans ce projet d'enseignement, l'envoi est simule : le message est seulement journalise.
 */
@Component
public class SmsNotificationService implements NotificationService {

    private static final Logger LOG = LoggerFactory.getLogger(SmsNotificationService.class);

    private final String url;

    public SmsNotificationService(@Value("${banque.sms.url:https://api.sms.example}") String url,
                                  @Value("${banque.sms.cle:CLE-DE-DEMONSTRATION}") String cleApi) {
        this.url = url;
    }

    @Override
    public void notifier(String numeroCompte, String message) {
        LOG.info("SMS (simulé, via {}) au titulaire du compte {} : {}", url, numeroCompte, message);
    }
}
