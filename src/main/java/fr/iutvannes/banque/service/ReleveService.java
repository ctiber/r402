package fr.iutvannes.banque.service;

import fr.iutvannes.banque.persistance.OperationRepository;
import java.time.LocalDateTime;
import java.time.YearMonth;
import org.springframework.stereotype.Service;

@Service
public class ReleveService {

    private final OperationRepository operations;

    public ReleveService(OperationRepository operations) {
        this.operations = operations;
    }

    /** Releve du mois : du 1er du mois a 0 h (inclus) au 1er du mois suivant a 0 h (exclu). */
    public Releve releve(String numeroCompte, YearMonth mois) {
        LocalDateTime debut = mois.atDay(1).atStartOfDay();
        LocalDateTime fin = mois.plusMonths(1).atDay(1).atStartOfDay();
        return new Releve(numeroCompte, mois,
                operations.findByCompteNumeroAndDateBetweenOrderByDateDesc(numeroCompte, debut, fin),
                operations.totalDebits(numeroCompte, debut, fin));
    }
}
