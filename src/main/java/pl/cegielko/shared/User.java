package pl.cegielko.shared;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
@Getter
@EqualsAndHashCode(of = {"name", "email"})
@ToString
public class User {

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
