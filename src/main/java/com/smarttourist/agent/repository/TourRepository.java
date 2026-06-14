package com.smarttourist.agent.repository;

import com.smarttourist.agent.model.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TourRepository extends JpaRepository<Tour, Long> {
    List<Tour> findByUserId(Long userId);
    List<Tour> findByUserIdAndStatus(Long userId, String status);
}
