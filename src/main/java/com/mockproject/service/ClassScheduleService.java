package com.mockproject.service;

import com.mockproject.dto.ClassScheduleDTO;
import com.mockproject.dto.TrainingClassFilterRequestDTO;
import com.mockproject.dto.TrainingClassFilterResponseDTO;
import com.mockproject.dto.UnitResponseDTO;
import com.mockproject.entity.ClassSchedule;
import com.mockproject.entity.Syllabus;
import com.mockproject.entity.TrainingClass;
import com.mockproject.entity.TrainingProgramSyllabus;
import com.mockproject.mapper.ClassScheduleMapper;
import com.mockproject.mapper.TrainingClassFilterMap;
import com.mockproject.repository.ClassScheduleRepository;
import com.mockproject.repository.TrainingClassRepository;
import com.mockproject.service.interfaces.IClassScheduleService;
import com.mockproject.specification.TrainingClassSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ClassScheduleService implements IClassScheduleService {

    private final ClassScheduleRepository repository;

    private final TrainingClassRepository trainingClassRepository;

    private final TrainingClassService trainingClassService;

    private final TrainingClassUnitInformationService trainingClassUnitInformationService;

    private final TrainingClassFilterMap trainingClassFilterMap;

    @Override
    public List<ClassScheduleDTO> listAll() {
        return repository.findAll().stream().map(ClassScheduleMapper.INSTANCE::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<ClassSchedule> listEntity() {
        return repository.findAll();
    }

    @Override
    public ClassSchedule save(ClassScheduleDTO dto) {
        return repository.save(ClassScheduleMapper.INSTANCE.toEntity(dto));
    }

    @Override
    public boolean saveClassScheduleForTrainingClass(List<LocalDate> listDate, Long tcId) {
//        Long count = listDto.stream().map(p -> repository.save(ClassScheduleMapper.INSTANCE.toEntity(p))).filter(Objects::nonNull).count();
        TrainingClass tc = new TrainingClass();
        tc.setId(tcId);
        List<ClassSchedule> list = listDate.stream().map(p -> new ClassSchedule(null, p, true, tc)).toList();
        List<ClassSchedule> result = repository.saveAll(list);
        return !result.isEmpty();
    }

    @Override
    public List<ClassScheduleDTO> getClassScheduleByTrainingClassId(Long id) {
        TrainingClass tc = trainingClassRepository.findByIdAndStatus(id, true).orElseThrow();
        List<ClassSchedule> schedules = repository.findByTrainingClassAndStatusOrderByDateAsc(tc, true).orElseThrow();
        return schedules.stream().map(ClassScheduleMapper.INSTANCE::toDTO).toList();
    }

    @Override
    public Long countDayBefore(LocalDate date, Long id) {
        return repository.countAllByDateBeforeAndTrainingClassId(date, id);
    }

    @Override
    public List<TrainingClassFilterResponseDTO> getTrainingClassByDay(TrainingClassFilterRequestDTO filterRequestDTO) {
        return trainingClassService.findAllBySpecification(TrainingClassSpecification.findByFilterDate(filterRequestDTO))
                .stream().distinct().map(trainingClass -> getTrainingClassDetail(trainingClass, filterRequestDTO.getNowDate())).collect(Collectors.toList());
    }

    @Override
    public List<TrainingClassFilterResponseDTO> getTrainingClassByWeek(TrainingClassFilterRequestDTO filterRequestDTO) {
        var trainingClassFiltered = trainingClassService.findAllBySpecification(TrainingClassSpecification.findByFilterWeek(filterRequestDTO));
        List<TrainingClassFilterResponseDTO> result = new ArrayList<>();
        trainingClassFiltered.stream().distinct().forEach(trainingClass -> {
            trainingClass.getListClassSchedules().stream().forEach(classSchedule -> {
                if (classSchedule.getDate().isAfter(filterRequestDTO.getStartDate().minusDays(1)) && classSchedule.getDate().isBefore(filterRequestDTO.getEndDate().plusDays(1)))
                    result.add(getTrainingClassDetail(trainingClass, classSchedule.getDate()));
            });
        });
        return result;
    }

    @Override
    public List<TrainingClassFilterResponseDTO> searchTrainingClassInDate(List<String> searchText, LocalDate date) {
        var checkSize = searchText.isEmpty();

        var Result = trainingClassService.findAllBySearchTextAndDate("%" + (checkSize ? "" : searchText.get(0)) + "%", date);
        if (!checkSize) {
            List<String> lowerCaseSearchTerms = searchText.stream()
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());
            Result = searchByText(Result, lowerCaseSearchTerms);
        }

        return Result.stream().map(trainingClass -> getTrainingClassDetail(trainingClass, date)).collect(Collectors.toList());
    }

    @Override
    public List<TrainingClassFilterResponseDTO> searchTrainingClassInWeek(List<String> searchText, LocalDate startDate, LocalDate endDate) {
        var checkSize = searchText.isEmpty();
        List<TrainingClassFilterResponseDTO> result = new ArrayList<>();
        var trainingClassByWeek = trainingClassService.findAllBySearchTextAndWeek("%" + (checkSize ? "" : searchText.get(0)) + "%", startDate, endDate);

        if (!checkSize) {
            List<String> lowerCaseSearchTerms = searchText.stream()
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());
            trainingClassByWeek = searchByText(trainingClassByWeek, lowerCaseSearchTerms);
        }
        trainingClassByWeek.stream().forEach(trainingClass -> {
            trainingClass.getListClassSchedules().stream().forEach(classSchedule -> {
                if (classSchedule.getDate().isAfter(startDate.minusDays(2)) && classSchedule.getDate().isBefore(endDate.plusDays(1)))
                    result.add(getTrainingClassDetail(trainingClass, classSchedule.getDate()));
            });
        });
        return result;
    }

    private List<TrainingClass> searchByText(List<TrainingClass> trainingClasses, List<String> searchText) {
        var filteredResult = new ArrayList<TrainingClass>();
        for (var text : searchText) {
            trainingClasses.stream()
                    .filter(Class -> Class.getClassCode().toLowerCase().contains(text)
                            || Class.getFsu().getFsuName().toLowerCase().contains(text)
                            || Class.getListTrainingClassAdmins().stream().anyMatch(trainingClassAdmin ->
                            trainingClassAdmin.getAdmin().getFullName().toLowerCase().contains(text))
                            || Class.getAttendee().getAttendeeName().toLowerCase().contains(text)
                            || Class.getListTrainingClassUnitInformations().stream().anyMatch(unit ->
                            unit.getTrainer().getFullName().toLowerCase().contains(text))
                            || Class.getLocation().getLocationName().toLowerCase().contains(text))
                    .forEach(filteredResult::add);
            trainingClasses = filteredResult;
            filteredResult = new ArrayList<>();
        }
        return trainingClasses;
    }

    @Override
    public TrainingClassFilterResponseDTO getTrainingClassDetail(TrainingClass trainingClass, LocalDate date) {
        var learnedDay = repository.countAllByDateBeforeAndTrainingClassId(date, trainingClass.getId()) + 1;
        var durationDay = learnedDay + "/" + trainingClass.getDay();
        var syllabusesList = trainingClass.getTrainingProgram().getListTrainingProgramSyllabuses()
                .stream()
                .map(TrainingProgramSyllabus::getSyllabus)
                .collect(Collectors.toList());
        List<UnitResponseDTO> unit = new ArrayList<>();
        //check syllabus to get unit
        for (Syllabus syllabus : syllabusesList) {
            if (learnedDay > syllabus.getDay()) {
                learnedDay -= syllabus.getDay();
            } else {
                Long finalLearnedDay = learnedDay;
                unit = syllabus.getListSessions()
                        .stream().filter(session -> session.getSessionNumber() == finalLearnedDay)
                        .flatMap(session -> session.getListUnit().stream())
                        .map(Unit -> new UnitResponseDTO(Unit.getUnitTitle(), Unit.getUnitNumber())
                        ).collect(Collectors.toList());
                break;
            }
        }

        var trainerName = trainingClassUnitInformationService
                .findAllByTrainingClassId(trainingClass.getId())
                .stream()
                .map(trainingClassUnitInformation -> trainingClassUnitInformation.getTrainer().getFullName())
                .collect(Collectors.toList());

        return trainingClassFilterMap.toTrainingClassFilterResponseDTO(
                trainingClass,
                trainerName,
                durationDay,
                date,
                unit);
    }
}
