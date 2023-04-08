package com.mockproject.service;

import com.mockproject.dto.ContactDTO;
import com.mockproject.entity.TrainingClass;
import com.mockproject.mapper.ContactMapper;
import com.mockproject.repository.ContactRepository;
import com.mockproject.repository.TrainingClassRepository;
import com.mockproject.service.interfaces.IContactService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ContactService implements IContactService {

    private final ContactRepository repository;
    private final TrainingClassRepository trainingClassRepository;

    @Override
    public List<ContactDTO> listAllTrue() {
        return repository.findByStatus(true).stream().map(ContactMapper.INSTANCE::toDTO).collect(Collectors.toList());
    }

    @Override
    public ContactDTO getContactByTrainingClassId(Long id) {
        TrainingClass tc = trainingClassRepository.findByIdAndStatus(id, true).orElseThrow();
        if(!tc.getContact().isStatus()){throw new NullPointerException();}
        return ContactMapper.INSTANCE.toDTO(tc.getContact());
    }
}
