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
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "TrainingClass")
@Table(name = "tblTrainingClass")
public class TrainingClass implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Nationalized
    @Column(
            name = "class_name",
            nullable = false,
            length = 50
    )
    private String className;

    @Column(
            name = "class_code",
            length = 50
    )
    private String classCode;

    @Column(
            name = "start_date",
            nullable = false
    )
    private LocalDate startDate;

    @Column(
            name = "start_time",
            nullable = false
    )
    private Time startTime;

    @Column(
            name = "end_time",
            nullable = false
    )
    private Time endTime;

    @Column(
            name = "duration_hour"
    )
    private BigDecimal hour;

    @Column(
            name = "duration_day"
    )
    private int day;

    @Column(
            name = "planned_amount"
    )
    private int planned;

    @Column(
            name = "accepted_amount"
    )
    private int accepted;

    @Column(
            name = "actual_amount"
    )
    private int actual;

    @Column(
            name = "state",
            length = 50
    )
    private String state;

    @Column(
            name = "date_created",
            nullable = false
    )
    private LocalDate dateCreated;

    @Column(
            name = "date_reviewed"
    )
    private LocalDate dateReviewed;

    @Column(
            name = "date_approved"
    )
    private LocalDate dateApproved;

    @Column(
            name = "last_date_modified"
    )
    private LocalDate lastDateModified;

    @Column(
            name = "period"
    )
    private int period;

    @Column(name = "status")
    private boolean status;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "attendee_id")
    private Attendee attendee;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "training_program_id")
    private TrainingProgram trainingProgram;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "fsu_id")
    private Fsu fsu;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "contact_id")
    private Contact contact;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "creator_id")
    private User creator;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "last_modifier_id")
    private User lastModifier;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "reviewer_id")
    private User reviewer;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "approver_id")
    private User approver;

    @OneToMany(mappedBy = "trainingClass", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<TrainingClassUnitInformation> listTrainingClassUnitInformations;

    @OneToMany(mappedBy = "trainingClass", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<ClassSchedule> listClassSchedules;

    @OneToMany(mappedBy = "trainingClass", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<TrainingClassAdmin> listTrainingClassAdmins;


}
