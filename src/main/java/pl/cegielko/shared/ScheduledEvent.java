package pl.cegielko.shared;

public abstract sealed class ScheduledEvent<T> permits ScheduledEvent.TimeSlotEvent, ScheduledEvent.MeetingEvent {
    private T value;

    public ScheduledEvent(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public abstract TimeSlot getTimeSlot();

    public static final class TimeSlotEvent extends ScheduledEvent<TimeSlot> {

        public TimeSlotEvent(TimeSlot value) {
            super(value);
        }

        @Override
        public TimeSlot getTimeSlot() {
            return getValue();
        }
    }

    public static final class MeetingEvent extends ScheduledEvent<Meeting> {

        public MeetingEvent(Meeting value) {
            super(value);
        }

        @Override
        public TimeSlot getTimeSlot() {
            return getValue().getTimeSlot();
        }
    }
}
