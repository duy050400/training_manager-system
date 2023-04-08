package com.mockproject.service.interfaces;

import com.mockproject.dto.ContactDTO;

import java.util.List;

public interface IContactService {

    List<ContactDTO> listAllTrue();

    ContactDTO getContactByTrainingClassId(Long id);
}
