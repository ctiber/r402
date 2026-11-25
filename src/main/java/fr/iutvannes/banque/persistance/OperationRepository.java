package fr.iutvannes.banque.persistance;

import fr.iutvannes.banque.domaine.Operation;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

public interface OperationRepository extends Repository<Operation, Long> {

    Operation save(Operation operation);

    // Operations d'un compte entre deux instants, de la plus recente a la plus ancienne
    List<Operation> findByCompteNumeroAndDateBetweenOrderByDateDesc(
            String numero, LocalDateTime debut, LocalDateTime fin);

    // Total des debits d'un compte sur une periode (0 s'il n'y en a aucun)
    @Query("""
        SELECT SUM(o.montant) FROM Operation o
         WHERE o.compte.numero = :numero AND o.type = 'DEBIT'
           AND o.date >= :debut AND o.date < :fin
        """)
    BigDecimal totalDebits(String numero, LocalDateTime debut, LocalDateTime fin);
}
