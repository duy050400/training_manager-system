package com.mockproject.repository;

import com.mockproject.entity.Session;
import com.mockproject.entity.Syllabus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    List<Session> findBySyllabus(Syllabus syllabus);

    Optional<List<Session>> findBySyllabusIdAndStatus(Long syllabusID, boolean status);

    Optional<Session> findByIdAndStatus(Long id, boolean status);

    List<Session> getSessionListBySyllabusId(Long idSyllabus);
}
