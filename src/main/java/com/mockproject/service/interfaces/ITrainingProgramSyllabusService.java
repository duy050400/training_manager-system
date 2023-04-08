package com.mockproject.service.interfaces;

import com.mockproject.dto.TrainingProgramSyllabusDTO;

import java.util.List;

public interface ITrainingProgramSyllabusService {
    List<TrainingProgramSyllabusDTO> getTrainingProgramSyllabusListById(Long trainingProgramID);

    List<TrainingProgramSyllabusDTO> getAllSyllabusByTrainingProgramId(long trainProgramID, boolean status);

}
