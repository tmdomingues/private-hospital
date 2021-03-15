package io.cloudmobility.tiago.utils;


import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import io.cloudmobility.tiago.domain.dto.CalendarEventDto;

@Component
public class CalendarManager {

    private static final int WORKDAY_INIT = 9;
    private static final int WORKDAY_END = 19;

    /**
     * asserts that range selected by user is within week range.
     * @param event end of period to be checked
     * @return validity if under 1 week
     */
    public boolean checkIfPeriodIsWithinWeekRange(final CalendarEventDto event) {

        final var period = Period.between(
                LocalDate.of(event.getFrom().getYear(), event.getFrom().getMonth(), event.getFrom().getDayOfMonth()),
                LocalDate.of(event.getTo().getYear(), event.getTo().getMonth(), event.getTo().getDayOfMonth())
        );

        final var WEEK_IN_DAYS = 7;

        return period.getDays() <= WEEK_IN_DAYS && period.getMonths() == 0;
    }

    /**
     * extract periods of time to 1h timeslots.
     * @param event the period/duration to be processed
     * @return the list of 1 hour slots
     */
    public Set<CalendarEventDto> extractTimeslots(final CalendarEventDto event) {

        final var results = new LinkedHashSet<CalendarEventDto>();

        final var duration = Duration.between(event.getFrom(), event.getTo());
        var temporalBaseline = event.getFrom();

        for (int i = 0; i < duration.toHours(); ++i) {
            if (temporalBaseline.getHour() >= WORKDAY_INIT && temporalBaseline.getHour() < WORKDAY_END) {
                results.add(new CalendarEventDto(temporalBaseline, temporalBaseline.plusHours(1)));
            }
            temporalBaseline = temporalBaseline.plusHours(1);
        }
        return results;
    }
}
