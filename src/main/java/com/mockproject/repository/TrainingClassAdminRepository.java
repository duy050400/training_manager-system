package com.mockproject.repository;

import com.mockproject.entity.TrainingClass;
import com.mockproject.entity.TrainingClassAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainingClassAdminRepository extends JpaRepository<TrainingClassAdmin, Long> {
    Optional<List<TrainingClassAdmin>> findByTrainingClassAndStatus(TrainingClass trainingClass, Boolean status);

    List<TrainingClassAdmin> findAllByTrainingClassId(Long id);
}
