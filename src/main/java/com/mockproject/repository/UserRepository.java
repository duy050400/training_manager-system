package com.mockproject.repository;

import com.mockproject.entity.Role;
import com.mockproject.entity.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

   Optional<User> findByEmailAndStatus(String email, Boolean status);
    Optional<User> findByStatusAndId(boolean status, long id);

    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);

    List<User> findAllByStatus(boolean status);

    Optional<User> findByIdAndStatus(Long id, Boolean status);

    Optional<User> findByPhone(String phone);


    List<User> findAllBy();

    @Query(value = "SELECT * FROM tblUser\n" +
            "ORDER BY ID\n" +
            "OFFSET ((?1-1)*?2) ROWS FETCH NEXT ?2 ROWS ONLY", nativeQuery = true)
    List<User> getAllByPageAndRowPerPage(Long page, Long rowPerPage);

    @Query(value = "select u from User u "+
            "where " +
            " (:dob is null or u.dob = :dob)" +
            "and (:gender is null or u.gender = :gender)" +
            "and (:#{#attendeeId.size()} = 0 or u.attendee.id in :attendeeId)" +
            "and u.status = true " +
            "and ( u.email like '%'+:search+'%' or u.fullName like '%'+:search+'%' or u.phone like '%'+:search+'%' or" +
            " u.level.levelCode like '%'+:search+'%' or u.role.roleName like '%'+:search+'%')"
    )
    List<User> searchByFilter(String search , LocalDate dob, Boolean gender, List<Long> attendeeId, Sort sort);

    List<User> findByRoleAndStatus(Role role, boolean status);

    Optional<User> findByEmailAndStatus(String email, boolean status);

}
