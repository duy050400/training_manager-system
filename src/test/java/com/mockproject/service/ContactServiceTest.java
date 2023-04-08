package com.mockproject.service;

import com.mockproject.dto.ContactDTO;
import com.mockproject.entity.Contact;
import com.mockproject.entity.TrainingClass;
import com.mockproject.mapper.ContactMapper;
import com.mockproject.repository.ContactRepository;
import com.mockproject.repository.TrainingClassRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {ContactService.class})
@ExtendWith(SpringExtension.class)
class ContactServiceTest {
    @MockBean
    private ContactRepository contactRepository;
    @MockBean
    private TrainingClassRepository trainingClassRepository;


    @Autowired
    private ContactService contactService;

    Contact contact1 = new Contact(1L, "contacmail1@gmail.com", "Des 1", true, null);
    Contact contact2 = new Contact(2L, "contacmail2@gmail.com", "Des 2", false, null);
    Contact contact3 = new Contact(3L, "contacmail3@gmail.com", "Des 3", true, null);

    /**
     * Method under test: {@link ContactService#listAllTrue()}
     */
    @Test
    void canListAllContactWithStatusTrue() {
        List<Contact> list = new ArrayList<>();
        list.add(contact1);
        list.add(contact2);
        list.add(contact3);

        when(contactRepository.findByStatus(true)).thenReturn(list.stream().filter(Contact::isStatus).toList());

        List<ContactDTO> result = contactService.listAllTrue();

        assertEquals(2, result.size());
        assertEquals("contacmail1@gmail.com",result.get(0).getContactEmail());
        assertTrue(result.stream().filter(p-> !p.isStatus()).toList().isEmpty() );

        verify(contactRepository).findByStatus(true);
    }

    /**
     * Method under test: {@link ContactService#getContactByTrainingClassId(Long)} ()}
     */
    @Test
    void canListAllContactInATrainingClass() {

        TrainingClass tc = new TrainingClass();
        tc.setContact(contact1);

        when(trainingClassRepository.findByIdAndStatus(1L, true)).thenReturn(Optional.of(tc));
        ContactDTO dto = contactService.getContactByTrainingClassId(1L);
        assertEquals(ContactMapper.INSTANCE.toDTO(contact1), dto);
        assertTrue(dto.isStatus());
        verify(trainingClassRepository).findByIdAndStatus(contact1.getId(), contact1.isStatus());
    }


    @Test
    void itShouldThrowExceptionWhenTrainingClassContactNotFound() {

        TrainingClass tc = new TrainingClass();
        tc.setContact(contact2);
        when(trainingClassRepository.findByIdAndStatus(2L, true)).thenReturn(Optional.of(tc));
        assertThrows(NullPointerException.class, () -> contactService.getContactByTrainingClassId(2L));
        verify(trainingClassRepository).findByIdAndStatus(2L, true);

        when(trainingClassRepository.findByIdAndStatus(1L, true)).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> contactService.getContactByTrainingClassId(1L));
        verify(trainingClassRepository).findByIdAndStatus(1L, true);
    }
}

