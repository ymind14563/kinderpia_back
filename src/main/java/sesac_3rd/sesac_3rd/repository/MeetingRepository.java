package sesac_3rd.sesac_3rd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sesac_3rd.sesac_3rd.entity.Meeting;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {

}
