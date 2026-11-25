package fr.iutvannes.banque.securite;

import java.util.Map;
import org.springframework.stereotype.Component;

/** Noms affiches des utilisateurs de demonstration. */
@Component
public class Utilisateurs {

    private static final Map<String, String> NOMS = Map.of(
        "alice", "Alice Martin",
        "bruno", "Bruno Le Goff");

    public String nom(String identifiant) {
        return NOMS.getOrDefault(identifiant, identifiant);
    }
}
