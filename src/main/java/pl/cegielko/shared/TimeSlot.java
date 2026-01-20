package pl.cegielko.shared;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@EqualsAndHashCode(of = {"start", "finish"})
@ToString
public class TimeSlot {

    private final OffsetDateTime start;
    private final OffsetDateTime finish;

    public TimeSlot(OffsetDateTime start, OffsetDateTime finish) {
        this.start = start;
        this.finish = finish;
    }
}
