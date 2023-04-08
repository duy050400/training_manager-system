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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "TrainingMaterial")
@Table(name = "tblTrainingMaterial")
public class TrainingMaterial implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "upload_date"
    )
    private LocalDate uploadDate;

    @Lob
    @Nationalized
    @Column(
            name = "data"
    )
    private String data;

    @Lob
    @Nationalized
    @Column(
            name = "name",
            nullable = false
    )
    private String name;

    @Column(
            name = "type",
            length = 200
    )
    private String type;

    @Column(
            name = "size"
    )
    private BigDecimal size;

    @Column(name = "status")
    private boolean status;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "unit_detail_id")
    private UnitDetail unitDetail;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "user_id")
    private User user;
}
