package pl.cegielko.shared;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@EqualsAndHashCode(of = {"start", "finish"})
@ToString
public class TimeSlot {

    private UUID id;
    private final OffsetDateTime start;
    private final OffsetDateTime finish;
    private User user;

    public TimeSlot(OffsetDateTime start, OffsetDateTime finish) {
        this.start = start;
        this.finish = finish;
    }
}
