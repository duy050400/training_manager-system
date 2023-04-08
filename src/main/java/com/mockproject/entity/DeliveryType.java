package com.mockproject.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "DeliveryType")
@Table(name = "tblDeliveryType")
public class DeliveryType implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "delivery_type_name",
            length = 50,
            nullable = false
    )
    private String typeName;

    @Column(name = "status")
    private boolean status;

    @OneToMany(mappedBy = "deliveryType", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<UnitDetail> listUnitDetail;
}
