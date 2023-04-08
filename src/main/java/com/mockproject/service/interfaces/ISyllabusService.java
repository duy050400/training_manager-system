package com.mockproject.service.interfaces;

import com.mockproject.dto.SyllabusDTO;
import com.mockproject.entity.Syllabus;
import com.mockproject.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ISyllabusService {
    List<Syllabus> getAllSyllabusEntityById(List<Long> id);

    List<SyllabusDTO> listByTrainingProgramIdTrue(Long trainingProgramId);



    Page<SyllabusDTO> getListSyllabus(boolean status,
                                      LocalDate fromDate, LocalDate toDate,
                                      List<String> search, String[] sort, Optional<Integer> page, Optional<Integer> row);

    List<Long> getListSyllabusIdByOSD(String osd);

    SyllabusDTO getSyllabusById(Long syllabusId,boolean state, boolean status);

    boolean replace(SyllabusDTO syllabusDTO, boolean status);

    Long create(SyllabusDTO syllabus, User user);

    boolean deactivate(Long syllabusId, boolean status);

    Syllabus editSyllabus(SyllabusDTO syllabusDTO, boolean status) throws IOException;

    boolean deleteSyllabus(Long syllabusId, boolean status);

    SyllabusDTO getSyllabusById(Long id);

    SyllabusDTO readFileCsv(MultipartFile file, int condition, int handle) throws IOException;

    byte[] getTemplateCsvFile() throws IOException;

    Long duplicateSyllabus(Long syllabusId, boolean status, boolean state);

    List<Syllabus> liveSearch();
}

