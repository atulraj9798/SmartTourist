package com.smarttourist.agent.repository;

import com.smarttourist.agent.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByIsAvailable(Boolean isAvailable);
    List<Vehicle> findByTypeAndIsAvailable(String type, Boolean isAvailable);
}
