package com.smarttourist.agent.repository;

import com.smarttourist.agent.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    List<Hotel> findByCategory(String category);
    List<Hotel> findByAddressContainingIgnoreCase(String query);
}
