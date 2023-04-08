package com.mockproject.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Location")
@Table(name = "tblLocation")
public class Location implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Nationalized
    @Column(
            name = "location_name",
            length = 100,
            nullable = false
    )
    private String locationName;

    @Lob
    @Nationalized
    @Column(
            name = "address"
    )
    private String address;

    @Column(name = "status")
    private boolean status;

    @OneToMany(mappedBy = "location", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Tower> listTowers;

    @OneToMany(mappedBy = "location", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<TrainingClass> listClasses;
}
