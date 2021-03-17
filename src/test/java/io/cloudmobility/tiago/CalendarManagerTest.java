package io.cloudmobility.tiago;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.cloudmobility.tiago.domain.dto.CalendarEventDto;
import io.cloudmobility.tiago.utils.CalendarManager;

@DisplayName("Calendar Manager Tests")
public class CalendarManagerTest {

    private final CalendarManager underTest = new CalendarManager();

    @Test
    @DisplayName("Given a certain period across 3 days extract 12 slots")
    public void fetchTimeSlotsWithinPeriod_multipleDays_returnlistWithSlotValues() {
        final var expectedSlots = 12;
        final var from = LocalDateTime.of(2021, 3, 22, 18, 0);
        final var to = LocalDateTime.of(2021, 3, 24, 10, 0);
        final var calendarEvent = new CalendarEventDto(from, to);

        /* Expectation for given range of 1 hour time slots: 12 slots
           [18-19] -> day 2021/03/22 (1 slot)
           [9-10, 10-11, 11-12, 12-13, 13-14, 14-15, 15-16, 16-17, 17-18, 18-19] (10 slots)-> day 2021/03/23
           [9-10] (1 slot)-> day 2021/03/24
        */
        final var timeSlotDtos = underTest.extractTimeslots(calendarEvent);

        assertThat(timeSlotDtos, notNullValue());
        assertThat(timeSlotDtos.size(), is(expectedSlots));
    }

    @Test
    @DisplayName("Given a certain range spanning 3h fetch 3 slots of 1h")
    public void extractTimeslots_slotsBiggerThanOneHourInTheSameDay_returnListOfOneHourSlots() {
        final var multipleHourSlotsInSameDay = getCalendarEventInferiorToAWorkday();
        final var eventList = underTest.extractTimeslots(multipleHourSlotsInSameDay);

        final var EXPECTED_SLOTS = 4;
        assertThat(eventList, notNullValue());
        assertThat(eventList.size(), is(EXPECTED_SLOTS));
    }

    @Test
    @DisplayName("Given a certain range spanning different days fetch 14 slots of 1h")
    public void extractTimeslots_slotsBiggerThanOneHourAcrossDays_returnListOfOneHourSlots() {
        final var EXPECTED_SLOTS = 14;
        final var multipleHourSlotsAcrossDays = getCalendarEventSuperiorToAWorkday();
        final var eventList = underTest.extractTimeslots(multipleHourSlotsAcrossDays);

        assertThat(eventList, notNullValue());
        assertThat(eventList.size(), is(EXPECTED_SLOTS));
    }

    @Test
    @DisplayName("Given a certain range verify is under 1 week")
    public void checkIfPeriodIsWithinWeekRange_periodBelow1Week_success() {
        final var multipleHourSlotsAcrossDays = getCalendarEventSuperiorToAWorkday();
        final var isWithinWeekRange = underTest.checkIfPeriodIsWithinWeekRange(multipleHourSlotsAcrossDays);

        assertThat(isWithinWeekRange, is(true));
    }

    @Test
    @DisplayName("Given a certain range verify is above 1 week")
    public void checkIfPeriodIsWithinWeekRange_periodAbove1Week_fail() {
        final var multipleHourSlotsAcrossDays = getCalendarEventSuperiorToAWorkday();
        multipleHourSlotsAcrossDays.setTo(multipleHourSlotsAcrossDays.getTo().plusMonths(1));
        final var isWithinWeekRange = underTest.checkIfPeriodIsWithinWeekRange(multipleHourSlotsAcrossDays);

        assertThat(isWithinWeekRange, is(false));
    }

    // *********************************** Test Fixtures ******************************************/

    private CalendarEventDto getCalendarEventInferiorToAWorkday() {
        final var init = LocalDateTime.of(2021, 3, 22, 14, 0);
        final var end = LocalDateTime.of(2021, 3, 22, 18, 0);

        return new CalendarEventDto(init, end);
    }

    private CalendarEventDto getCalendarEventSuperiorToAWorkday() {
        final var init = LocalDateTime.of(2021, 3, 21, 14, 0);
        final var end = LocalDateTime.of(2021, 3, 22, 18, 0);

        return new CalendarEventDto(init, end);
    }

    private CalendarEventDto getCalendarEventSuperiorToAWeek() {
        final var init = LocalDateTime.of(2021, 2, 21, 14, 0);
        final var end = LocalDateTime.of(2021, 3, 22, 18, 0);

        return new CalendarEventDto(init, end);
    }
}
