package fr.iutvannes.banque.infrastructure;

import fr.iutvannes.banque.domaine.Devise;
import fr.iutvannes.banque.service.TauxChangeClient;
import fr.iutvannes.banque.service.TauxIndisponibleException;
import java.math.BigDecimal;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

/**
 * Adaptateur vers le fournisseur de taux de change :
 * GET {url}/v1/{de}/{vers} renvoie {"taux": 1.0837}.
 */
@Component
public class HttpTauxChangeClient implements TauxChangeClient {

    private final RestClient http;

    public HttpTauxChangeClient(@Value("${banque.taux.url:https://api.taux-change.example}") String url) {
        this.http = RestClient.create(url);
    }

    @Override
    public BigDecimal taux(Devise de, Devise vers) {
        try {
            Map<?, ?> reponse = http.get().uri("/v1/{de}/{vers}", de, vers).retrieve().body(Map.class);
            if (reponse == null || reponse.get("taux") == null) {
                throw new TauxIndisponibleException("Réponse du fournisseur de taux invalide");
            }
            return new BigDecimal(reponse.get("taux").toString());
        } catch (RestClientException e) {
            throw new TauxIndisponibleException("Fournisseur de taux injoignable : " + e.getMessage());
        }
    }
}
