package com.smarttourist.agent.repository;

import com.smarttourist.agent.model.Guide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GuideRepository extends JpaRepository<Guide, Long> {
    List<Guide> findByIsAvailable(Boolean isAvailable);
    List<Guide> findByCurrentLocationAndIsAvailable(String currentLocation, Boolean isAvailable);
}
