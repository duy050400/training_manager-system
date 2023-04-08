package com.mockproject.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "TrainingProgramSyllabus")
@Table(name = "tblTrainingProgramSyllabus")
public class TrainingProgramSyllabus implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(name = "status")
    private boolean status;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "syllabus_id")
    private Syllabus syllabus;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "training_program_id")
    private TrainingProgram trainingProgram;
}
