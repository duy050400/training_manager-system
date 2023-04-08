package com.mockproject.repository;

import com.mockproject.entity.Attendee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttendeeRepository extends JpaRepository<Attendee, Long> {

    List<Attendee> findByStatus(boolean status);

    Optional<Attendee> findByAttendeeNameAndStatus(String name, boolean status);

    List<Attendee> findAllByStatusIs(boolean status);

    Optional<Attendee> findByStatusAndId(boolean status, Long id);
}
