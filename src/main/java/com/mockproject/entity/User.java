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
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "User")
@Table(
        name = "tblUser",
        uniqueConstraints = {
                @UniqueConstraint(name = "user_email_unique", columnNames = "email"),
                @UniqueConstraint(name = "user_phone_unique", columnNames = "phone")
        }
)
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "email",
            updatable = false
    )
    private String email;

    @Lob
    @Column(
            name = "password",
            nullable = false
    )
    private String password;

    @Lob
    @Nationalized
    @Column(
            name = "fullname"
    )
    private String fullName;

    @Lob
    @Nationalized
    @Column(
            name = "image"
    )
    private String image;

    @Column(
            name = "state"
    )
    private int state;

    @Column(
            name = "date_of_birth"
    )
    private LocalDate dob;

    @Column(
            name = "phone",
            length = 17
    )
    private String phone;

    @Column(
            name = "gender",
            nullable = false
    )
    private boolean gender;

    @Column(name = "status")
    private boolean status;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "level_id")
    private Level level;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "attendee_id")
    private Attendee attendee;

    @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<TrainingClass> listTrainingClassCreates;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<TrainingMaterial> listMaterials;

    @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Syllabus> listSyllabusCreates;

    @OneToMany(mappedBy = "lastModifier", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Syllabus> listSyllabusLastModifiers;

    @OneToMany(mappedBy = "lastModifier", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<TrainingClass> listTrainingClassModifies;

    @OneToMany(mappedBy = "reviewer", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<TrainingClass> listTrainingClassReviews;

    @OneToMany(mappedBy = "approver", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<TrainingClass> listTrainingClassApproves;

    @OneToMany(mappedBy = "trainer", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<TrainingClassUnitInformation> listTrainingClassUnitInformations;

    @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<TrainingProgram> listTrainingProgramCreates;

    @OneToMany(mappedBy = "lastModifier", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<TrainingProgram> listTrainingProgramModifiers;

    @OneToMany(mappedBy = "admin", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<TrainingClassAdmin> listTrainingClassAdmins;
}
