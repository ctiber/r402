package fr.iutvannes.banque.service;

import fr.iutvannes.banque.domaine.Operation;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

/** Releve mensuel d'un compte. */
public record Releve(String numeroCompte, YearMonth mois, List<Operation> operations, BigDecimal totalDebits) {
}
