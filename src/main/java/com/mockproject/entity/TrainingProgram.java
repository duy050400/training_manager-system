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
@Entity(name = "TrainingProgram")
@Table(name = "tblTrainingProgram")
public class TrainingProgram implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "program_id"
    )
    private int programId;

    @Lob
    @Nationalized
    @Column(
            name = "program_name"
    )
    private String name;

    @Column(
            name = "date_created",
            nullable = false
    )
    private LocalDate dateCreated;

    @Column(
            name = "last_date_modified"
    )
    private LocalDate lastDateModified;

    @Column(
            name = "duration_hour"
    )
    private BigDecimal hour;

    @Column(
            name = "duration_day"
    )
    private int day;

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

    @OneToMany(mappedBy = "trainingProgram", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<TrainingClass> listTrainingClasses;

    @OneToMany(mappedBy = "trainingProgram", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<TrainingProgramSyllabus> listTrainingProgramSyllabuses;
}
