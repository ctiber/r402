package fr.iutvannes.banque.domaine;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Une operation (credit, debit, frais) sur un compte. */
@Entity
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Compte compte;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeOperation type;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal montant;

    @Column(nullable = false)
    private LocalDateTime date;

    protected Operation() {
        // pour JPA
    }

    public Operation(Compte compte, TypeOperation type, BigDecimal montant, LocalDateTime date) {
        this.compte = compte;
        this.type = type;
        this.montant = montant;
        this.date = date;
    }

    public Long getId() { return id; }
    public Compte getCompte() { return compte; }
    public TypeOperation getType() { return type; }
    public BigDecimal getMontant() { return montant; }
    public LocalDateTime getDate() { return date; }

    @Override
    public String toString() {
        return type + " " + montant + " (" + date + ")";
    }
}
