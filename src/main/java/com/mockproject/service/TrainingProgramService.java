package com.mockproject.service;

import com.mockproject.dto.*;
import com.mockproject.entity.*;
import com.mockproject.exception.FileException;
import com.mockproject.exception.SyllabusException;
import com.mockproject.mapper.TrainingProgramMapper;
import com.mockproject.repository.TrainingProgramRepository;
import com.mockproject.service.interfaces.IFileService;
import com.mockproject.service.interfaces.ITrainingProgramService;
import com.mockproject.specification.TrainingProgramSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TrainingProgramService implements ITrainingProgramService {

    private final TrainingProgramRepository trainingProgramRepository;
    private final SyllabusService syllabusService;
    private final TrainingProgramSyllabusService trainingProgramSyllabusService;
    private final IFileService fileService;

    private final String PROGRAMID = "program id";
    private final String PROGRAMNAME = "program name";
    private final String ALLOW = "allow";
    private final String REPLACE = "replace";
    private final String SKIP = "skip";


    private int getDay(List<Syllabus> syllabusList) {
        return syllabusList.stream()
                .map(syllabus -> syllabus.getDay())
                .reduce(0, Integer::sum);
    }

    private BigDecimal getHour(List<Syllabus> syllabusList) {
        return syllabusList.stream()
                .map(syllabus -> syllabus.getHour())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<TrainingProgram> getAll() {
        return trainingProgramRepository.findAll();
    }

    @Override
    public List<TrainingProgram> getByName(String keyword) {
        return trainingProgramRepository.getTrainingProgramByNameContains(keyword);
    }

    @Override
    public Page<TrainingProgramDTO> findByNameContaining(Integer pageNo, Integer pageSize, String name, String name2) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<TrainingProgram> page = trainingProgramRepository.findAllByNameContainingOrCreatorFullNameContaining(pageable, name, name2);
        Page<TrainingProgramDTO> programDTOPage = page.map(TrainingProgramMapper.INSTANCE::toDTO);
        return programDTOPage;
    }

    @Override
    public Long countAll() {
        return trainingProgramRepository.count();
    }

    @Override
    public List<TrainingProgramDTO> getByCreatorFullname(String keyword) {
        return trainingProgramRepository.getAllByCreatorFullNameContains(keyword).stream().map(TrainingProgramMapper.INSTANCE::toDTO).collect(Collectors.toList());
    }

    @Override
    public Page<TrainingProgramDTO> searchByNameOrCreator(SearchTPDTO searchList, Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        boolean check = searchList.getSearch().isEmpty();
        Page<TrainingProgram> result = null;
        if (!check) {
//            List<String> lowerCaseSearchTerms = searchList.getSearch().stream()
//                    .map(String::toLowerCase)
//                    .collect(Collectors.toList());
//            result = doSearch(trainingProgramRepository.findAll(), lowerCaseSearchTerms);
            result = trainingProgramRepository.findAll(TrainingProgramSpecification.getTrainingProgramSpecification(searchList), pageable);
        }
        Page<TrainingProgramDTO> programDTOPage = result.map(TrainingProgramMapper.INSTANCE::toDTO);
        return programDTOPage;
    }

    @Override
    public boolean de_activeTrainingProgram(Long trainingProgramID) {
        Optional<TrainingProgram> trainingProgramModel = trainingProgramRepository.findById(trainingProgramID);
        if (trainingProgramModel.isEmpty()) {
            return false;
        } else {
            trainingProgramModel.map(trainingProgram -> {
                trainingProgram.setState(false);
                return trainingProgramRepository.save(trainingProgram);
            });
        }
        return true;
    }

    @Override
    public boolean activeTrainingProgram(Long trainingProgramID) {
        Optional<TrainingProgram> trainingProgramModel = trainingProgramRepository.findById(trainingProgramID);
        if (trainingProgramModel.isPresent()) {
            trainingProgramModel.map(trainingProgram -> {
                trainingProgram.setState(true);
                return trainingProgramRepository.save(trainingProgram);
            });
        } else {
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteTrainingProgram(Long trainingProgramID) {
        Optional<TrainingProgram> trainingProgramModel = trainingProgramRepository.findById(trainingProgramID);
        if (trainingProgramModel.isPresent()) {
            trainingProgramModel.map(trainingProgram -> {
                trainingProgram.setStatus(false);
                return trainingProgramRepository.save(trainingProgram);
            });
        } else {
            return false;
        }
        return true;
    }

    @Override
    public void restoreAllTrainingPrograms() {
        trainingProgramRepository.findAll().forEach(trainingProgram -> {
            trainingProgram.setStatus(true);
            trainingProgramRepository.save(trainingProgram);
        });
    }

    @Override
    public boolean duplicateProgram(Long trainingProgramID) {
        Optional<TrainingProgram> trainingProgramModel = trainingProgramRepository.findById(trainingProgramID);
        List<TrainingProgramSyllabusDTO> programSyllabusList = trainingProgramSyllabusService.getAllSyllabusByTrainingProgramId(trainingProgramID, true);
        List<Long> syllabusIDList = new ArrayList<>();
        CustomUserDetails CusUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = CusUserDetails.getUser();
        programSyllabusList.forEach(programSyllabus -> {
            syllabusIDList.add(programSyllabus.getSyllabusId());
        });
        List<Syllabus> syllabusList = syllabusService.getAllSyllabusEntityById(syllabusIDList);
        LocalDate nowDay = LocalDate.now();
//        int day = getDay(syllabusList);
        BigDecimal hour = getHour(syllabusList);
        //create trainingProgram
        if (trainingProgramModel.isPresent()) {
            TrainingProgram trainingProgram = TrainingProgram.builder()
                    .name(trainingProgramModel.get().getName())
                    .dateCreated(nowDay)
                    .lastDateModified(nowDay)
                    .day(trainingProgramModel.get().getDay())
                    .hour(hour)
                    .state(true)
                    .status(true)
                    .programId(trainingProgramModel.get().getProgramId())
                    .creator(user)
                    .lastModifier(user)
                    .build();
            // create programSyllabus
            List<TrainingProgramSyllabus> programSyllabus = syllabusList.stream()
                    .map(syllabus -> new TrainingProgramSyllabus(null, true, syllabus, trainingProgram))
                    .collect(Collectors.toList());
            // save to database
            trainingProgramRepository.save(trainingProgram);
            trainingProgramSyllabusService.saveAll(programSyllabus);
        }
        return trainingProgramModel.isPresent();
    }

    @Override
    public Optional<TrainingProgram> editProgram(Long trainingProgramID, String newTrainingProgramName) {
        return trainingProgramRepository.findById(trainingProgramID).map(trainingProgram -> {
            trainingProgram.setName(newTrainingProgramName);
            return trainingProgramRepository.save(trainingProgram);
        });
    }


    @Override
    public List<TrainingProgramDTO> searchByName(String name) {
        return trainingProgramRepository.findByNameContainingAndStatus(name, true).stream().map(TrainingProgramMapper.INSTANCE::toDTO).collect(Collectors.toList());
    }

    @Override
    public Page<TrainingProgramDTO> getAll(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<TrainingProgram> page = trainingProgramRepository.getTrainingProgramByStatus(true, pageable);
        Page<TrainingProgramDTO> programDTOPage = page.map(TrainingProgramMapper.INSTANCE::toDTO);
        return programDTOPage;
    }
//    @Override
//    public List<TrainingProgramDTO> searchByNameOrCreator(SearchTPDTO searchList) {
//        boolean check = searchList.getSearch().isEmpty();
//        List<TrainingProgram> result = new ArrayList<>();
//        if (!check) {
//            List<String> lowerCaseSearchTerms = searchList.getSearch().stream()
//                    .map(String::toLowerCase)
//                    .collect(Collectors.toList());
//            result = doSearch(trainingProgramRepository.findAll(), lowerCaseSearchTerms);
//        }
//        return result.stream().map(TrainingProgramMapper.INSTANCE::toDTO).collect(Collectors.toList());
//    }

    private List<TrainingProgram> doSearch(List<TrainingProgram> trainingPrograms, List<String> searchList) {
        List<TrainingProgram> result = trainingPrograms.stream()
                .filter(trainingProgram1 -> trainingProgram1.getCreator() != null
                        && trainingProgram1.isStatus()).toList();
        List<TrainingProgram> result2 = new ArrayList<>();
        for (String search : searchList) {
            result2 = result.stream()
                    .filter(trainingProgram -> trainingProgram.getName().toLowerCase().contains(search)
                            || trainingProgram.getCreator().getFullName().toLowerCase().contains(search)).collect(Collectors.toList());
        }
        return result2;
    }

    @Override
    public TrainingProgramDTO getTrainingProgramById(Long id) {
        return TrainingProgramMapper.INSTANCE.toDTO(trainingProgramRepository.getTrainingProgramByIdAndStatus(id, true));
    }

    @Override
    public void save(TrainingProgramAddDto trainingProgramDTO, HashMap<TrainingProgram, List<Long>> trainingProgramHashMap, ReadFileDto readFileDto) {
        //add without read csv file
        CustomUserDetails CusUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = CusUserDetails.getUser();
        if (trainingProgramHashMap == null || trainingProgramHashMap.isEmpty()) {
            // get list of syllabus
            List<Syllabus> syllabusList = syllabusService.getAllSyllabusEntityById(trainingProgramDTO.getSyllabusIdList());

            if (syllabusList.size() < trainingProgramDTO.getSyllabusIdList().size()) {
                throw new SyllabusException("One or more syllabus Ids not found", HttpStatus.BAD_REQUEST.value());
            } else {
                LocalDate nowDay = LocalDate.now();
                int day = getDay(syllabusList);
                BigDecimal hour = getHour(syllabusList);

                int lastTrainingProgramId = trainingProgramRepository.findTopByOrderByIdDesc().getId().intValue() + 1;
                //create trainingProgram
                TrainingProgram trainingProgram = TrainingProgram.builder()
                        .name(trainingProgramDTO.getName())
                        .dateCreated(nowDay)
                        .lastDateModified(nowDay)
                        .day(day)
                        .hour(hour)
                        .state(true)
                        .status(true)
                        .programId(lastTrainingProgramId)
                        .creator(user)
                        .lastModifier(user)
                        .build();
                // create programSyllabus
                List<TrainingProgramSyllabus> programSyllabus = syllabusList.stream()
                        .map(syllabus -> new TrainingProgramSyllabus(null, true, syllabus, trainingProgram))
                        .collect(Collectors.toList());
                // save to database
                trainingProgramRepository.save(trainingProgram);
                trainingProgramSyllabusService.saveAll(programSyllabus);
            }
            //add with csv file
        } else {

            List<String> scanning = readFileDto.getScanning().stream().map(String::toLowerCase).collect(Collectors.toList());

            if (!scanning.isEmpty()) {
                List<TrainingProgram> trainingProgramFilter = filterExistTrainingClassByScanning(scanning, trainingProgramHashMap);

                String dupHandle = readFileDto.getDuplicateHandle().toLowerCase();
                //check by scanning
                if (dupHandle.equals(ALLOW)) {

                } else if (dupHandle.equals(SKIP)) {
                    trainingProgramFilter.stream().forEach(trainingProgram ->
                    {
                        trainingProgramHashMap.remove(trainingProgram);
                    });
                } else if (dupHandle.equals(REPLACE)) {
                    var a = trainingProgramRepository.findTopByProgramIdOrderByIdDesc(1001);
                    trainingProgramFilter.stream().forEach(trainingProgram ->
                    {
                        var trainProgramInDatabase = trainingProgramRepository.findTopByProgramIdOrderByIdDesc(trainingProgram.getProgramId());
                        trainProgramInDatabase.setName(trainingProgram.getName());
                        trainProgramInDatabase.setDateCreated(trainingProgram.getDateCreated());
                        trainProgramInDatabase.setLastDateModified(trainingProgram.getLastDateModified());
                        trainProgramInDatabase.setState(trainingProgram.isState());
                        trainProgramInDatabase.setStatus(trainingProgram.isStatus());
                        List<Long> syllabusIdList = trainingProgramHashMap.get(trainingProgram);
                        trainingProgramHashMap.remove(trainingProgram);
                        trainingProgramHashMap.put(trainProgramInDatabase, syllabusIdList);
                    });

                } else {
                    throw new FileException("Duplicate handle was wrong type", HttpStatus.BAD_REQUEST.value());
                }

                HashMap<TrainingProgram, List<Syllabus>> trainingProgramSyllabusHashMap = new HashMap<>();
                // find syllabus by id
                for (TrainingProgram key : trainingProgramHashMap.keySet()) {

                    List<Syllabus> syllabusList = syllabusService.getAllSyllabusEntityById(trainingProgramHashMap.get(key));

                    if (syllabusList.size() < trainingProgramHashMap.get(key).size()) {
                        throw new SyllabusException("One or more syllabus Ids not found", HttpStatus.BAD_REQUEST.value());
                    }
                    trainingProgramSyllabusHashMap.put(key, syllabusList);
                }
                // set syllabus and key to
                for (TrainingProgram key : trainingProgramSyllabusHashMap.keySet()) {
                    System.out.println(key.getId());
                    var syllabusList = trainingProgramSyllabusHashMap.get(key);
                    if (key.getId() == null) {

                        int day = getDay(syllabusList);
                        BigDecimal hour = getHour(syllabusList);

                        key.setDay(day);
                        key.setHour(hour);
                        key.setCreator(user);
                        key.setLastModifier(user);
                        List<TrainingProgramSyllabus> programSyllabus = syllabusList.stream()
                                .map(syllabus -> new TrainingProgramSyllabus(null, true, syllabus, key))
                                .collect(Collectors.toList());

                        trainingProgramRepository.save(key);
                        trainingProgramSyllabusService.saveAll(programSyllabus);
                    } else {
                        List<TrainingProgramSyllabus> programSyllabus = syllabusList.stream()
                                .map(syllabus -> new TrainingProgramSyllabus(null, true, syllabus, key))
                                .collect(Collectors.toList());
                        trainingProgramSyllabusService.removeByTrainingProgramID(key.getId());
                        trainingProgramRepository.save(key);
                        trainingProgramSyllabusService.saveAll(programSyllabus);
                    }
                }
            }
        }
    }


    @Override
    public String addFromFileCsv(MultipartFile file, ReadFileDto readFileDto) {
        Long creatorId, lastModifierId;
        int programId;
        LocalDate dateCreated, lastDateModified;
        String name;
        BigDecimal hour;
        int day;
        boolean status;
        List<Long> listTrainingClassesId, listTrainingProgramSyllabusesId;

        CSVParser parser = fileService.readFile(file, readFileDto.getEncodingType(), readFileDto.getSeparator());
//        // skip header of csv file
        parser.iterator().next();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        HashMap<TrainingProgram, List<Long>> trainingProgramHashMap = new HashMap<>();
        try {

            for (CSVRecord record : parser.getRecords()) {
                if (record.size() < 5) {
                    throw new FileException("File is Wrong format", HttpStatus.BAD_REQUEST.value());
                }
                try {
                    dateCreated = LocalDate.parse(record.get(2), dateFormat);
                    lastDateModified = LocalDate.parse(record.get(3), dateFormat);
                } catch (DateTimeException e) {
                    throw new FileException("Date time is wrong(format: yyyy-MM-dd)", HttpStatus.BAD_REQUEST.value());
                }
                try {
                    programId = Integer.parseInt(record.get(0));
                    name = record.get(1);
                    status = Boolean.parseBoolean(record.get(4));

                    listTrainingProgramSyllabusesId = Arrays.stream(record.get(5).split("/"))
                            .map(syllabusesId -> Long.parseLong(syllabusesId))
                            .collect(Collectors.toList());
                } catch (Exception e) {
                    throw new FileException(e.getMessage(), HttpStatus.BAD_REQUEST.value());
                }

                var trainingProgram = new TrainingProgram(null, programId, name, dateCreated, lastDateModified, null, 0, true, status, null, null, null, null);
                trainingProgramHashMap.put(trainingProgram, listTrainingProgramSyllabusesId);
            }
            save(null, trainingProgramHashMap, readFileDto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return "No error";
    }

    private List<TrainingProgram> filterExistTrainingClassByScanning(List<String> scanning,
                                                                     HashMap<TrainingProgram, List<Long>> trainingProgramHashMap) {
        List<TrainingProgram> trainingProgramFilter = new ArrayList<>();
        if (scanning.contains(PROGRAMID)) {
            trainingProgramFilter = trainingProgramHashMap.keySet().stream()
                    .filter(trainingProgram -> trainingProgramRepository.existsByProgramId(trainingProgram.getProgramId()))
                    .collect(Collectors.toList());
        } else if (scanning.contains(PROGRAMNAME)) {
            trainingProgramFilter = trainingProgramHashMap.keySet().stream()
                    .filter(trainingProgram -> trainingProgramRepository.existsByName(trainingProgram.getName()))
                    .collect(Collectors.toList());
        } else if (scanning.size() == 2) {
            trainingProgramFilter = trainingProgramHashMap.keySet().stream()
                    .filter(trainingProgram -> trainingProgramRepository.existsByProgramIdOrName(trainingProgram.getProgramId(), trainingProgram.getName()))
                    .collect(Collectors.toList());
        }
        return trainingProgramFilter;
    }


}
