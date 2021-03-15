package io.cloudmobility.tiago.domain.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import io.cloudmobility.tiago.domain.model.Absence;
import io.cloudmobility.tiago.domain.model.Doctor;

@Repository
public interface AbsenceRepository extends CrudRepository<Absence, Long> {

    @Query("SELECT a FROM Absence a WHERE NOT (a.startPeriod > :to OR a.endPeriod < :from) AND a.doctor = :doctor")
    List<Absence> findAbsencesByDoctorForGivenPeriod(final Doctor doctor, final LocalDateTime from, final LocalDateTime to);
}
