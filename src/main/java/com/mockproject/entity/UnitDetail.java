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
@Entity(name = "UnitDetail")
@Table(name = "tblUnitDetail")
public class UnitDetail implements Serializable {
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
            name = "unit_detail_title",
            nullable = false
    )
    private String title;

    @Column(
            name = "unit_detail_duration"
    )
    private BigDecimal duration;

    @Column(
            name = "unit_detail_type"
    )
    private boolean type;

    @Column(name = "status")
    private boolean status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonManagedReference(value = "unit_unitdetail")
    @JoinColumn(name = "unit_id")
    private Unit unit;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "delivery_type_id")
    private DeliveryType deliveryType;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "output_standard_id")
    private OutputStandard outputStandard;

    @OneToMany(mappedBy = "unitDetail", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<TrainingMaterial> listMaterials;
}
