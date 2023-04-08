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
@Entity(name = "Attendee")
@Table(name = "tblAttendee")
public class Attendee implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;
    @Column(
            name = "attendee_name",
            length = 50,
            nullable = false
    )
    private String attendeeName;

    @Lob
    @Nationalized
    @Column(
            name = "description"
    )
    private String description;

    @Column(name = "status")
    private boolean status;

    @OneToMany(mappedBy = "attendee", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<User> listUser;

    @OneToMany(mappedBy = "attendee", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<TrainingClass> listTrainingClasses;
}
