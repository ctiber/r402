package fr.iutvannes.banque.web;

import fr.iutvannes.banque.domaine.CompteClotureException;
import fr.iutvannes.banque.domaine.PlafondDepasseException;
import fr.iutvannes.banque.domaine.SoldeInsuffisantException;
import fr.iutvannes.banque.service.CompteInconnuException;
import fr.iutvannes.banque.service.TauxIndisponibleException;
import fr.iutvannes.banque.service.VirementImpossibleException;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/** Traduit les exceptions metier en reponses HTTP : {"message": "..."}. */
@RestControllerAdvice
public class ErreursApi {

    @ExceptionHandler(CompteInconnuException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> introuvable(RuntimeException e) {
        return Map.of("message", e.getMessage());
    }

    @ExceptionHandler({SoldeInsuffisantException.class, CompteClotureException.class,
                       PlafondDepasseException.class, VirementImpossibleException.class})
    @ResponseStatus(HttpStatus.UNPROCESSABLE_CONTENT)
    public Map<String, String> refuse(RuntimeException e) {
        if (e instanceof SoldeInsuffisantException) {
            return Map.of("message", "Solde insuffisant");
        }
        return Map.of("message", e.getMessage());
    }

    @ExceptionHandler(TauxIndisponibleException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public Map<String, String> indisponible(RuntimeException e) {
        return Map.of("message", "Service de change indisponible, réessayez plus tard");
    }

    @ExceptionHandler({IllegalArgumentException.class, HttpMessageNotReadableException.class,
                       MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> invalide(Exception e) {
        return Map.of("message", "Demande invalide");
    }
}
