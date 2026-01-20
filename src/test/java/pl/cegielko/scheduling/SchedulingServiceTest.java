package pl.cegielko.scheduling;

import org.junit.jupiter.api.Test;
import pl.cegielko.dao.MeetingRepository;
import pl.cegielko.dao.TimeSlotRepository;
import pl.cegielko.shared.Meeting;
import pl.cegielko.shared.ScheduledEvent;
import pl.cegielko.shared.TimeSlot;
import pl.cegielko.shared.User;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SchedulingServiceTest {

    private final OffsetDateTime now = OffsetDateTime.now();
    private final OffsetDateTime nowPlusOneHour = OffsetDateTime.now().plusHours(1);
    private final OffsetDateTime nowPlusTwoHours = OffsetDateTime.now().plusHours(2);
    private final OffsetDateTime nowPlusThreeHours = OffsetDateTime.now().plusHours(3);

    TimeSlotRepository timeSlotRepository = mock(TimeSlotRepository.class);
    MeetingRepository meetingRepository = mock(MeetingRepository.class);
    SchedulingService schedulingService = new SchedulingService(timeSlotRepository, meetingRepository);

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
        List<TimeSlot> timeSlots = List.of(new TimeSlot(now, nowPlusOneHour), new TimeSlot(nowPlusOneHour, nowPlusThreeHours));
        List<Meeting> meetings = List.of(new Meeting(new TimeSlot(nowPlusOneHour, nowPlusTwoHours)));

        // When
        List<TimeSlot> freeSlots = schedulingService.extractFreeSlots(timeSlots, meetings);

        // Then
        assertEquals(2, freeSlots.size());
        assertEquals(new TimeSlot(now, nowPlusOneHour), freeSlots.getFirst());
        assertEquals(new TimeSlot(nowPlusTwoHours, nowPlusThreeHours), freeSlots.get(1));
    }

    /*
        |---TS1---|          |---TS2---|
                      AND
                   |---M1---|
                       =
        |---TS1---||---M1---||---TS2---|
     */
    @Test
    public void joinTimeSlotsWithMeetings() {

        // Given
        List<TimeSlot> timeSlots = List.of(new TimeSlot(now, nowPlusOneHour), new TimeSlot(nowPlusTwoHours, nowPlusThreeHours));
        List<Meeting> meetings = List.of(new Meeting(new TimeSlot(nowPlusOneHour, nowPlusTwoHours)));

        // When
        List<ScheduledEvent<?>> aggregated = schedulingService.joinTimeSlotsWithMeetings(timeSlots, meetings);

        // Then
        assertEquals(3, aggregated.size());
        assertEquals(new TimeSlot(now, nowPlusOneHour), aggregated.getFirst().getTimeSlot());
        assertEquals(new TimeSlot(nowPlusOneHour, nowPlusTwoHours), aggregated.get(1).getTimeSlot());
        assertEquals(new TimeSlot(nowPlusTwoHours, nowPlusThreeHours), aggregated.get(2).getTimeSlot());
    }

    /*
                   |----John-----|
        |----------Mark------|
    */
    @Test
    public void onePossibleTimeSlot() {
        // Given
        User John = new User();
        John.addAvailabilitySlots(List.of(
                new TimeSlot(nowPlusOneHour, nowPlusThreeHours)
        ));

        User Mark = new User();
        Mark.addAvailabilitySlots(List.of(
                new TimeSlot(now, nowPlusTwoHours)
        ));

        // When
        List<TimeSlot> possibleTimeSlots = schedulingService.getPossibleSlotsForUsers(List.of(John, Mark));

        // Then
        assertEquals(1, possibleTimeSlots.size());
        assertEquals(new TimeSlot(nowPlusOneHour, nowPlusTwoHours), possibleTimeSlots.getFirst());
    }

    /*
                      |----John-----|
        |---Mark---|
    */
    @Test
    public void noPossibleTimeSlots() {
        // Given
        User John = new User();
        John.addAvailabilitySlots(List.of(
                new TimeSlot(nowPlusTwoHours, nowPlusThreeHours)
        ));

        User Mark = new User();
        Mark.addAvailabilitySlots(List.of(
                new TimeSlot(now, nowPlusOneHour)
        ));

        // When
        List<TimeSlot> possibleTimeSlots = schedulingService.getPossibleSlotsForUsers(List.of(John, Mark));

        // Then
        assertEquals(0, possibleTimeSlots.size());
    }

    /*
        |---------||-------------|
                 MINUS
                   |--------|
                   =
        |---------|          |---|
    */
    @Test
    public void getFreeTimeSlotsForUser() {
        // Given
        UUID userId = UUID.randomUUID();
        when(timeSlotRepository.findTimeSlotsByUserId(any(UUID.class))).thenReturn(List.of(new TimeSlot(now, nowPlusOneHour), new TimeSlot(nowPlusOneHour, nowPlusThreeHours)));
        when(meetingRepository.findMeetingsByUserId(any(UUID.class))).thenReturn(List.of(new Meeting(new TimeSlot(nowPlusOneHour, nowPlusTwoHours))));

        // When
        List<TimeSlot> freeTimeSlots = schedulingService.getFreeTimeSlots(userId);

        // Then
        assertEquals(2, freeTimeSlots.size());
        assertEquals(new TimeSlot(now, nowPlusOneHour), freeTimeSlots.getFirst());
        assertEquals(new TimeSlot(nowPlusTwoHours, nowPlusThreeHours), freeTimeSlots.get(1));
    }

    @Test
    public void getAggregatedViewForUser() {
        // Given
        UUID userId = UUID.randomUUID();
        when(timeSlotRepository.findTimeSlotsByUserId(any(UUID.class))).thenReturn(List.of(new TimeSlot(now, nowPlusOneHour), new TimeSlot(nowPlusOneHour, nowPlusThreeHours)));
        when(meetingRepository.findMeetingsByUserId(any(UUID.class))).thenReturn(List.of(new Meeting(new TimeSlot(nowPlusOneHour, nowPlusTwoHours))));

        // When
        List<ScheduledEvent<?>> scheduledEvents = schedulingService.getAggregatedViewForUser(userId);

        // Then
        assertEquals(3, scheduledEvents.size());
        assertEquals(new TimeSlot(now, nowPlusOneHour), scheduledEvents.getFirst().getTimeSlot());
        assertEquals(new TimeSlot(nowPlusOneHour, nowPlusTwoHours), scheduledEvents.get(1).getTimeSlot());
        assertEquals(new TimeSlot(nowPlusTwoHours, nowPlusThreeHours), scheduledEvents.get(2).getTimeSlot());
    }
}
