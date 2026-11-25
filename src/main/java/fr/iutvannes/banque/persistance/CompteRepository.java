package fr.iutvannes.banque.persistance;

import fr.iutvannes.banque.domaine.Compte;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

/**
 * Acces aux comptes. Spring Data genere l'implementation a partir des noms de methodes.
 * On etend Repository (et non JpaRepository) pour ne declarer que les methodes utiles :
 * une implementation en memoire (pour les tests) reste ainsi tres simple.
 */
public interface CompteRepository extends Repository<Compte, Long> {

    Optional<Compte> findByNumero(String numero);

    Compte save(Compte compte);

    List<Compte> findAll();

    List<Compte> findByTitulaireOrderByNumero(String titulaire);
}
