package com.mockproject.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Syllabus")
@Table(name = "tblSyllabus")
public class Syllabus implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Lob
    @Nationalized
    @Column(
            name = "syllabus_name",
            nullable = false
    )
    private String name;

    @Column(
            name = "syllabus_code",
            length = 50
    )
    private String code;

    @Column(
            name = "syllabus_version",
            length = 50
    )
    private String version;

    @Column(
            name = "syllabus_level",
            length = 50
    )
    private String level;

    @Column(
            name = "attendee_amount"
    )
    private int attendee;

    @Column(
            name = "duration_hour"
    )
    private BigDecimal hour;

    @Column(
            name = "duration_day"
    )
    private int day;

    @Lob
    @Nationalized
    @Column(
            name = "technical_requirements"
    )
    private String technicalRequirements;

    @Lob
    @Nationalized
    @Column(
            name = "course_objectives"
    )
    private String courseObjectives;

    @Column(
            name = "date_created"
    )
    private LocalDate dateCreated;

    @Column(
            name = "last_date_modified"
    )
    private LocalDate lastDateModified;

    @Column(
            name = "quiz"
    )
    private BigDecimal quiz;

    @Column(
            name = "assignment"
    )
    private BigDecimal assignment;

    @Column(
            name = "final"
    )
    private BigDecimal finalExam;

    @Column(
            name = "final_theory"
    )
    private BigDecimal finalTheory;

    @Column(
            name = "final_practice"
    )
    private BigDecimal finalPractice;

    @Column(
            name = "GPA"
    )
    private BigDecimal gpa;

    @Lob
    @Nationalized
    @Column(
            name = "training_description"
    )
    private String trainingDes;

    @Lob
    @Nationalized
    @Column(
            name = "re_test_description"
    )
    private String reTestDes;

    @Lob
    @Nationalized
    @Column(
            name = "marking_description"
    )
    private String markingDes;

    @Lob
    @Nationalized
    @Column(
            name = "waiver_criteria_description"
    )
    private String waiverCriteriaDes;

    @Lob
    @Nationalized
    @Column(
            name = "other_description"
    )
    private String otherDes;

    @Column(name = "state")
    private boolean state;

    @Column(name = "status")
    private boolean status;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "creator_id")
    private User creator;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "last_modifier_id")
    private User lastModifier;


    @OneToMany(mappedBy = "syllabus", fetch = FetchType.LAZY)
    @JsonBackReference(value = "syllabus_session")
    private List<Session> listSessions;

    @OneToMany(mappedBy = "syllabus", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<TrainingProgramSyllabus> listTrainingProgramSyllabuses;
}
