package io.cloudmobility.tiago;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import io.cloudmobility.tiago.domain.PrivateHospitalService;
import io.cloudmobility.tiago.domain.converter.AbsenceConverter;
import io.cloudmobility.tiago.domain.converter.AppointmentConverter;
import io.cloudmobility.tiago.domain.converter.DoctorConverter;
import io.cloudmobility.tiago.domain.dto.AbsenceRequestDto;
import io.cloudmobility.tiago.domain.model.Absence;
import io.cloudmobility.tiago.domain.model.Doctor;
import io.cloudmobility.tiago.domain.repository.AbsenceRepository;
import io.cloudmobility.tiago.domain.repository.AppointmentRepository;
import io.cloudmobility.tiago.domain.repository.DoctorRepository;
import io.cloudmobility.tiago.domain.repository.PatientRepository;
import io.cloudmobility.tiago.utils.CalendarManager;

@ExtendWith(MockitoExtension.class)
public class PrivateHospitalServiceTest {

    @InjectMocks private PrivateHospitalService underTest;

    @Spy private AbsenceConverter absenceConverter;

    @Mock private AbsenceRepository absenceRepository;
    @Mock private AppointmentRepository appointmentRepository;
    @Mock private AppointmentConverter appointmentConverter;
    @Mock private DoctorRepository doctorRepository;
    @Mock private DoctorConverter doctorConverter;
    @Mock private PatientRepository patientRepository;
    @Mock private CalendarManager calendarManager;

    @Test
    public void getDoctorAppointments_validInput_success() {

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(getStubDoctor()));

        final var doctorAppointments =
                underTest.getDoctorAppointments(1L, LocalDateTime.now(), LocalDateTime.now().plusHours(2));

        assertThat(doctorAppointments, notNullValue());
    }

    @Test
    public void setDoctorTimeOff_validInput_success() {

        final var absenceRequest = new AbsenceRequestDto(LocalDateTime.now(), LocalDateTime.now().plusHours(2), "Stuff");

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(getStubDoctor()));
        when(absenceRepository.save(Mockito.any(Absence.class))).thenReturn(new Absence());

        final var absence = underTest.setDoctorAbsence(1L, absenceRequest);

        assertThat(absence, notNullValue());
    }

    /* ******************* Tests Fixtures ****************** */
    private Doctor getStubDoctor() {
        return new Doctor();
    }
}
