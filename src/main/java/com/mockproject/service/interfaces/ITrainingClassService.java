package com.mockproject.service.interfaces;

import com.mockproject.dto.TrainingClassDTO;
import com.mockproject.entity.TrainingClass;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface ITrainingClassService {
    List<TrainingClass> findAllByListClassSchedulesDate(LocalDate date);

    List<TrainingClass> findAllBySpecification(Specification specification);

    List<TrainingClass> findAllBySearchTextAndDate(String searchText,LocalDate date);

    List<TrainingClass> findAllBySearchTextAndWeek(String searchText, LocalDate startDate,LocalDate endDate);

    Page<TrainingClassDTO> getListClass(boolean status,
                                        List<Long> locationId, LocalDate fromDate, LocalDate toDate,
                                        List<Integer> period, String isOnline, List<String> state, List<Long> attendeeId, Long fsu,
                                        Long trainerId, List<String> search, String[] sort, Optional<Integer> page, Optional<Integer> row);

    List<TrainingClassDTO> getAllClass();

    Long create(TrainingClassDTO trainingClassDTO);

    TrainingClassDTO getAllDetails(Long id);

    boolean deleteTrainingClass(Long id);

    boolean duplicateClass(Long id);

}
