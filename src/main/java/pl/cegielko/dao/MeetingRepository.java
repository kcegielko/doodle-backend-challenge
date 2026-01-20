package pl.cegielko.dao;

import pl.cegielko.shared.Meeting;

import java.util.List;
import java.util.UUID;

public interface MeetingRepository {
    List<Meeting> findMeetingsByUserId(UUID userId);
}
