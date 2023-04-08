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
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Tower")
@Table(name = "tblTower")
public class Tower implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Nationalized
    @Column(
            name = "tower_name",
            nullable = false,
            length = 100
    )
    private String towerName;

    @Lob
    @Nationalized
    @Column(
            name = "address"
    )
    private String address;

    @Column(name = "status")
    private boolean status;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "location_id")
    private Location location;

    @OneToMany(mappedBy = "tower", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<TrainingClassUnitInformation> listTrainingClassUnitInformations;
}
