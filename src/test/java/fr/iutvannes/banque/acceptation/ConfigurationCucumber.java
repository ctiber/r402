package fr.iutvannes.banque.acceptation;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.test.context.ContextConfiguration;

/**
 * Cucumber cree les classes d'etapes avec Spring. Ce contexte ne contient que ContexteBanque
 * (pas toute l'application) : une instance par scenario, partagee par toutes les classes d'etapes.
 */
@CucumberContextConfiguration
@ContextConfiguration(classes = ContexteBanque.class)
public class ConfigurationCucumber {
}
