package com.smarttourist.agent.repository;

import com.smarttourist.agent.model.GovernmentIdDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface GovernmentIdDetailRepository extends JpaRepository<GovernmentIdDetail, Long> {
    Optional<GovernmentIdDetail> findByUserId(Long userId);
    Optional<GovernmentIdDetail> findByIdNumber(String idNumber);
}
