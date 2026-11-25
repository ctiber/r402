package fr.iutvannes.banque.web;

import fr.iutvannes.banque.service.VirementService;
import java.math.BigDecimal;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/virements")
public class VirementController {

    public record DemandeVirement(String source, String cible, BigDecimal montant) { }

    private final VirementService virements;

    public VirementController(VirementService virements) {
        this.virements = virements;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> virer(@RequestBody DemandeVirement demande) {
        virements.virer(demande.source(), demande.cible(), demande.montant());
        return Map.of("message", "Virement effectué");
    }
}
