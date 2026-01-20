package pl.cegielko.overlapping;

import pl.cegielko.shared.Meeting;
import pl.cegielko.shared.TimeSlot;
import pl.cegielko.sweepline.SweepLineAlgorithm;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static pl.cegielko.sweepline.SweepLineAlgorithm.*;

public class OverlappingFacade {

    private final SweepLineAlgorithm sweepLineAlgorithm = new SweepLineAlgorithm();

    public static final Comparator<Point<OffsetDateTime>> ALLOW_TO_JOIN_TIME_SLOTS_COMPARATOR = (p1, p2) -> {
        if (p1.getValue().equals(p2.getValue()))
            return p1 instanceof StartingPoint<?> ? -1 : 1;
        else
            return p1.getValue().isBefore(p2.getValue()) ? -1 : 1;
    };

    public static final Comparator<Point<OffsetDateTime>> DO_NOT_ALLOW_TO_JOIN_TIME_SLOTS_COMPARATOR = (p1, p2) -> {
        if (p1.getValue().equals(p2.getValue()))
            return p1 instanceof StartingPoint<?> ? 1 : -1;
        else
            return p1.getValue().isBefore(p2.getValue()) ? -1 : 1;
    };

    public List<TimeSlot> getOverlappingTimeSlots(List<List<TimeSlot>> timeSlots) {
        return getOverlappingTimeSlots(timeSlots, ALLOW_TO_JOIN_TIME_SLOTS_COMPARATOR);
    }

    public List<TimeSlot> getOverlappingTimeSlots(List<List<TimeSlot>> timeSlots, Comparator<Point<OffsetDateTime>> comparator) {
        final int numberOfPeople = timeSlots.size();
        List<Point<OffsetDateTime>> sortedPoints = timeSlots
                .stream()
                .flatMap(List::stream)
                .map(timeSlot -> List.of(new StartingPoint<>(timeSlot.getStart()), new FinishingPoint<>(timeSlot.getFinish())))
                .flatMap(List::stream)
                .sorted(comparator)
                .toList();
        List<Point<?>> points = new ArrayList<>(sortedPoints);
        List<List<Point<?>>> overlapping = sweepLineAlgorithm.getOverlapping(points.stream().toList(), numberOfPeople);
        return overlapping.stream().map(overlappingPoints -> new TimeSlot((OffsetDateTime) overlappingPoints.getFirst().getValue(), (OffsetDateTime) overlappingPoints.get(1).getValue())).toList();
    }

    public List<TimeSlot> getTimeSlotsWithoutMeetings(List<TimeSlot> timeSlots, List<Meeting> meetings) {

        List<Point<OffsetDateTime>> sortedPoints = Stream.concat(
                timeSlots.stream()
                        .map(timeSlot -> List.of(new StartingPoint<>(timeSlot.getStart()), new FinishingPoint<>(timeSlot.getFinish())))
                        .flatMap(List::stream),
                meetings.stream()
                        .map(meeting -> List.of(new FinishingPoint<>(meeting.getTimeSlot().getStart()), new StartingPoint<>(meeting.getTimeSlot().getFinish())))
                        .flatMap(List::stream)
        ).sorted(DO_NOT_ALLOW_TO_JOIN_TIME_SLOTS_COMPARATOR).toList();

        ArrayList<Point<?>> points = new ArrayList<>(sortedPoints);
        List<List<Point<?>>> overlapping = sweepLineAlgorithm.getOverlapping(points.stream().toList(), 1);
        return overlapping.stream().map(overlappingPoints -> new TimeSlot((OffsetDateTime) overlappingPoints.getFirst().getValue(), (OffsetDateTime) overlappingPoints.get(1).getValue())).toList();
    }
}
