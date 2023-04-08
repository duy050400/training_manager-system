package com.mockproject.repository;

import com.mockproject.entity.TrainingMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainingMaterialRepository extends JpaRepository<TrainingMaterial, Long> {
    List<TrainingMaterial> getListTrainingMaterialByUnitDetailId(Long id);

    Optional<TrainingMaterial> findByIdAndStatus(Long trainingMaterialId, boolean status);

    Optional<List<TrainingMaterial>> findAllByUnitDetailIdAndStatus(Long unitDetailId, boolean status);
}
