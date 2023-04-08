package com.mockproject.specification;

import com.mockproject.dto.TimeRangeDTO;
import com.mockproject.dto.TrainingClassFilterRequestDTO;
import com.mockproject.entity.ClassSchedule;
import com.mockproject.entity.TrainingClass;
import jakarta.persistence.criteria.Join;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class TrainingClassSpecification {

    public static Specification<TrainingClass> hasLocationIn(List<String> locations) {
//        return (root, query, builder) -> root.get("location").get("locationName").in(locations);
        Specification<TrainingClass> trainingClassSpecification = null;
        locations = locations.stream().map(local -> "%" + local + "%").collect(Collectors.toList());
        for (String location : locations) {
            if (trainingClassSpecification == null) {
                trainingClassSpecification = (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("location").get("locationName"), location);
            } else {
                trainingClassSpecification = trainingClassSpecification.or(trainingClassSpecification = (root, query, criteriaBuilder) ->
                        criteriaBuilder.like(root.get("location").get("locationName"), location));
            }
        }
        return trainingClassSpecification;

    }

    public static Specification<TrainingClass> hasClassScheduleBetween(LocalDate startDate, LocalDate endDate) {
        return (root, query, builder) -> {
            Join<TrainingClass, ClassSchedule> classSchedule = root.join("listClassSchedules");
            return builder.between(classSchedule.get("date"), startDate, endDate);
        };
    }

    public static Specification<TrainingClass> hasClassSchedule(LocalDate date) {
        return (root, query, builder) -> {

            Join<TrainingClass, ClassSchedule> classSchedule = root.join("listClassSchedules");
            return builder.equal(classSchedule.get("date"), date);
        };
    }

    public static Specification<TrainingClass> hasStartTimeBetween(List<TimeRangeDTO> timeRangeList) {
        Specification<TrainingClass> trainingClassSpecification = null;
        for (TimeRangeDTO timeRange : timeRangeList) {
            log.info(timeRange.getFrom().toString()+" "+timeRange.getTo().toString());
            if (trainingClassSpecification == null) {
                trainingClassSpecification = (root, query, criteriaBuilder) -> {
                    return criteriaBuilder.between(root.get("startTime"), timeRange.getFrom(), timeRange.getTo());
                };
            } else {
                trainingClassSpecification = trainingClassSpecification.or(trainingClassSpecification = (root, query, criteriaBuilder) ->
                        criteriaBuilder.between(root.get("startTime"), timeRange.getFrom(), timeRange.getTo()));
            }
        }
        return trainingClassSpecification;
    }

    public static Specification<TrainingClass> hasStatusIn(List<String> Status) {
        return (root, query, criteriaBuilder) -> root.get("state").in(Status);
    }

    public static Specification<TrainingClass> hasAttendeeAttendeeName(List<String> attendeeName) {
        return (root, query, criteriaBuilder) -> root.get("attendee").get("attendeeName").in(attendeeName);
    }

    public static Specification<TrainingClass> hasFsu(String fsu) {
        return (root, query, builder) -> builder.equal(root.get("fsu").get("fsuName"), fsu);
    }

    public static Specification<TrainingClass> hasTrainer(String trainer) {
        return (root, query, builder) -> builder.equal(root.get("listTrainingClassUnitInformations")
                .get("trainer")
                .get("fullName"), trainer);
    }

    public static Specification<TrainingClass> hasStatus() {
        return (root, query, builder) -> builder.equal(root.get("status"), true);
    }

    public static Specification<TrainingClass> findByFilterWeek(TrainingClassFilterRequestDTO filter) {


        Specification<TrainingClass> spec = Specification.where(hasClassScheduleBetween(filter.getStartDate(), filter.getEndDate()))
                .and(hasStatus());
        return getTrainingClassSpecification(filter, spec);
    }

    public static Specification<TrainingClass> findByFilterDate(TrainingClassFilterRequestDTO filter) {


        Specification<TrainingClass> spec = Specification.where(hasClassSchedule(filter.getNowDate()))
                .and(hasStatus());
        return getTrainingClassSpecification(filter, spec);
    }


    private static Specification<TrainingClass> getTrainingClassSpecification(TrainingClassFilterRequestDTO filter, Specification<TrainingClass> spec) {
        if (filter.getLocations() != null && !filter.getLocations().isEmpty()) {
            spec = spec.and(hasLocationIn(filter.getLocations()));
        }

        if (filter.getTimeRanges() != null && !filter.getTimeRanges().isEmpty()) {
            spec = spec.and(hasStartTimeBetween(filter.getTimeRanges()));
        }
        if (filter.getStatuses() != null && !filter.getStatuses().isEmpty()) {
            spec = spec.and(hasStatusIn(filter.getStatuses()));
        }
        if (filter.getAttendees() != null && !filter.getAttendees().isEmpty()) {
            spec = spec.and(hasAttendeeAttendeeName(filter.getAttendees()));
        }
        if (filter.getFsu() != null) {
            spec = spec.and(hasFsu(filter.getFsu()));
        }
        if (filter.getTrainer() != null) {
            spec = spec.and(hasTrainer(filter.getTrainer()));
        }
        return spec;
    }
}
