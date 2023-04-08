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
@Entity(name = "TrainingClassUnitInformation")
@Table(name = "tblTrainingClassUnitInformation")
public class TrainingClassUnitInformation implements Serializable {
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
    @JoinColumn(name = "trainer_id")
    private User trainer;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "unit_id")
    private Unit unit;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "training_class_id")
    private TrainingClass trainingClass;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "tower_id")
    private Tower tower;
}
