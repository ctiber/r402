package fr.iutvannes.banque.domaine;

public class PlafondDepasseException extends RuntimeException {
    public PlafondDepasseException(String numeroCompte) {
        super("Plafond du compte épargne dépassé : " + numeroCompte);
    }
}
