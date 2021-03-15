package io.cloudmobility.tiago.domain.model;

import java.time.LocalDateTime;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column
    private LocalDateTime fromDatetime;

    @NotNull
    @Column
    private LocalDateTime toDatetime;

    @Column
    private String description;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    private Doctor doctor;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    private Patient patient;
}
