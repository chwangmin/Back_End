package baby.lignin.auth.repository;

import baby.lignin.auth.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    Optional<MemberEntity> findByCertificationIdAndDeletedFalse(Long certificationId);

}
