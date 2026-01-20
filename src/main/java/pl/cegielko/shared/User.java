package pl.cegielko.shared;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@EqualsAndHashCode(of = {"name", "email"})
@ToString
public class User {

    private UUID id;
    private String name;
    private String email;
    private List<TimeSlot> availableTimeSlots;
    private List<Meeting> meetings;

    public void addAvailabilitySlots(List<TimeSlot> timeSlots) {
        if (availableTimeSlots == null)
            availableTimeSlots = new ArrayList<>();
        this.availableTimeSlots.addAll(timeSlots);
    }
}
