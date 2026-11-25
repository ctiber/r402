package fr.iutvannes.banque.doublures;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

/** Une horloge dont on choisit la date (a 10 h, heure de Paris). */
public class HorlogeModifiable extends Clock {

    private static final ZoneId PARIS = ZoneId.of("Europe/Paris");
    private Instant instant;

    public HorlogeModifiable(LocalDate date) {
        fixerDate(date);
    }

    public void fixerDate(LocalDate date) {
        this.instant = date.atTime(LocalTime.of(10, 0)).atZone(PARIS).toInstant();
    }

    @Override
    public ZoneId getZone() {
        return PARIS;
    }

    @Override
    public Clock withZone(ZoneId zone) {
        return Clock.fixed(instant, zone);
    }

    @Override
    public Instant instant() {
        return instant;
    }
}
