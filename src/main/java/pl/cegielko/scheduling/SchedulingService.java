package pl.cegielko.scheduling;

import lombok.RequiredArgsConstructor;
import pl.cegielko.dao.MeetingRepository;
import pl.cegielko.dao.TimeSlotRepository;
import pl.cegielko.shared.ScheduledEvent;
import pl.cegielko.shared.ScheduledEvent.*;
import pl.cegielko.overlapping.OverlappingFacade;
import pl.cegielko.shared.Meeting;
import pl.cegielko.shared.TimeSlot;
import pl.cegielko.shared.User;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class SchedulingService {

    public static final Comparator<ScheduledEvent<?>> SCHEDULED_EVENT_COMPARATOR = (event1, event2) -> {
        if (event1.getTimeSlot().equals(event2.getTimeSlot()))
            return 0;
        else
            return event1.getTimeSlot().getStart().isBefore(event2.getTimeSlot().getStart()) ? -1 : 1;
    };

    private final OverlappingFacade overlappingFacade = new OverlappingFacade();

    private final TimeSlotRepository timeSlotRepository;
    private final MeetingRepository meetingRepository;

    protected List<TimeSlot> extractFreeSlots(List<TimeSlot> timeSlots, List<Meeting> meetings) {
        return overlappingFacade.getTimeSlotsWithoutMeetings(timeSlots, meetings);
    }

    protected List<ScheduledEvent<?>> joinTimeSlotsWithMeetings(List<TimeSlot> timeSlots, List<Meeting> meetings) {
        return Stream.concat(
                timeSlots.stream().map(TimeSlotEvent::new),
                meetings.stream().map(MeetingEvent::new)
        ).sorted(SCHEDULED_EVENT_COMPARATOR).toList();
    }

    public List<TimeSlot> getPossibleSlotsForUsers(List<User> users) {
        List<List<TimeSlot>> timeSlots = users.stream().map(User::getAvailableTimeSlots).toList();
        return overlappingFacade.getOverlappingTimeSlots(timeSlots);
    }

    public List<TimeSlot> getFreeTimeSlots(UUID userId) {
        return extractFreeSlots(timeSlotRepository.findTimeSlotsByUserId(userId), meetingRepository.findMeetingsByUserId(userId));
    }

    public List<ScheduledEvent<?>> getAggregatedViewForUser(UUID userId) {
        List<TimeSlot> timeSlots = timeSlotRepository.findTimeSlotsByUserId(userId);
        List<Meeting> meetings = meetingRepository.findMeetingsByUserId(userId);
        List<TimeSlot> freeTimeSlots = extractFreeSlots(timeSlots, meetings);
        return joinTimeSlotsWithMeetings(freeTimeSlots, meetings);
    }
}
