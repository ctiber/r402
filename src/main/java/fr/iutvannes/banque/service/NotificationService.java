package fr.iutvannes.banque.service;

/** Envoi d'un message au titulaire d'un compte (SMS ou e-mail, selon ses preferences). */
public interface NotificationService {
    /** @throws NotificationException si l'envoi echoue */
    void notifier(String numeroCompte, String message);
}
