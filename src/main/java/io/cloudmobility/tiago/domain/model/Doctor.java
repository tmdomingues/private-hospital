package io.cloudmobility.tiago.domain.model;


import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Doctors")
public class Doctor extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column
    private String specialization;

    @OneToMany(mappedBy = "doctor")
    private Set<Appointment> appointments;

    @JsonManagedReference
    @OneToMany(mappedBy = "doctor")
    private Set<Absence> absences;
}
