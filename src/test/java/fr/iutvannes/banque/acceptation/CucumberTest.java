package fr.iutvannes.banque.acceptation;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

/** Lance tous les scenarios de src/test/resources/features avec les etapes de ce paquetage. */
@Suite(failIfNoTests = false)   // aucun scenario au depart : la suite vide ne doit pas echouer
@IncludeEngines("cucumber")
@SelectPackages("features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "fr.iutvannes.banque.acceptation")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty, html:target/cucumber.html")
public class CucumberTest {
}
