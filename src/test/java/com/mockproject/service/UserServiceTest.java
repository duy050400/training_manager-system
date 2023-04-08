package com.mockproject.service;

import com.mockproject.dto.UserDTO;
import com.mockproject.entity.*;
import com.mockproject.mapper.UserMapper;
import com.mockproject.repository.*;
import com.mockproject.service.interfaces.IUnitService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {UserService.class})
@ExtendWith(SpringExtension.class)
class UserServiceTest {

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private RoleRepository roleRepository;
    @MockBean
    private LevelRepository levelRepository;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private TrainingClassRepository trainingClassRepository;
    @MockBean
    private TrainingClassUnitInformationRepository trainingClassUnitInformationRepository;
    @MockBean
    private TrainingClassAdminRepository trainingClassAdminRepository;
    @MockBean
    private AttendeeRepository attendeeRepository;
    @MockBean
    private IUnitService unitService;

    @Autowired
    private UserService userService;

    Role role1 = new Role(1L, "Super Admin", true, null, null);
    Role role2 = new Role(2L, "Class Admin", true, null, null);

    User user1 = new User(1L, "user1@gmail.com", "123", "Tui la user 1", "user1.png",
            1, LocalDate.now(), "0123456789", true, true, role2, null, null,
            null, null, null, null, null, null,
            null, null, null, null, null);
    User user2 = new User(2L, "user2@gmail.com", "123", "Tui la user 2", "user1.png",
            1, LocalDate.now(), "0123456789", true, false, role1, null, null,
            null, null, null, null, null, null,
            null, null, null, null, null);
    User user3 = new User(3L, "user3@gmail.com", "123", "Tui la user 3", "user1.png",
            1, LocalDate.now(), "0123456789", true, true, role2, null, null,
            null, null, null, null, null, null,
            null, null, null, null, null);

    Level level1 = new Level(1l, "AA","hihi", true, null);

    /**
     * Method under test: {@link UserService#listClassAdminTrue()}
     */
    @Test
    void canListUserHaveRoleClassAdminWithStatusTrue() {
        List<User> list = new ArrayList<>();
        list.add(user1);
        list.add(user2);
        list.add(user3);
        Role role = new Role();
        role.setId(2L);

        when(userRepository.findByRoleAndStatus(role, true)).thenReturn(list.stream().filter(p -> p.getRole().getId() == 2L).toList());
        List<UserDTO> result = userService.listClassAdminTrue();

        assertEquals(2, result.size());
        assertEquals("user3@gmail.com", result.get(1).getEmail());
        verify(userRepository).findByRoleAndStatus((Role) any(), anyBoolean());
    }

    /**
     * Method under test: {@link UserService#getUserById(Long)}
     */
    @Test
    void canGetUserById() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
        UserDTO actualUser = userService.getUserById(2L);
        assertTrue(actualUser.isGender() && user2.isGender());
        assertFalse(user2.isStatus() && actualUser.isStatus());
        assertEquals(user2.getState(), actualUser.getState());
        assertEquals(user2.getRole().getId(), actualUser.getRoleId());
        assertEquals(user2.getPhone(), actualUser.getPhone());
        assertEquals(2L, actualUser.getId());
        assertEquals(user2.getImage(), actualUser.getImage());
        assertEquals(user2.getFullName(), actualUser.getFullName());
        assertEquals(user2.getEmail(), actualUser.getEmail());
        assertEquals(user2.getDob(), actualUser.getDob());
        verify(userRepository).findById((Long) any());
    }

    @Test
    void canUpdateStatus(){
        //updatestatus to false to delete user

        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.save(user1)).thenReturn(user1);

        Boolean result = userService.updateStatus(1L);
        assertEquals(true, result);
        assertEquals(user1.isStatus(), false);

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(user1);
    }

    @Test
    void canUpdateStateToFalse(){
        //update state to 0 to De-active user

        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.save(user1)).thenReturn(user1);

        int result = userService.updateStateToFalse(1L);
        assertEquals(0, result);
        assertEquals(user1.getState(), 0);

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(user1);
    }

    @Test
    void canUpdateStateToTrue(){
        //update state to 1 to Active user

        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.save(user1)).thenReturn(user1);

        int result = userService.updateStateToTrue(1L);
        assertEquals(1, result);
        assertEquals(user1.getState(), 1);

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(user1);
    }

    @Test
    void canChangeRole(){
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(roleRepository.getRoleById(1l)).thenReturn(Optional.of(role1));
        when(userRepository.save(user1)).thenReturn(user1);

        boolean result = userService.changeRole(1l, 1l);

        assertTrue(result);
        assertEquals(role1, user1.getRole());

        verify(userRepository, times(1)).findById(1l);
        verify(userRepository, times(1)).save(user1);
        verify(roleRepository,times(1)).getRoleById(1l);
    }

    @Test
    void canEditUser(){
        when(userRepository.findById(1l)).thenReturn(Optional.of(user1));
        when(levelRepository.getLevelById(1l)).thenReturn(Optional.of(level1));
        when(userRepository.save(user1)).thenReturn(user1);

        UserDTO userDTO = UserMapper.INSTANCE.toDTO(user1);
        userDTO.setFullName("Test");
        userDTO.setLevelId(1l);
        userDTO.setGender(true);
        LocalDate dob;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        dob = LocalDate.parse("28/10/2002", formatter);

        userDTO.setDob(dob);

        boolean result = userService.editUser(userDTO);

        assertTrue(result);
        assertEquals("Test", user1.getFullName());
        assertEquals(dob, user1.getDob());
        assertTrue(user1.isGender());

        verify(userRepository, times(1)).findById(1l);
        verify(levelRepository,times(1)).getLevelById(1l);
        verify(userRepository, times(1)).save(user1);
    }


    /**
     * Method under test: {@link UserService#getAllTrainersByTrainingClassId(long)}
     */
    @Test
    void canGetAllTrainersByTrainingClassId() {
        List<User> users = new ArrayList<>(Arrays.asList(user1, user2, user3));

        TrainingClass tc = new TrainingClass();
        tc.setId(1L);
        tc.setStatus(true);

        List<TrainingClassUnitInformation> infos = Stream.of(1, 2, 3)
                .map(i -> {
                    TrainingClassUnitInformation t = new TrainingClassUnitInformation();
                    t.setTrainingClass(tc);
                    t.setStatus(users.get(i-1).isStatus());
                    t.setTrainer(users.get(i-1));
                    return t;
                }).toList();

        when(trainingClassRepository.findByIdAndStatus(1L, true)).thenReturn(Optional.of(tc));
        List<TrainingClassUnitInformation> list = infos.stream().filter(TrainingClassUnitInformation::isStatus).toList();
        when(trainingClassUnitInformationRepository.findByTrainingClassAndStatus(tc, true))
                .thenReturn(Optional.of(list));
        list.forEach(i -> when(userRepository.findByIdAndStatus(i.getTrainer().getId(), true))
                .thenReturn(Optional.of(i.getTrainer())));

        List<UserDTO> result = userService.getAllTrainersByTrainingClassId(1L);
        assertEquals(2, result.size());
        verify(trainingClassRepository).findByIdAndStatus(1L, true);
        verify(trainingClassUnitInformationRepository).findByTrainingClassAndStatus(tc, true);
        list.forEach(i -> verify(userRepository, atLeastOnce()).findByIdAndStatus(i.getTrainer().getId(), true));
    }



    @Test
    void itShouldThrowExceptionWhenTrainingClassTrainersNotFound() {

        TrainingClass tc = new TrainingClass();
        tc.setId(1L);
        tc.setStatus(true);

        when(trainingClassRepository.findByIdAndStatus(2L, true)).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> userService.getAllTrainersByTrainingClassId(2L));


        when(trainingClassUnitInformationRepository.findByTrainingClassAndStatus(tc, true))
                .thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> userService.getAllTrainersByTrainingClassId(1L));

        when(userRepository.findByIdAndStatus(1L, true)).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> userService.getAllTrainersByTrainingClassId(1L));

        verify(trainingClassRepository, times(2)).findByIdAndStatus(1L, true);

    }


    /**
     * Method under test: {@link UserService#getAllAdminsByTrainingClassId(long)}
     */
    @Test
    void canGetAllAdminsByTrainingClassId() {
        List<User> users = new ArrayList<>(Arrays.asList(user1, user2, user3));

        TrainingClass tc = new TrainingClass();
        tc.setId(1L);
        tc.setStatus(true);

        List<TrainingClassAdmin> infos = Stream.of(1, 2, 3)
                .map(i -> {
                    TrainingClassAdmin t = new TrainingClassAdmin();
                    t.setTrainingClass(tc);
                    t.setStatus(users.get(i-1).isStatus());
                    t.setAdmin(users.get(i-1));
                    return t;
                }).toList();

        when(trainingClassRepository.findByIdAndStatus(1L, true)).thenReturn(Optional.of(tc));
        List<TrainingClassAdmin> list = infos.stream().filter(TrainingClassAdmin::isStatus).toList();
        when(trainingClassAdminRepository.findByTrainingClassAndStatus(tc, true))
                .thenReturn(Optional.of(list));
        list.forEach(i -> when(userRepository.findByIdAndStatus(i.getAdmin().getId(), true))
                .thenReturn(Optional.of(i.getAdmin())));

        List<UserDTO> result = userService.getAllAdminsByTrainingClassId(1L);
        assertEquals(2, result.size());
        verify(trainingClassRepository).findByIdAndStatus(1L, true);
        verify(trainingClassAdminRepository).findByTrainingClassAndStatus(tc, true);
        list.forEach(i -> verify(userRepository, atLeastOnce()).findByIdAndStatus(i.getAdmin().getId(), true));
    }


    @Test
    void itShouldThrowExceptionWhenTrainingClassAdminNotFound() {

        TrainingClass tc = new TrainingClass();
        tc.setId(1L);
        tc.setStatus(true);

        when(trainingClassRepository.findByIdAndStatus(2L, true)).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> userService.getAllAdminsByTrainingClassId(2L));


        when(trainingClassAdminRepository.findByTrainingClassAndStatus(tc, true))
                .thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> userService.getAllAdminsByTrainingClassId(1L));

        when(userRepository.findByIdAndStatus(1L, true)).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> userService.getAllAdminsByTrainingClassId(1L));

        verify(trainingClassRepository, times(2)).findByIdAndStatus(1L, true);

    }


    /**
     * Method under test: {@link UserService#getCreatorByTrainingClassId(long)}
     */
    @Test
    void canGetCreatorByTrainingClassId() {
        TrainingClass tc = new TrainingClass();
        tc.setStatus(true);
        tc.setId(1L);
        tc.setCreator(user1);

        when(trainingClassRepository.findByIdAndStatus(tc.getId(), true)).thenReturn(Optional.of(tc));
        when(userRepository.findByIdAndStatus(tc.getId(), true)).thenReturn(Optional.of(tc.getCreator()));

        UserDTO dto = userService.getCreatorByTrainingClassId(tc.getId());
        assertNotNull(dto);
        assertEquals(tc.getCreator().getId(), dto.getId());
        verify(userRepository).findByIdAndStatus(tc.getId(), true);
    }


    @Test
    void itShouldThrowExceptionWhenTrainingClassCreatorNotFound() {

        when(trainingClassRepository.findByIdAndStatus(1L, true)).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> userService.getCreatorByTrainingClassId(1L));

        when(userRepository.findByIdAndStatus(1L, true)).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> userService.getCreatorByTrainingClassId(1L));

        verify(trainingClassRepository, times(2)).findByIdAndStatus(1L, true);
    }


    /**
     * Method under test: {@link UserService#getAllTrainersForADateByTrainingClassId(long, int)}
     */
    @Test
    void canGetAllTrainersForADateByTrainingClassId() {

        // Create Training class
        TrainingClass tc = new TrainingClass();

        // Create id
        AtomicReference<Long> id = new AtomicReference<>(1L);

        // Create list units and Assign value for each
        List<Unit> units = Stream.of(1, 2)
                .map(i -> {
                    Unit unit = new Unit();
                    unit.setId(id.get());
                    id.updateAndGet(v -> v + 1);
                    unit.setListTrainingClassUnitInformations(new ArrayList<>(List.of(new TrainingClassUnitInformation())));
                    unit.setStatus(true);
                    return unit;
                }).toList();

        // reset id
        id.getAndSet(1L);

        // Create list TrainingClassUnitInfo
        List<TrainingClassUnitInformation> list = new ArrayList<>();

        // Assign value for Training class unit info
        units.forEach(i -> {
            TrainingClassUnitInformation t = i.getListTrainingClassUnitInformations().get(0);
            t.setId(id.get());
            id.getAndSet(id.get() + 1);
            t.setTrainingClass(tc);
            t.setUnit(i);
            t.setTrainer(i.getId() % 2 == 0 ? user1 : user3);
            t.setStatus(true);
            list.add(t);
        });


        when(trainingClassRepository.findByIdAndStatus(1L, true)).thenReturn(Optional.of(tc));
        when(unitService.getListUnitsInASessionByTrainingClassId(1L, 1)).thenReturn(units);
        units.forEach(i -> when(trainingClassUnitInformationRepository.findByUnitAndTrainingClassAndStatus(i, tc, true))
                .thenReturn(Optional.of(i.getListTrainingClassUnitInformations().get(0))));
        list.forEach(i -> when(userRepository.findByIdAndStatus(i.getTrainer().getId(), true))
                .thenReturn(Optional.of(list.get(list.indexOf(i)).getTrainer())));

        List<UserDTO> dtos = userService.getAllTrainersForADateByTrainingClassId(1L, 1);

        assertNotNull(dtos);
        assertEquals(2, dtos.size());

        verify(trainingClassRepository).findByIdAndStatus(1L, true);
        verify(unitService).getListUnitsInASessionByTrainingClassId(1L, 1);
        units.forEach(i -> verify(trainingClassUnitInformationRepository).findByUnitAndTrainingClassAndStatus(i, tc, true));
        list.forEach(i -> verify(userRepository).findByIdAndStatus(i.getTrainer().getId(), true));
    }


    @Test
    void itShouldThrowExceptionWhenTrainingClassDateTrainersNotFound() {

        TrainingClass tc = new TrainingClass();

        TrainingClassUnitInformation t = new TrainingClassUnitInformation();
        t.setTrainingClass(tc);
        t.setStatus(false);
        TrainingClassUnitInformation t2 = new TrainingClassUnitInformation();
        t.setTrainingClass(tc);
        t2.setStatus(true);
        t2.setTrainer(user2);

        Unit u = new Unit(); u.setStatus(false);
        List<Unit> units = new ArrayList<>(List.of(u));

        when(trainingClassRepository.findByIdAndStatus(1L, true)).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> userService.getAllTrainersForADateByTrainingClassId(1L, 1));

        when(trainingClassRepository.findByIdAndStatus(1L, true)).thenReturn(Optional.of(tc));
        when(unitService.getListUnitsInASessionByTrainingClassId(1L, 1)). thenReturn(units);
        when(trainingClassUnitInformationRepository.findByUnitAndTrainingClassAndStatus(u, tc, true)).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> userService.getAllTrainersForADateByTrainingClassId(1L, 1));

        verify(trainingClassRepository, times(2)).findByIdAndStatus(1L, true);
        verify(unitService).getListUnitsInASessionByTrainingClassId(1L, 1);
        verify(trainingClassUnitInformationRepository).findByUnitAndTrainingClassAndStatus(u, tc, true);
    }
}

