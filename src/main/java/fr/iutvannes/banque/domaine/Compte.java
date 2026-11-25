package fr.iutvannes.banque.domaine;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.FetchType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Un compte bancaire. Les regles metier R1 a R5 sont decrites dans le sujet du TP1.
 */
@Entity
public class Compte {

    public static final BigDecimal PLAFOND_EPARGNE = new BigDecimal("22950.00");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numero;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeCompte type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Devise devise;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal solde;

    @Column(name = "decouvert_autorise", nullable = false, precision = 12, scale = 2)
    private BigDecimal decouvertAutorise = BigDecimal.ZERO;

    @Column(nullable = false)
    private boolean cloture;

    /** Identifiant (login) du titulaire ; null pour un compte cree dans un test unitaire. */
    private String titulaire;

    @OneToMany(mappedBy = "compte", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderBy("date")
    private List<Operation> operations = new ArrayList<>();

    protected Compte() {
        // pour JPA
    }

    /** Compte courant en euros. */
    public Compte(String numero, BigDecimal soldeInitial) {
        this(numero, TypeCompte.COURANT, Devise.EUR, soldeInitial);
    }

    /** Compte courant ou epargne, en euros. */
    public Compte(String numero, TypeCompte type, BigDecimal soldeInitial) {
        this(numero, type, Devise.EUR, soldeInitial);
    }

    /** Compte courant dans une devise donnee. */
    public Compte(String numero, Devise devise, BigDecimal soldeInitial) {
        this(numero, TypeCompte.COURANT, devise, soldeInitial);
    }

    public Compte(String numero, TypeCompte type, Devise devise, BigDecimal soldeInitial) {
        this.numero = Objects.requireNonNull(numero);
        this.type = Objects.requireNonNull(type);
        this.devise = Objects.requireNonNull(devise);
        this.solde = Objects.requireNonNull(soldeInitial);
    }

    public void setDecouvertAutorise(BigDecimal decouvert) {
        if (type == TypeCompte.EPARGNE) {
            throw new IllegalStateException("Pas de découvert sur un compte épargne");
        }
        if (decouvert == null || decouvert.signum() < 0) {
            throw new IllegalArgumentException("Découvert invalide : " + decouvert);
        }
        this.decouvertAutorise = decouvert;
    }

    public void crediter(BigDecimal montant) {
        verifierOuvert();
        verifierMontant(montant);
        if (type == TypeCompte.EPARGNE && solde.compareTo(PLAFOND_EPARGNE) > 0) {
            throw new PlafondDepasseException(numero);
        }
        solde = solde.add(montant);
        operations.add(new Operation(this, TypeOperation.CREDIT, montant, LocalDateTime.now()));
    }

    public void debiter(BigDecimal montant) {
        verifierOuvert();
        verifierMontant(montant);
        operations.add(new Operation(this, TypeOperation.DEBIT, montant, LocalDateTime.now()));
        if (solde.subtract(montant).compareTo(decouvertAutorise.negate()) <= 0) {
            throw new SoldeInsuffisantException(numero);
        }
        solde = solde.subtract(montant);
    }

    /**
     * Preleve les frais mensuels de tenue de compte.
     * Les frais sont toujours preleves, meme si le compte passe au-dela du decouvert autorise.
     */
    public void appliquerFraisTenueCompte(BigDecimal frais) {
        verifierOuvert();
        verifierMontant(frais);
        solde = solde.subtract(frais);
        operations.add(new Operation(this, TypeOperation.FRAIS, frais, LocalDateTime.now()));
    }

    public void cloturer() {
        if (solde.signum() > 0) {
            throw new IllegalStateException("Un compte ne peut être clôturé que si son solde est nul");
        }
        cloture = true;
    }

    private void verifierOuvert() {
        if (cloture) {
            throw new CompteClotureException(numero);
        }
    }

    private static void verifierMontant(BigDecimal montant) {
        if (montant == null) {
            throw new IllegalArgumentException("Le montant est obligatoire");
        }
        if (montant.signum() < 0) {
            throw new IllegalArgumentException("Le montant doit être strictement positif : " + montant);
        }
    }

    public Long getId() { return id; }
    public String getNumero() { return numero; }
    public TypeCompte getType() { return type; }
    public Devise getDevise() { return devise; }
    public BigDecimal getSolde() { return solde; }
    public BigDecimal getDecouvertAutorise() { return decouvertAutorise; }
    public boolean isCloture() { return cloture; }
    public String getTitulaire() { return titulaire; }
    public void setTitulaire(String titulaire) { this.titulaire = titulaire; }

    /** Historique des operations, non modifiable. */
    public List<Operation> getOperations() {
        return List.copyOf(operations);
    }

    @Override
    public String toString() {
        return numero + " (" + solde + " " + devise + ")";
    }
}
