package com.mockproject.repository;

import com.mockproject.entity.ClassSchedule;
import com.mockproject.entity.TrainingClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClassScheduleRepository extends JpaRepository<ClassSchedule, Long> {
    Optional<List<ClassSchedule>> findByTrainingClassAndStatusOrderByDateAsc(TrainingClass trainingClass, Boolean status);

    List<ClassSchedule> findAllByDateBetweenAndTrainingClassId(LocalDate StartDate, LocalDate endDate, Long id);

    Long countAllByDateBetweenAndTrainingClassId(LocalDate StartDate, LocalDate endDate, Long id);

    Long countAllByDateBeforeAndTrainingClassId(LocalDate date, Long id);

}
