package io.cloudmobility.tiago;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import io.cloudmobility.tiago.domain.PrivateHospitalService;
import io.cloudmobility.tiago.domain.converter.AbsenceConverter;
import io.cloudmobility.tiago.domain.converter.AppointmentConverter;
import io.cloudmobility.tiago.domain.converter.DoctorConverter;
import io.cloudmobility.tiago.domain.dto.AbsenceDto;
import io.cloudmobility.tiago.domain.model.Absence;
import io.cloudmobility.tiago.domain.model.Appointment;
import io.cloudmobility.tiago.domain.model.Doctor;
import io.cloudmobility.tiago.domain.model.Patient;
import io.cloudmobility.tiago.domain.repository.AbsenceRepository;
import io.cloudmobility.tiago.domain.repository.AppointmentRepository;
import io.cloudmobility.tiago.domain.repository.DoctorRepository;
import io.cloudmobility.tiago.domain.repository.PatientRepository;
import io.cloudmobility.tiago.utils.CalendarManager;

@ExtendWith(MockitoExtension.class)
public class PrivateHospitalServiceTest {

    @InjectMocks private PrivateHospitalService underTest;

    @Spy private AbsenceConverter absenceConverter;
    @Spy private CalendarManager calendarManager;

    @Mock private AbsenceRepository absenceRepository;
    @Mock private AppointmentRepository appointmentRepository;
    @Mock private AppointmentConverter appointmentConverter;
    @Mock private DoctorRepository doctorRepository;
    @Mock private DoctorConverter doctorConverter;
    @Mock private PatientRepository patientRepository;

    @Test
    public void getDoctorAppointments_validInput_success() {

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(getDoctor()));

        final var doctorAppointments =
                underTest.getDoctorAppointments(1L, LocalDateTime.now(), LocalDateTime.now().plusHours(2));

        assertThat(doctorAppointments, notNullValue());
    }

    @Test
    public void setDoctorTimeOff_nonOverlappingPeriod_success() {

        final var doctor = getDoctor();
        final var startOfExistingAbsencePeriod = LocalDateTime.of(2021, 11, 19, 14, 0);
        final var absenceRequestDto = new AbsenceDto(startOfExistingAbsencePeriod, startOfExistingAbsencePeriod.plusHours(2), "Stuff");

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(absenceRepository.findAbsencesByDoctorForGivenPeriod(doctor, absenceRequestDto.getFrom(), absenceRequestDto.getTo()))
                .thenReturn(getAbsence());
        when(absenceRepository.save(any(Absence.class))).thenReturn(new Absence());

        final var absence = underTest.setDoctorAbsence(1L, absenceRequestDto);

        assertThat(absence, notNullValue());
    }

    @Test
    public void setDoctorTimeOff_overlappingPeriod_fail() {

        final var doctor = getDoctor();
        final var startOfExistingAbsencePeriod = LocalDateTime.of(2021, 11, 19, 14, 0);
        final var overlappingAbsenceRequest = new AbsenceDto(startOfExistingAbsencePeriod, startOfExistingAbsencePeriod.plusHours(6), "Stuff");

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(absenceRepository.findAbsencesByDoctorForGivenPeriod(doctor, overlappingAbsenceRequest.getFrom(), overlappingAbsenceRequest.getTo()))
                .thenReturn(getAbsence());

        final var thrown = assertThrows(IllegalArgumentException.class, () -> {
            underTest.setDoctorAbsence(1L, overlappingAbsenceRequest);
        });

        assertEquals(thrown.getMessage(), "Defined time off overlaps with one existing period");
    }

    @Test
    public void getDoctorAvailability_validInput_success() {
        final var doctor = getDoctor();
        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));
        when(appointmentRepository.findAppointmentByDoctorIdAndStartPeriodGreaterThanEqualAndEndPeriodLessThanEqual(
                any(Long.class), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(Collections.emptyList());
        when(absenceRepository.findAbsencesByDoctorForGivenPeriod(
                any(Doctor.class), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(Collections.emptyList());


        underTest.getDoctorAvailability(doctor.getId(), LocalDateTime.now(), LocalDateTime.now().plusHours(2));
    }

    @Test
    public void scheduleAppointment_nothingScheduled_success() {
        final var patient = getPatient();
        final var doctor = getDoctor();
        final var startOfAppointment = LocalDateTime.of(2021, 11, 19, 14, 0);

        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(patient.getId())).thenReturn(Optional.of(patient));

        underTest.scheduleAppointment(patient.getId(), doctor.getId(), startOfAppointment, "Description");
    }

    @Test
    public void scheduleAppointment_unavailableSlot_fail() {
        final var patient = getPatient();
        final var doctor = getDoctor();
        final var startOfAppointment = LocalDateTime.of(2021, 11, 19, 14, 0);

        final Appointment app = new Appointment();
        app.setStartPeriod(startOfAppointment);
        app.setEndPeriod(startOfAppointment.plusHours(1));

        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(patient.getId())).thenReturn(Optional.of(patient));
        when(appointmentRepository.findAppointmentByDoctorIdAndStartPeriodGreaterThanEqualAndEndPeriodLessThanEqual(
                doctor.getId(), startOfAppointment, startOfAppointment.plusHours(1))).thenReturn(Collections.singletonList(app));


        final var thrown = assertThrows(IllegalArgumentException.class, () -> {
            underTest.scheduleAppointment(patient.getId(), doctor.getId(), startOfAppointment, "Description");
        });

        assertEquals(thrown.getMessage(), "Requested timeslot is unavailable");
    }


    /* ******************* Tests Fixtures ****************** */

    private Doctor getDoctor() {
        final var dr = new Doctor();
        dr.setId(1L);
        return dr;
    }

    private Patient getPatient() {
        final var patient = new Patient();
        patient.setId(1L);
        return patient;
    }

    // Absence for the November 19, 2021 from 4pm to 7pm
    private List<Absence> getAbsence() {
        final Absence existingAbsence = new Absence();
        existingAbsence.setId(1L);
        existingAbsence.setReason("");
        existingAbsence.setStartPeriod(LocalDateTime.of(2021, 11, 19, 16, 0));
        existingAbsence.setEndPeriod(LocalDateTime.of(2021, 11, 19, 19, 0));
        return Collections.singletonList(existingAbsence);
    }
}
