package com.mockproject.service.interfaces;

import com.mockproject.dto.UserDTO;
import com.mockproject.entity.User;
import org.springframework.data.domain.Page;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

public interface IUserService {

    List<User> csvToUsers(InputStream is, Boolean replace, Boolean skip);

    String readCSVFile(File file);

    Page<UserDTO> searchByFilter(List<String> search, String dob, Boolean gender,  List<Long> atendeeId, Optional<Integer> page, Optional<Integer> size, List<String> sort) throws Exception;

    boolean updateStatus(Long id);

    List<UserDTO> listClassAdminTrue();

    List<UserDTO> listTrainerTrue();

    Integer updateStateToFalse(Long id);

    UserDTO getUserById(Long id);

    Integer updateStateToTrue(Long id);

    boolean changeRole(Long id, Long roleId);


    boolean editUser(UserDTO user);


    UserDTO getUserById(boolean status, Long id);

    int getStateIdByStateName(String name);

    InputStream getCSVUserFileExample();

    void storeListUser(List<User> list);

    List<UserDTO> getAllTrainersByTrainingClassId(long id);

    List<UserDTO> getAllAdminsByTrainingClassId(long id);

    UserDTO getCreatorByTrainingClassId(long id);

    List<UserDTO> getAllTrainersForADateByTrainingClassId(long id, int dayNth);


}
