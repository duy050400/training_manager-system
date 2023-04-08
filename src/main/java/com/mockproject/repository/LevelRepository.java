package com.mockproject.repository;

import com.mockproject.entity.Level;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LevelRepository extends JpaRepository<Level, Long> {
    Optional<Level> getLevelById(Long id);
    Optional<Level> getLevelByLevelCode(String levelCode);
}
