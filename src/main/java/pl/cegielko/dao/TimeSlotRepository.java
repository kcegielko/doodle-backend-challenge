package pl.cegielko.dao;

import pl.cegielko.shared.TimeSlot;

import java.util.List;
import java.util.UUID;

public interface TimeSlotRepository {
    List<TimeSlot> findTimeSlotsByUserId(UUID userId);
}
