package fr.iutvannes.banque.e2e;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Tracing;
import java.nio.file.Paths;
import javax.sql.DataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

/**
 * Base des tests de bout en bout :
 *  - l'application complete demarre sur un port aleatoire, avec une base PostgreSQL de test (Testcontainers) ;
 *  - les donnees de src/test/resources/donnees/e2e.sql sont rechargees avant chaque test ;
 *  - un navigateur Chromium est partage par les tests de la classe, avec un contexte (onglet) neuf par test ;
 *  - une trace Playwright est enregistree pour chaque test dans target/traces/.
 * Mode visible et ralenti : ajouter -Dheaded=true (options de la VM dans IntelliJ, ou en ligne de commande).
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public abstract class BaseE2ETest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:18-alpine");

    static Playwright playwright;
    static Browser browser;

    protected BrowserContext context;
    protected Page page;

    @LocalServerPort
    protected int port;

    @Autowired
    private DataSource dataSource;

    protected String url() {
        return "http://localhost:" + port;
    }

    @BeforeAll
    static void lancerNavigateur() {
        boolean visible = Boolean.getBoolean("headed");
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
            .setHeadless(!visible)
            .setSlowMo(visible ? 300 : 0));
    }

    @BeforeEach
    void preparer() {
        new ResourceDatabasePopulator(new ClassPathResource("donnees/e2e.sql")).execute(dataSource);
        context = browser.newContext();
        context.tracing().start(new Tracing.StartOptions().setScreenshots(true).setSnapshots(true));
        page = context.newPage();
    }

    @AfterEach
    void terminer(TestInfo test) {
        String nom = getClass().getSimpleName() + "-" + test.getTestMethod().map(m -> m.getName()).orElse("test");
        context.tracing().stop(new Tracing.StopOptions().setPath(Paths.get("target/traces/" + nom + ".zip")));
        context.close();
    }

    @AfterAll
    static void fermerNavigateur() {
        playwright.close();
    }
}
