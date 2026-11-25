package fr.iutvannes.banque.persistance;

import fr.iutvannes.banque.domaine.Compte;
import fr.iutvannes.banque.domaine.Devise;
import fr.iutvannes.banque.domaine.TypeCompte;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Ancienne implementation JDBC, conservee pour AlerteDecouvertJob.
 * Seule la lecture de tous les comptes est implementee.
 */
public class JdbcCompteRepository implements CompteRepository {
    private final String url;
    private final String utilisateur;
    private final String motDePasse;

    public JdbcCompteRepository(String url, String utilisateur, String motDePasse) {
        this.url = url;
        this.utilisateur = utilisateur;
        this.motDePasse = motDePasse;
    }

    @Override
    public List<Compte> findAll() {
        List<Compte> comptes = new ArrayList<>();
        try (Connection connexion = DriverManager.getConnection(url, utilisateur, motDePasse);
             Statement requete = connexion.createStatement();
             ResultSet lignes = requete.executeQuery("SELECT numero, type, devise, solde, titulaire FROM compte")) {
            while (lignes.next()) {
                Compte compte = new Compte(lignes.getString("numero"),
                        TypeCompte.valueOf(lignes.getString("type")),
                        Devise.valueOf(lignes.getString("devise")),
                        lignes.getBigDecimal("solde"));
                compte.setTitulaire(lignes.getString("titulaire"));
                comptes.add(compte);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Lecture des comptes impossible", e);
        }
        return comptes;
    }

    @Override
    public Optional<Compte> findByNumero(String numero) {
        throw new UnsupportedOperationException("Non implemente");
    }

    @Override
    public Compte save(Compte compte) {
        throw new UnsupportedOperationException("Non implemente");
    }

    @Override
    public List<Compte> findByTitulaireOrderByNumero(String titulaire) {
        throw new UnsupportedOperationException("Non implemente");
    }
}
