package fr.iutvannes.banque.service;

public class TauxIndisponibleException extends RuntimeException {
    public TauxIndisponibleException(String message) {
        super(message);
    }
}
