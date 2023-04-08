package com.mockproject.service;

import com.mockproject.entity.TrainingClass;
import com.mockproject.entity.TrainingClassAdmin;
import com.mockproject.entity.User;
import com.mockproject.repository.TrainingClassAdminRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {TrainingClassAdminService.class})
@ExtendWith(SpringExtension.class)
class TrainingClassAdminServiceTest {
    @MockBean
    private TrainingClassAdminRepository trainingClassAdminRepository;

    @Autowired
    private TrainingClassAdminService trainingClassAdminService;

    /**
     * Method under test: {@link TrainingClassAdminService#saveList(List, Long)}
     */


    @Test
    void itShouldReturnTrueWhenSavingClassAdminSuccessful() {
        List<Long> listAdminId = new ArrayList<>();
        listAdminId.add(1L); listAdminId.add(2L); listAdminId.add(3L);
        Long tcId = 1L;
        TrainingClass tc = new TrainingClass();
        tc.setId(tcId);

        List<TrainingClassAdmin> list = listAdminId.stream().map(p -> {
            User admin = new User();
            admin.setId(p);
            return new TrainingClassAdmin(null, true, admin, tc);
        }).toList();

//        List<TrainingClassAdmin> listAfter = listBefore.stream().map(p -> {
//            p.setId(count.get());
//            return p;
//        }).toList();

        when(trainingClassAdminRepository.saveAll(list)).thenReturn(list);
        assertTrue(trainingClassAdminService.saveList(listAdminId, tcId));
        verify(trainingClassAdminRepository).saveAll(list);
    }

    /**
     * Method under test: {@link TrainingClassAdminService#saveList(List, Long)}
     */
    @Test
    void itShouldReturnFalseWhenSavingClassAdminSuccessful() {
        List<Long> listAdminId = new ArrayList<>();
        listAdminId.add(1L); listAdminId.add(2L); listAdminId.add(3L);
        Long tcId = 1L;
        TrainingClass tc = new TrainingClass();
        tc.setId(tcId);

        List<TrainingClassAdmin> list = listAdminId.stream().map(p -> {
            User admin = new User();
            admin.setId(p);
            return new TrainingClassAdmin(null, true, admin, tc);
        }).toList();

        when(trainingClassAdminRepository.saveAll(list)).thenReturn(Collections.emptyList());
        assertFalse(trainingClassAdminService.saveList(listAdminId, tcId));
        verify(trainingClassAdminRepository).saveAll(list);
    }

}

