package pl.cegielko.shared;

import lombok.*;

import java.util.List;

@EqualsAndHashCode
@ToString
@Getter
public class Meeting {

    private List<User> participants;
    private final TimeSlot timeSlot;

    public Meeting(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }
}
