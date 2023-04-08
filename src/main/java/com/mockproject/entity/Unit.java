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
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Unit")
@Table(name = "tblUnit")
public class Unit implements Serializable {
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
            name = "unit_title",
            nullable = false
    )
    private String unitTitle;

    @Column(
            name = "unit_number",
            nullable = false,
            unique = false
    )
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int unitNumber;

    @Column(
            name = "unit_duration"
    )
    private BigDecimal duration;

    @Column(name = "status")
    private boolean status;

    @ManyToOne
    @JsonManagedReference(value = "session_unit")
    @JoinColumn(name = "session_id")
    private Session session;

    @OneToMany(mappedBy = "unit", fetch = FetchType.LAZY)
    @JsonBackReference(value = "unit_unitdetail")
    private List<UnitDetail> listUnitDetail;

    @OneToMany(mappedBy = "unit", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<TrainingClassUnitInformation> listTrainingClassUnitInformations;
}
