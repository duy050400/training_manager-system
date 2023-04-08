package com.mockproject.repository;

import com.mockproject.entity.Location;
import com.mockproject.entity.Tower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TowerRepository extends JpaRepository<Tower, Long> {
    Optional<Tower> findByIdAndStatus(Long id, Boolean status);

    List<Tower> findByLocationAndStatus(Location location, boolean status);
}
