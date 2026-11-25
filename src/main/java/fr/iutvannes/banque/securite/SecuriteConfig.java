package fr.iutvannes.banque.securite;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

@Configuration
public class SecuriteConfig {

    @Bean
    SecurityFilterChain securite(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(acces -> acces
                .requestMatchers("/connexion.html", "/css/**", "/actuator/health").permitAll()
                .anyRequest().authenticated())
            .formLogin(formulaire -> formulaire
                .loginPage("/connexion.html")
                .loginProcessingUrl("/connexion")
                .defaultSuccessUrl("/", true)
                .failureUrl("/connexion.html?erreur")
                .permitAll())
            .logout(deconnexion -> deconnexion.logoutSuccessUrl("/connexion.html?deconnecte"))
            // L'API repond 401 (et non une redirection vers la page de connexion) si l'on n'est pas connecte
            .exceptionHandling(erreurs -> erreurs.defaultAuthenticationEntryPointFor(
                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                PathPatternRequestMatcher.withDefaults().matcher("/api/**")))
            // Jeton CSRF lisible par le JavaScript du front (cookie XSRF-TOKEN, en-tete X-XSRF-TOKEN)
            .csrf(csrf -> csrf.spa());
        return http.build();
    }

    /**
     * Utilisateurs de demonstration, en memoire. Mots de passe en clair ({noop}) :
     * acceptable pour un projet d'enseignement, jamais pour une vraie application.
     */
    @Bean
    UserDetailsService comptesUtilisateurs() {
        return new InMemoryUserDetailsManager(
            User.withUsername("alice").password("{noop}alice123").roles("CLIENT").build(),
            User.withUsername("bruno").password("{noop}bruno123").roles("CLIENT").build());
    }
}
