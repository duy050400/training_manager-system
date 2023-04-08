package com.mockproject.repository;

import com.mockproject.entity.TrainingClass;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
public interface TrainingClassRepository extends JpaRepository<TrainingClass, Long>, JpaSpecificationExecutor<TrainingClass> {

    Optional<TrainingClass> findByIdAndStatus(Long id, Boolean status);

    List<TrainingClass> findByClassNameContaining(String name);

    @Query("SELECT c FROM TrainingClass c " +
            "WHERE c.status = ?1 " +
            "AND (:#{#locationId.size()} = 0 OR c.location.id IN ?2) " +
            "AND (?3 IS NULL OR ?4 IS NULL OR c.startDate BETWEEN ?3 AND ?4) " +
            "AND (:#{#period.size()} = 0 OR c.period IN ?5) " +
            "AND c.attendee.attendeeName LIKE '%' + ?6 + '%' " +
            "AND (:#{#state.size()} = 0 OR c.state IN ?7) " +
            "AND (:#{#attendeeId.size()} = 0 OR c.attendee.id IN ?8) " +
            "AND (?9 = 0 OR c.fsu.id = ?9) " +
            "AND (:#{#classId.size()} = 0 OR c.id IN ?10) " +
            "AND (c.className LIKE '%' + ?11 + '%' OR c.classCode LIKE '%' + ?11 + '%' " +
            "OR c.creator.fullName LIKE '%' + ?11 + '%')")
    List<TrainingClass> getListClass(boolean status, List<Long> locationId, LocalDate fromDate, LocalDate toDate,
                                     List<Integer> period, String isOnline, List<String> state, List<Long> attendeeId,
                                     Long fsu, List<Long> classId, String search, Sort sort);

    List<TrainingClass> findAllByStatus(boolean status);

    List<TrainingClass> findAllByListClassSchedulesDate(LocalDate date);

    @Query("SELECT tc FROM TrainingClass tc " +
            "LEFT JOIN tc.listClassSchedules cs " +
            "LEFT JOIN tc.listTrainingClassAdmins ta " +
            "LEFT JOIN tc.listTrainingClassUnitInformations ti "+
            "LEFT JOIN tc.location lo " +
            "LEFT JOIN tc.attendee at " +
            "WHERE " +
            "(tc.className LIKE (:SearchText) OR " +
            "tc.classCode LIKE (:SearchText)  OR " +
            "tc.fsu.fsuName LIKE (:SearchText) OR " +
            "ta.admin.fullName LIKE (:SearchText) OR " +
            "at.attendeeName LIKE (:SearchText) OR " +
            "ti.trainer.fullName LIKE (:SearchText) OR "+
            "lo.locationName LIKE (:SearchText)) AND " +
            "cs.date = :date AND " +
            "tc.status = true AND " +
            "cs.status = true ")
    public List<TrainingClass> findAllBySearchTextAndListClassSchedulesDate(
            @Param("SearchText") String SearchText,
            @Param("date") LocalDate date);

    @Query("SELECT tc FROM TrainingClass tc " +
            "LEFT JOIN tc.listClassSchedules cs " +
            "LEFT JOIN tc.listTrainingClassAdmins ta " +
            "LEFT JOIN tc.location lo " +
            "LEFT JOIN tc.listTrainingClassUnitInformations ti "+
            "LEFT JOIN tc.attendee at " +
            "WHERE " +
            "(tc.className LIKE (:searchText) OR " +
            "tc.classCode LIKE (:searchText)  OR " +
            "tc.fsu.fsuName LIKE (:searchText) OR " +
            "ta.admin.fullName LIKE (:searchText) OR " +
            "at.attendeeName LIKE (:searchText) OR " +
            "ti.trainer.fullName LIKE (:searchText) OR "+
            "lo.locationName LIKE (:searchText)) AND " +
            "cs.date BETWEEN :startDate AND :endDate AND " +
            "tc.status = true AND " +
            "cs.status = true ")
    public List<TrainingClass> findAllBySearchTextAndListClassSchedulesWeek(
            @Param("searchText") String searchText,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

}
