package pl.cegielko.overlapping;

import org.junit.jupiter.api.Test;
import pl.cegielko.shared.Meeting;
import pl.cegielko.shared.TimeSlot;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OverlappingFacadeTest {

    private final OffsetDateTime now = OffsetDateTime.now();
    private final OffsetDateTime nowPlusOneHour = OffsetDateTime.now().plusHours(1);
    private final OffsetDateTime nowPlusTwoHours = OffsetDateTime.now().plusHours(2);
    private final OffsetDateTime nowPlusThreeHours = OffsetDateTime.now().plusHours(3);
    private final OffsetDateTime nowPlusFourHours = OffsetDateTime.now().plusHours(4);

    /*
                   |-------------|
                   AND
        |--------------------|
                    =
                   |---------|
    */
    @Test
    public void oneOverlappingTimeSlot() {
        // Given
        OverlappingFacade overlappingFacade = new OverlappingFacade();
        List<List<TimeSlot>> timeSlots = List.of(
                List.of(new TimeSlot(nowPlusOneHour, nowPlusThreeHours)),
                List.of(new TimeSlot(now, nowPlusTwoHours))
        );

        // When
        List<TimeSlot> overlappingTimeSlots = overlappingFacade.getOverlappingTimeSlots(timeSlots);

        // Then
        assertEquals(1, overlappingTimeSlots.size());
        assertEquals(new TimeSlot(nowPlusOneHour, nowPlusTwoHours), overlappingTimeSlots.getFirst());
    }

    /*
        |---------||-------------|
                  AND
              |----------|
                   =
              |----------|
    */
    @Test
    public void oneJoinedOverlappingTimeSlot() {
        // Given
        OverlappingFacade overlappingFacade = new OverlappingFacade();
        List<List<TimeSlot>> timeSlots = List.of(
                List.of(new TimeSlot(now, nowPlusTwoHours), new TimeSlot(nowPlusTwoHours, nowPlusFourHours)),
                List.of(new TimeSlot(nowPlusOneHour, nowPlusThreeHours))
        );

        // When
        List<TimeSlot> overlappingTimeSlots = overlappingFacade.getOverlappingTimeSlots(timeSlots, OverlappingFacade.ALLOW_TO_JOIN_TIME_SLOTS_COMPARATOR);

        // Then
        assertEquals(1, overlappingTimeSlots.size());
        assertEquals(new TimeSlot(nowPlusOneHour, nowPlusThreeHours), overlappingTimeSlots.getFirst());
    }

    /*
        |---------||-------------|
                  AND
              |----------|
                   =
              |---||-----|
    */
    @Test
    public void twoOverlappingTimeSlots() {
        // Given
        OverlappingFacade overlappingFacade = new OverlappingFacade();
        List<List<TimeSlot>> timeSlots = List.of(
                List.of(new TimeSlot(now, nowPlusTwoHours), new TimeSlot(nowPlusTwoHours, nowPlusFourHours)),
                List.of(new TimeSlot(nowPlusOneHour, nowPlusThreeHours))
        );

        // When
        List<TimeSlot> overlappingTimeSlots = overlappingFacade.getOverlappingTimeSlots(timeSlots, OverlappingFacade.DO_NOT_ALLOW_TO_JOIN_TIME_SLOTS_COMPARATOR);

        // Then
        assertEquals(2, overlappingTimeSlots.size());
        assertEquals(new TimeSlot(nowPlusOneHour, nowPlusTwoHours), overlappingTimeSlots.getFirst());
        assertEquals(new TimeSlot(nowPlusTwoHours, nowPlusThreeHours), overlappingTimeSlots.get(1));
    }

    /*
        |---------||-------------|
                 MINUS
                   |--------|
                   =
        |---------|          |---|
    */
    @Test
    public void extractFreeTimeSlots() {
        // Given
        OverlappingFacade overlappingFacade = new OverlappingFacade();
        List<TimeSlot> timeSlots = List.of(new TimeSlot(now, nowPlusOneHour), new TimeSlot(nowPlusOneHour, nowPlusThreeHours));
        List<Meeting> meetings = List.of(new Meeting(new TimeSlot(nowPlusOneHour, nowPlusTwoHours)));

        // When
        List<TimeSlot> freeSlots = overlappingFacade.getTimeSlotsWithoutMeetings(timeSlots, meetings);

        // Then
        assertEquals(2, freeSlots.size());
        assertEquals(new TimeSlot(now, nowPlusOneHour), freeSlots.getFirst());
        assertEquals(new TimeSlot(nowPlusTwoHours, nowPlusThreeHours), freeSlots.get(1));
    }
}
