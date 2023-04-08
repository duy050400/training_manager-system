package com.mockproject.repository;

import com.mockproject.entity.Fsu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FsuRepository extends JpaRepository<Fsu, Long> {

    List<Fsu> findByStatus(boolean status);

    Optional<Fsu> findByFsuNameAndStatus(String name, boolean status);

    Optional<Fsu> findByStatusAndId(boolean status, Long id);

    List<Fsu> findAllByStatus(boolean status);
}
