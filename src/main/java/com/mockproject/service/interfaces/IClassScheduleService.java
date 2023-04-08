package com.mockproject.service.interfaces;

import com.mockproject.dto.ClassScheduleDTO;
import com.mockproject.dto.TrainingClassFilterRequestDTO;
import com.mockproject.dto.TrainingClassFilterResponseDTO;
import com.mockproject.entity.ClassSchedule;
import com.mockproject.entity.TrainingClass;

import java.time.LocalDate;
import java.util.List;

public interface IClassScheduleService {

    List<ClassScheduleDTO> listAll();

    boolean saveClassScheduleForTrainingClass(List<LocalDate> listDate, Long tcId);

    List<ClassScheduleDTO> getClassScheduleByTrainingClassId(Long id);

    List<ClassSchedule> listEntity();

    ClassSchedule save(ClassScheduleDTO dto);

    Long countDayBefore(LocalDate date, Long id);

    List<TrainingClassFilterResponseDTO> getTrainingClassByDay(TrainingClassFilterRequestDTO filterRequestDTO);

    List<TrainingClassFilterResponseDTO> getTrainingClassByWeek(TrainingClassFilterRequestDTO filterRequestDTO);

    TrainingClassFilterResponseDTO getTrainingClassDetail(TrainingClass trainingClass, LocalDate date);

    List<TrainingClassFilterResponseDTO> searchTrainingClassInDate(List<String> textSearch, LocalDate date);

    List<TrainingClassFilterResponseDTO> searchTrainingClassInWeek(List<String> textSearch, LocalDate startDate, LocalDate endDate);

}
