package fr.iutvannes.banque.service;

public class CompteInconnuException extends RuntimeException {
    public CompteInconnuException(String message) {
        super(message);
    }
}
