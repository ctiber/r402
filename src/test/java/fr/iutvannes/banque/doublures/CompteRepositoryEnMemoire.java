package fr.iutvannes.banque.doublures;

import fr.iutvannes.banque.domaine.Compte;
import fr.iutvannes.banque.persistance.CompteRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/** FAKE : un CompteRepository qui garde les comptes dans une Map, sans base de donnees. */
public class CompteRepositoryEnMemoire implements CompteRepository {

    private final Map<String, Compte> comptes = new LinkedHashMap<>();

    @Override
    public Optional<Compte> findByNumero(String numero) {
        return Optional.ofNullable(comptes.get(numero));
    }

    @Override
    public Compte save(Compte compte) {
        comptes.put(compte.getNumero(), compte);
        return compte;
    }

    @Override
    public List<Compte> findAll() {
        return new ArrayList<>(comptes.values());
    }

    @Override
    public List<Compte> findByTitulaireOrderByNumero(String titulaire) {
        return comptes.values().stream()
                      .filter(c -> Objects.equals(c.getTitulaire(), titulaire))
                      .sorted(Comparator.comparing(Compte::getNumero))
                      .toList();
    }
}
