package com.mockproject.service;

import com.mockproject.dto.FileClassResponseDTO;
import com.mockproject.entity.*;
import com.mockproject.exception.FileException;
import com.mockproject.exception.entity.DateNotValidExption;
import com.mockproject.exception.entity.EntityNotFoundException;
import com.mockproject.exception.file.FileRequestException;
import com.mockproject.repository.*;
import com.mockproject.service.interfaces.IFileService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Time;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class FileService implements IFileService {

    private final LocationRepository locationRepository;
    private final FsuRepository fsuRepository;
    private final ContactRepository contactRepository;
    private final TrainingProgramRepository trainingProgramRepository;
    private final AttendeeRepository attendeeRepository;
    private final UserRepository userRepository;

    private final String UTF_8 = "utf-8";
    private final String UTF_16 = "utf-16";
    private final String ISO_8859_1 = "iso_8859_1";
    private final String US_ASCII = "us_ascii";
    private final String COMMA = "comma";
    private final String PIPE = "pipe";
    private final String SPACE = " ";
    private final String SEMICOLON = "semicolon";

    @Override
    public FileClassResponseDTO readFileCsv(MultipartFile file) throws IOException {
        String[] HEADERS = {"Class Name", "Start Date (yyyy-mm-dd)", "Start Time (hh:mm:ss)", "End Time (hh:mm:ss)",
                "Duration Hour", "Duration Day", "Planned amount", "Accepted amount", "Actual amount", "Location",
                "FSU", "Contact Email", "Training Program Name", "Attendee", "Admin Email (divine by / )"
        };
        CSVParser parser = CSVParser.parse(file.getInputStream(),
                Charset.defaultCharset(),
                CSVFormat.DEFAULT.builder().setHeader(HEADERS)
                        .setSkipHeaderRecord(true).build());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        CSVRecord record = parser.getRecords().get(0);

        LocalDate startDate;
        Time startTime;
        Time endTime;
        BigDecimal hour;
        int day;
        int planned;
        int accepted;
        int actual;
        String className, locationName, fsuName, contactEmail, trainingProgramName, attendeeName;

        try {
            startDate = LocalDate.parse(record.get(1), formatter);
            startTime = Time.valueOf(record.get(2));
            endTime = Time.valueOf(record.get(3));
        } catch (Exception e) {
            throw new DateNotValidExption("Date time doesn't match with format! (yyyy/mm/dd) / Time (hh/mm)");
        }
        try {
            className = record.get(0);
            hour = new BigDecimal(record.get(4));
            day = Integer.parseInt(record.get(5));
            planned = Integer.parseInt(record.get(6));
            accepted = Integer.parseInt(record.get(7));
            actual = Integer.parseInt(record.get(8));
            locationName = record.get(9);
            fsuName = record.get(10);
            contactEmail = record.get(11);
            trainingProgramName = record.get(12);
            attendeeName = record.get(13);
        } catch (Exception e) {
            throw new FileRequestException("Data of record is not valid! Please check your input data!");
        }

        String[] listAdminEmail = record.get(14).split("/");
        Location location = locationRepository.findFirstByLocationNameAndStatus(locationName, true)
                .orElseThrow(() -> new EntityNotFoundException("Location not found!"));
        Fsu fsu = fsuRepository.findByFsuNameAndStatus(fsuName, true)
                .orElseThrow(() -> new EntityNotFoundException("Fsu not found!"));
        Contact contact = contactRepository.findByContactEmailAndStatus(contactEmail, true)
                .orElseThrow(()-> new EntityNotFoundException("Contact not found!"));
        TrainingProgram trainingProgram = trainingProgramRepository.findFirstByNameAndStatus(trainingProgramName, true)
                .orElseThrow(()-> new EntityNotFoundException("Tranining class not found!"));
        Attendee attendee = attendeeRepository.findByAttendeeNameAndStatus(attendeeName, true)
                .orElseThrow(() -> new EntityNotFoundException("Attendee not found!"));

        List<User> listAdmin = Arrays.stream(listAdminEmail)
                .map(p ->
                        userRepository.findByEmailAndStatus(p, true)
                                .orElseThrow(() -> new EntityNotFoundException("User not found!"))
                ).toList();

        List<Map<Long,String>> listMapAdmin = new ArrayList<>();
        for (User admin : listAdmin) {
            var adminMap = Map.of(admin.getId(), admin.getFullName());
            listMapAdmin.add(adminMap);
        }
        return new FileClassResponseDTO(className, startDate, startTime, endTime, hour,
                day, planned, accepted, actual, location.getId(),location.getLocationName() ,fsu.getId(),
                fsu.getFsuName(), contact.getId(),contact.getContactEmail(), trainingProgram.getId(),
                trainingProgram.getName(), attendee.getId(),attendee.getAttendeeName(), listMapAdmin);
    }

    @Override
    public byte[] getCsvFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        return IOUtils.toByteArray(is);
    }

    @Override
    public CSVParser readFile(MultipartFile file, String encodingType,String separator) {
        encodingType = encodingType.trim().toLowerCase();
        separator= separator.trim().toLowerCase();
        InputStreamReader inputStreamReader = null;
        BufferedReader reader;
        CSVParser parser;
        try {
            switch (encodingType) {
                case UTF_8:
                    inputStreamReader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8);
                    break;
                case UTF_16:
                    inputStreamReader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_16);
                    break;
                case ISO_8859_1:
                    inputStreamReader = new InputStreamReader(file.getInputStream(), StandardCharsets.ISO_8859_1);
                    break;
                case US_ASCII:
                    inputStreamReader = new InputStreamReader(file.getInputStream(), StandardCharsets.US_ASCII);
                    break;
                default:
                    inputStreamReader = new InputStreamReader(file.getInputStream());
            }
        } catch (IOException e) {
            throw new FileException("Encoding type is wrong ", HttpStatus.BAD_REQUEST.value());
        }

        switch (separator) {
            case COMMA:
                separator=",";
                break;
            case SEMICOLON:
                separator=";";
                break;
            case PIPE:
                separator="|";
                break;
            case SPACE:
                separator=" ";
                break;
            default:
                throw new FileException("separator type is wrong ", HttpStatus.BAD_REQUEST.value());
        }

        try {
            System.out.println(separator);
            reader = new BufferedReader(inputStreamReader);
            parser = new CSVParser(reader, CSVFormat.DEFAULT.builder().setSkipHeaderRecord(true).build().withDelimiter(separator.charAt(0)));
        } catch (IOException e) {
            throw new FileException("Content is wrong",HttpStatus.BAD_REQUEST.value());
        }
        return parser;
    }
}
