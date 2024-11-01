package sesac_3rd.sesac_3rd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sesac_3rd.sesac_3rd.entity.ReportReason;

import java.util.Optional;

public interface ReportReasonRepository extends JpaRepository<ReportReason, Long> {
}
