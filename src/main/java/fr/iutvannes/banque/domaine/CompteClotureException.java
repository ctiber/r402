package fr.iutvannes.banque.domaine;

public class CompteClotureException extends RuntimeException {
    public CompteClotureException(String numeroCompte) {
        super("Le compte " + numeroCompte + " est clôturé");
    }
}
