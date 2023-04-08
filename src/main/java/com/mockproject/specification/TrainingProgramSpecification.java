package com.mockproject.specification;

import com.mockproject.dto.SearchTPDTO;
import com.mockproject.entity.TrainingProgram;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.stream.Collectors;

public class TrainingProgramSpecification {

    public static Specification<TrainingProgram> hasNameLike(List<String> searchText) {
        Specification<TrainingProgram> trainingProgramSpecification = null;
        searchText = searchText.stream().map(text -> "%" + text + "%").collect(Collectors.toList());
        for (String text : searchText) {
            if (trainingProgramSpecification == null) {
                trainingProgramSpecification = (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), text);
            } else {
                trainingProgramSpecification = trainingProgramSpecification.or(trainingProgramSpecification = (root, query, criteriaBuilder) ->
                        criteriaBuilder.like(root.get("name"), text));
            }
        }
        return trainingProgramSpecification;
    }

    public static Specification<TrainingProgram> hasCreatorNameLike(List<String> searchText) {
        Specification<TrainingProgram> trainingProgramSpecification = null;
        searchText = searchText.stream().map(text -> "%" + text.trim() + "%").collect(Collectors.toList());
        for (String text : searchText) {
            if (trainingProgramSpecification == null) {
                trainingProgramSpecification = (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("creator").get("fullName"), text);
            } else {
                trainingProgramSpecification = trainingProgramSpecification.or(trainingProgramSpecification = (root, query, criteriaBuilder) ->
                        criteriaBuilder.like(root.get("name").get("fullName"), text));
            }
        }
        return trainingProgramSpecification;
    }
    public static Specification<TrainingProgram> hasStatus() {
        return (root, query, builder) -> builder.equal(root.get("status"), true);
    }
    public static Specification<TrainingProgram> getTrainingProgramSpecification(SearchTPDTO filter) {
        Specification<TrainingProgram> spec = Specification.where(null);
        spec = spec.or(hasNameLike(filter.getSearch())).or(hasCreatorNameLike(filter.getSearch())).and(hasStatus());
        return spec;
    }
}
