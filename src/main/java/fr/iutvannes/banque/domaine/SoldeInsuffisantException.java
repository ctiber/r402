package fr.iutvannes.banque.domaine;

public class SoldeInsuffisantException extends RuntimeException {
    private final String numeroCompte;

    public SoldeInsuffisantException(String numeroCompte) {
        super("Solde insuffisant sur le compte " + numeroCompte);
        this.numeroCompte = numeroCompte;
    }

    public String getNumeroCompte() {
        return numeroCompte;
    }
}
