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
@Entity(name = "OutputStandard")
@Table(name = "tblOutputStandard")
public class OutputStandard implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "output_standard_code",
            length = 10,
            nullable = false
    )
    private String standardCode;

    @Column(
            name = "output_standard_name",
            length = 50
    )
    private String standardName;

    @Lob
    @Nationalized
    @Column(
            name = "description"
    )
    private String description;

    @Column(name = "status")
    private boolean status;

    @OneToMany(mappedBy = "outputStandard", fetch = FetchType.EAGER)
    @JsonBackReference
    private List<UnitDetail> listUnitDetail;
}
