package com.example.evisitorai.repository;

import com.example.evisitorai.entity.Tourist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TouristRepository extends JpaRepository<Tourist, UUID> {

    List<Tourist> findAllByOrderByCreatedAtDesc();
}
