package io.cloudmobility.tiago.domain.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import io.cloudmobility.tiago.domain.model.Appointment;

@Repository
public interface AppointmentRepository extends PagingAndSortingRepository<Appointment, Long> {

    List<Appointment> findAppointmentByDoctorIdAndStartPeriodGreaterThanEqualAndEndPeriodLessThanEqual(
            final Long doctorId, final LocalDateTime from, final LocalDateTime to);
}
