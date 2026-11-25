package fr.iutvannes.banque.service;

public class VirementImpossibleException extends RuntimeException {
    public VirementImpossibleException(String message) {
        super(message);
    }

    public VirementImpossibleException(String message, Throwable cause) {
        super(message, cause);
    }
}
