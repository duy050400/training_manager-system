package com.mockproject.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Entity(name = "Session")
@Table(name = "tblSession")
public class Session implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "session_number",
            nullable = false
    )
    private int sessionNumber;

    @Column(name = "status")
    private boolean status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonManagedReference(value = "syllabus_session")
    @JoinColumn(name = "syllabus_id")
    private Syllabus syllabus;


    @OneToMany(mappedBy = "session", fetch = FetchType.LAZY)
    @JsonBackReference(value = "session_unit")
    private List<Unit> listUnit;

}
