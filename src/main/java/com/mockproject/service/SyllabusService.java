package com.mockproject.service;

import com.mockproject.dto.*;
import com.mockproject.entity.*;
import com.mockproject.mapper.SessionMapper;
import com.mockproject.mapper.SyllabusMapper;
import com.mockproject.repository.OutputStandardRepository;
import com.mockproject.repository.SyllabusRepository;
import com.mockproject.repository.TrainingProgramSyllabusRepository;
import com.mockproject.repository.UnitDetailRepository;
import com.mockproject.service.interfaces.ISessionService;
import com.mockproject.service.interfaces.ISyllabusService;
import com.mockproject.utils.FileUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class SyllabusService implements ISyllabusService {

    private final SyllabusRepository syllabusRepo;

    private final OutputStandardRepository outputStandardRepo;

    private final UnitDetailRepository unitDetailRepo;

    private final SyllabusRepository syllabusRepository;

    private final ISessionService sessionService;

    private final TrainingProgramSyllabusRepository trainingProgramSyllabusRepository;

    private static final String TEMPLATE_FILE_PATH = "file-format\\syllabus-template.csv";

    @Override
    public List<SyllabusDTO> listByTrainingProgramIdTrue(Long trainingProgramId) {
        TrainingProgram tp = new TrainingProgram();
        tp.setId(trainingProgramId);
        List<TrainingProgramSyllabus> listTPS = trainingProgramSyllabusRepository.findByTrainingProgramAndStatus(tp, true);
        List<Syllabus> listSyllabus = new ArrayList<>();

        listTPS.forEach(p -> listSyllabus.add(p.getSyllabus()));
        return listSyllabus.stream().map(SyllabusMapper.INSTANCE::toDTO).collect(Collectors.toList());
    }

    @Override
    public Page<SyllabusDTO> getListSyllabus(boolean status, LocalDate fromDate, LocalDate toDate,
                                             List<String> search, String[] sort, Optional<Integer> page, Optional<Integer> row) {
        List<Sort.Order> order = new ArrayList<>();
        if(row.orElse(10) < 1)  throw new InvalidParameterException("Page size must not be less than one!");
        if(page.orElse(0) < 0)  throw new InvalidParameterException("Page number must not be less than zero!");
        int skipCount = page.orElse(0) * row.orElse(10);
        Set<String> sourceFieldList = getAllFields(new Syllabus().getClass());
        if(sort[0].contains(",")){
            for (String sortItem: sort) {
                String[] subSort = sortItem.split(",");
                if(sourceFieldList.contains(subSort[0])){
                    order.add(new Sort.Order(getSortDirection(subSort[1]), transferProperty(subSort[0])));
                } else {
                    throw new NotFoundException(subSort[0] + " is not a propertied of Syllabus!");
                }
            }
        } else {
            if(sort.length == 1){
                throw new ArrayIndexOutOfBoundsException("Sort direction(asc/desc) not found!");
            }
            if(sourceFieldList.contains(sort[0])){
                order.add(new Sort.Order(getSortDirection(sort[1]), transferProperty(sort[0])));
            } else {
                throw new NotFoundException(sort[0] + " is not a propertied of Syllabus!");
            }
        }
        List<Syllabus> pages = syllabusRepo.getListSyllabus(status, fromDate, toDate, search.size() > 0 ? search.get(0) : "", getListSyllabusIdByOSD(search.size() > 0 ? search.get(0) : ""), Sort.by(order));
        if (search.size() > 1){
            for (int i = 1; i < search.size(); i++) {
                String subSearch = search.get(i).toUpperCase();
                pages = pages.stream().filter(s
                        -> s.getName().toUpperCase().contains(subSearch) ||
                                s.getCode().toUpperCase().contains(subSearch) ||
                                s.getCreator().getFullName().toUpperCase().contains(subSearch) ||
                                checkOsdBelongSyllabus(s.getId(), subSearch))
                        .collect(Collectors.toList());
            }
        }
        if(pages.size() > 0){
            return new PageImpl<>(
                    pages.stream().skip(skipCount).limit(row.orElse(10)).map(SyllabusMapper.INSTANCE::toDTO).peek(p -> p.setOutputStandardCodeList(unitDetailRepo.getOsdCodeBySyllabusID(true, p.getId()))).collect(Collectors.toList()),
                    PageRequest.of(page.orElse(0), row.orElse(10), Sort.by(order)),
                    pages.size());
        } else {
            throw new NotFoundException("Syllabus not found!");
        }
    }

    private static String transferProperty(String property){
        switch (property) {
            case "creator":
                return "creator.fullName";
            default:
                return property;
        }
    }

    private boolean checkOsdBelongSyllabus(long syllabusId, String search) {
        if (getListSyllabusIdByOSD(search).contains(syllabusId)) {
            return true;
        }
        return false;
    }

    @Override
    public List<Syllabus> getAllSyllabusEntityById(List<Long> sId) {
        List<Syllabus> syllabus = syllabusRepository.getAllSyllabusByIdInAndStatus(sId,true);
        return syllabus;
    }

    @Override
    public SyllabusDTO getSyllabusById(Long id){
        Syllabus syllabus = syllabusRepository.getSyllabusById(id);
        return SyllabusMapper.INSTANCE.toDTO(syllabus);
    }

//    @Autowired
//    private final SyllabusRepository repository;
//    public Syllabus getSyllabusById(Long id){
//         Optional<Syllabus> syllabusOptional = repository.findById(id);
//         if(syllabusOptional.isPresent()){
//             return syllabusOptional.get();
//         }else {
//             return new Syllabus();
//         }
//    }

    private static Set<String> getAllFields(final Class<?> type) {
        Set<String> fields = new HashSet<>();
        //loop the fields using Java Reflections
        for (Field field : type.getDeclaredFields()) {
            fields.add(field.getName());
        }
        //recursive call to getAllFields
        if (type.getSuperclass() != null) {
            fields.addAll(getAllFields(type.getSuperclass()));
        }
        return fields;
    }

    public Sort.Direction getSortDirection(String direction) {
        if (direction.equals("asc")) {
            return Sort.Direction.ASC;
        } else if (direction.equals("desc")) {
            return Sort.Direction.DESC;
        }
        return Sort.Direction.ASC;
    }

    @Override
    public List<Long> getListSyllabusIdByOSD(String osd) {
        List<UnitDetail> detailList = unitDetailRepo.findByStatusAndOutputStandardIn(true, outputStandardRepo.findByStatusAndStandardCodeContainingIgnoreCase(true, osd));
        return detailList.stream().map(ob
                -> ob.getUnit().getSession().getSyllabus().getId()).collect(Collectors.toList());
    }

    @Override
    public boolean replace(SyllabusDTO syllabusDTO, boolean status){
        sessionService.deleteSessions(syllabusDTO.getId(), true);

        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        syllabusRepository.save(SyllabusMapper.INSTANCE.toEntity(syllabusDTO));

        sessionService.createSession(syllabusDTO.getId(), syllabusDTO.getSessionDTOList(), user.getUser());

        return true;
    }

    @Override
    public Long create(SyllabusDTO syllabus, User user){
        syllabus.setCreatorId(user.getId());
        syllabus.setLastModifierId(user.getId());
        syllabus.setDateCreated(java.time.LocalDate.now());
        syllabus.setLastDateModified(java.time.LocalDate.now());
        syllabus.setHour(BigDecimal.valueOf(0));
        syllabus.setStatus(true);
        Syllabus newSyllabus = syllabusRepository.save(SyllabusMapper.INSTANCE.toEntity(syllabus));
        sessionService.createSession(newSyllabus.getId(), syllabus.getSessionDTOList(), user);
        return newSyllabus.getId();
    }

    @Override
    public boolean deactivate(Long syllabusId, boolean status) {
        Optional<Syllabus> syllabus = syllabusRepository.findByIdAndStateAndStatus(syllabusId, true, status);
        syllabus.orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "Syllabus not found"));

        syllabus.get().setState(syllabus.get().isState() == true ? false: true);

        syllabusRepository.save(syllabus.get());

        return syllabus.get().isState();
    }

    @Override
    public Syllabus editSyllabus(SyllabusDTO syllabusDTO, boolean status) throws IOException{
        Optional<Syllabus> syllabus = syllabusRepository.findByIdAndStatus(syllabusDTO.getId(), status);
        syllabus.orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "Syllabus not found"));
        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Set day and hour
        syllabus.get().setDay(0);
        syllabusRepository.save(syllabus.get());

        syllabusDTO.setLastModifierId(user.getUser().getId());
        syllabusDTO.setLastDateModified(java.time.LocalDate.now());

        if(syllabusDTO.isStatus())
        {
            for (SessionDTO s : syllabusDTO.getSessionDTOList()) {
                if (s.getId() == null) {
                    sessionService.createSession(syllabusDTO.getId(), s, user.getUser());
                }else {
                    sessionService.editSession(s, true);
                }
            }
        } else {
            deleteSyllabus(syllabusDTO.getId(), status);
        }

        syllabus = syllabusRepository.findByIdAndStatus(syllabusDTO.getId(), true);
        List<SessionDTO> sessionDTOList = sessionService.getAllSessionBySyllabusId(syllabus.get().getId(), true);
        syllabusDTO.setHour(syllabus.get().getHour());
        syllabusDTO.setSessionDTOList(sessionDTOList);

        if(syllabusDTO.getSessionDTOList().isEmpty()){
            syllabusDTO.setDay(0);
        } else {
            syllabusDTO.setDay(syllabus.get().getListSessions().size());
        }

        return syllabusRepository.save(SyllabusMapper.INSTANCE.toEntity(syllabusDTO));
    }

    @Override
    public boolean deleteSyllabus(Long syllabusId, boolean status){
        Optional<Syllabus> syllabus = syllabusRepository.findByIdAndStatus(syllabusId, status);
        syllabus.orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT,"Syllabus not found"));
        List<SessionDTO> sessionDTOList = sessionService.getAllSessionBySyllabusId(syllabusId, true);
        List<Session>   sessionList = new ArrayList<>();

        for(SessionDTO sessionDTO : sessionDTOList){
            sessionList.add(SessionMapper.INSTANCE.toEntity(sessionDTO));
        }
        syllabus.get().setListSessions(sessionList);
        syllabus.get().setStatus(false);
        if(!(syllabus.get().getListSessions().isEmpty()))
            sessionService.deleteSessions(syllabusId, status);
        syllabusRepository.save(syllabus.get());
        return true;
    }

    @Override
    public SyllabusDTO readFileCsv(MultipartFile file, int condition, int handle) throws IOException {

        final String[] HEADERS = {"Syllabus Name","Syllabus Code","Syllabus Version","Syllabus Level","Attendee Amount","Technical Requirements","Course Objectives","Quiz","Assignment","Final","Final Theory","Final Practice","GPA","Training Description","Retest Description","Marking Description","Waiver Criteria Description","Other Description","State","Status"};
        CSVParser parser = CSVParser.parse(file.getInputStream(), Charset.defaultCharset(), CSVFormat.DEFAULT.builder().setHeader(HEADERS).setSkipHeaderRecord(true).build());
        List<CSVRecord> records = parser.getRecords();

        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        SyllabusDTO syllabusDTO = new SyllabusDTO();
        for(CSVRecord record: records) {
            String name = record.get(0);
            if(name.isBlank())
              throw new NotFoundException("Name cannot empty");
            String code = record.get(1);
            if(code.isBlank())
                throw new NotFoundException("Code cannot empty");
            String version = record.get(2);
            String level = record.get(3);
            int attendee = Integer.parseInt(record.get(4));
            String technicalRequirements = record.get(5);
            String courseObjectives = record.get(6);
            BigDecimal quiz = BigDecimal.valueOf(Double.parseDouble(record.get(7)));
            BigDecimal assignment = BigDecimal.valueOf(Double.parseDouble(record.get(8)));
            BigDecimal finalExam = BigDecimal.valueOf(Double.parseDouble(record.get(9)));
            BigDecimal finalTheory = BigDecimal.valueOf(Double.parseDouble(record.get(10)));
            BigDecimal finalPractice = BigDecimal.valueOf(Double.parseDouble(record.get(11)));
            BigDecimal gpa = BigDecimal.valueOf(Double.parseDouble(record.get(12)));
            String trainingDes = record.get(13);
            String reTestDes = record.get(14);
            String markingDes = record.get(15);
            String waiverCriteriaDes = record.get(16);
            String otherDes = record.get(17);
            boolean status = true;
            boolean state = true;
            Syllabus syllabus = Syllabus.builder()
                    .name(name)
                    .code(code)
                    .version(version)
                    .level(level)
                    .attendee(attendee)
                    .technicalRequirements(technicalRequirements)
                    .courseObjectives(courseObjectives)
                    .dateCreated(java.time.LocalDate.now())
                    .lastDateModified(java.time.LocalDate.now())
                    .quiz(quiz)
                    .assignment(assignment)
                    .finalExam(finalExam)
                    .finalTheory(finalTheory)
                    .finalPractice(finalPractice)
                    .gpa(gpa)
                    .trainingDes(trainingDes)
                    .reTestDes(reTestDes)
                    .markingDes(markingDes)
                    .waiverCriteriaDes(waiverCriteriaDes)
                    .otherDes(otherDes)
                    .state(state)
                    .status(status)
                    .creator(user.getUser())
                    .lastModifier(user.getUser())
                    .build();
            syllabusDTO = SyllabusMapper.INSTANCE.toDTO(syllabus);
        }

        syllabusDTO.setHour(BigDecimal.valueOf(0));

        // condition Name or code
        // 1 Name
        // 2 Code
        // 3 Name and Code

        // handle Allow, Replace or Skip
        // 1 Allow
        // 2 Replace
        // 3 Skip
        if(condition == 3){
            if (handle == 1){
                return syllabusDTO;
            }
            else if (handle == 2) {
                Optional<List<Syllabus>> syllabusList = syllabusRepository.findByNameAndCodeAndStatus(syllabusDTO.getName(), syllabusDTO.getCode(),true);
                if (syllabusList.isPresent()) {
                    Syllabus syllabus = syllabusList.get().get(syllabusList.get().size() - 1);
                    syllabusDTO.setId(syllabus.getId());
                }
                return syllabusDTO;
            }
            else if (handle == 3){
                Optional<List<Syllabus>> syllabusList = syllabusRepository.findByNameAndCodeAndStatus(syllabusDTO.getName(), syllabusDTO.getCode(),true);
                if (syllabusList.isEmpty()){
                    return syllabusDTO;
                }
                else {
                    return new SyllabusDTO();
                }
            }
        }
        else if (condition == 1){
            if(handle == 1) {
                return syllabusDTO;
            }
            else if (handle == 2) {
                Optional<List<Syllabus>> syllabusList = syllabusRepository.findByNameAndStatus(syllabusDTO.getName(), true);
                if (syllabusList.isPresent()) {
                    Syllabus syllabus = syllabusList.get().get(syllabusList.get().size() - 1);
                    syllabusDTO.setId(syllabus.getId());
                }
                return syllabusDTO;
            }
            else if (handle == 3){
                Optional<List<Syllabus>> syllabusList = syllabusRepository.findByNameAndStatus(syllabusDTO.getName(), true);
                if (syllabusList.isEmpty()){
                    return syllabusDTO;
                }
                else {
                    return new SyllabusDTO();
                }
            }
        } else if (condition == 2){
            if(handle == 1){
                return syllabusDTO;
            }
            else if (handle == 2) {
                Optional<List<Syllabus>> syllabusList = syllabusRepository.findByCodeAndStatus(syllabusDTO.getName(), true);
                if (syllabusList.isPresent()) {
                    Syllabus syllabus = syllabusList.get().get(syllabusList.get().size() - 1);
                    syllabusDTO.setId(syllabus.getId());
                }
                return syllabusDTO;
            }
            else if (handle == 3){
                Optional<List<Syllabus>> syllabusList = syllabusRepository.findByCodeAndStatus(syllabusDTO.getName(), true);
                if (syllabusList.isEmpty()){
                    return syllabusDTO;
                }
                else {
                    return new SyllabusDTO();
                }
            }
        }
        return syllabusDTO;
    }

    @Override
    public SyllabusDTO getSyllabusById(Long syllabusId,boolean state, boolean status){
        Optional<Syllabus> syllabus = syllabusRepository.findByIdAndStateAndStatus(syllabusId, state, status);
        syllabus.orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "Syllabus not found"));
        SyllabusDTO syllabusDTO = SyllabusMapper.INSTANCE.toDTO(syllabus.get());
        syllabusDTO.setOutputStandardCodeList(unitDetailRepo.getOsdCodeBySyllabusID1(true, syllabusId));
        List<SessionDTO> sessionDTOList = sessionService.getAllSessionBySyllabusId(syllabusId, true);
        syllabusDTO.setSessionDTOList(sessionDTOList);
        return syllabusDTO;
    }

    public byte[] getTemplateCsvFile() throws IOException {
        return FileUtils.getFileBytes(TEMPLATE_FILE_PATH);
    }

    public Long duplicateSyllabus(Long syllabusId, boolean status, boolean state){
        Optional<Syllabus> syllabus = syllabusRepository.findByIdAndStateAndStatus(syllabusId, state, status);
        syllabus.orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "Syllabus not found"));
        SyllabusDTO syllabusDTO = SyllabusMapper.INSTANCE.toDTO(syllabus.get());
        List<SessionDTO> sessionDTOList = sessionService.getAllSessionBySyllabusId(syllabusId, true);
        syllabusDTO.setSessionDTOList(sessionDTOList);
        syllabusDTO.setId(null);
        syllabusDTO.setName("Copy of "+syllabusDTO.getName());

        for (SessionDTO session: syllabusDTO.getSessionDTOList()) {
            for(UnitDTO unit: session.getUnitDTOList()){
                for(UnitDetailDTO unitDetail: unit.getUnitDetailDTOList()){
                    for(TrainingMaterialDTO trainingMaterial: unitDetail.getTrainingMaterialDTOList()){
                        trainingMaterial.setId(null);
                    }
                    unitDetail.setId(null);
                }
                unit.setId(null);
            }
            session.setId(null);
        }
        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return create(syllabusDTO, user.getUser());
    }

    @Override
    public List<Syllabus> liveSearch() {
        var result = syllabusRepository.findAllByStatus(true);
        return result.orElse(null);
    }
}
