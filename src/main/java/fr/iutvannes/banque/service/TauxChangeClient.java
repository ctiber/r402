package fr.iutvannes.banque.service;

import fr.iutvannes.banque.domaine.Devise;
import java.math.BigDecimal;

/** Fournisseur de taux de change (service externe). */
public interface TauxChangeClient {
    /**
     * @return combien vaut 1 unite de la devise {@code de} dans la devise {@code vers}
     * @throws TauxIndisponibleException si le fournisseur ne repond pas
     */
    BigDecimal taux(Devise de, Devise vers);
}
