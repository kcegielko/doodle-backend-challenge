package pl.cegielko.shared;

import lombok.*;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode
@ToString
@Getter
public class Meeting {

    private UUID id;
    private List<User> participants;
    private final TimeSlot timeSlot;

    public Meeting(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }
}
