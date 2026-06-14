package com.smarttourist.agent.repository;

import com.smarttourist.agent.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByHotelIdAndIsAvailable(Long hotelId, Boolean isAvailable);
    List<Room> findByIsAvailableAndCapacityGreaterThanEqual(Boolean isAvailable, Integer capacity);
}
