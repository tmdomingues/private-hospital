package io.cloudmobility.tiago.domain;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import io.cloudmobility.tiago.domain.converter.AbsenceConverter;
import io.cloudmobility.tiago.domain.converter.AppointmentConverter;
import io.cloudmobility.tiago.domain.converter.DoctorConverter;
import io.cloudmobility.tiago.domain.dto.AbsenceRequestDto;
import io.cloudmobility.tiago.domain.dto.AppointmentResponseDto;
import io.cloudmobility.tiago.domain.dto.CalendarEventDto;
import io.cloudmobility.tiago.domain.dto.DoctorDto;
import io.cloudmobility.tiago.domain.model.Absence;
import io.cloudmobility.tiago.domain.model.Appointment;
import io.cloudmobility.tiago.domain.repository.AbsenceRepository;
import io.cloudmobility.tiago.domain.repository.AppointmentRepository;
import io.cloudmobility.tiago.domain.repository.DoctorRepository;
import io.cloudmobility.tiago.domain.repository.PatientRepository;
import io.cloudmobility.tiago.utils.CalendarManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrivateHospitalService {

    private final AbsenceRepository absenceRepository;
    private final AbsenceConverter absenceConverter;
    private final AppointmentRepository appointmentRepository;
    private final AppointmentConverter appointmentConverter;
    private final CalendarManager calendarManager;
    private final DoctorRepository doctorRepository;
    private final DoctorConverter doctorConverter;
    private final PatientRepository patientRepository;

    /**
     * doctor domain operation for fetching his/her appointments for a given time period.
     *
     * @param doctorId the doctor id
     * @param from     start time of the appointment
     * @param to       end time of the appointment
     * @return a doctor's scheduled appointments
     */
    public List<AppointmentResponseDto> getDoctorAppointments(final Long doctorId,
                                                              final LocalDateTime from,
                                                              final LocalDateTime to) {

        final var doctor = doctorRepository.findById(doctorId);

        log.info("Searching appointments for {}:", doctor.orElseThrow().getName());

        final List<Appointment> appointments = appointmentRepository
                .findAppointmentByDoctorIdAndFromDatetimeGreaterThanEqualAndToDatetimeLessThanEqual(
                        doctor.orElseThrow().getId(), from, to);

        return appointments.stream()
                .map(appointmentConverter::convert)
                .collect(Collectors.toList());
    }

    /**
     * doctor domain operation for setting time off.
     *
     * @param doctorId          the doctor id
     * @param absenceRequestDto request for the absence period
     * @return the created absence
     */
    public Absence setDoctorAbsence(final Long doctorId, final AbsenceRequestDto absenceRequestDto) {
        final var doctor = doctorRepository.findById(doctorId).orElseThrow();

        checkIfNewAbsenceRequestOverlapsWithExistingAbsentPeriods(doctorId, absenceRequestDto);

        log.info("Setting time off for doctor: {}", doctor.getName());
        final var absence = absenceConverter.convert(absenceRequestDto);
        assert absence != null;
        absence.setDoctor(doctor);

        return absenceRepository.save(absence);
    }

    /**
     * patient domain operation for scheduling a medical appointment with a given doctor.
     *
     * @param doctorId the doctor id
     * @param from     start of appointment
     * @param to       end of appointment
     * @return the appointment
     */
    public Appointment scheduleAppointment(final Long patientId,
                                           final Long doctorId,
                                           final LocalDateTime from,
                                           final LocalDateTime to,
                                           final String description) {

        final var doctor = doctorRepository.findById(doctorId).orElseThrow();
        final var patient = patientRepository.findById(patientId).orElseThrow();

        final var calendarEvent = new CalendarEventDto(from, to);
        if (!calendarManager.checkIfPeriodIsWithinWeekRange(calendarEvent)) {
            throw new IllegalArgumentException("Please select the maximum period of a week");
        }

        if (canScheduleAppointment(doctorId, from, to)) {
            log.info("Scheduling appointment for patient {} with doctor {} " + "from {} to {}", patient, doctor, from, to);
            final Appointment appointment = new Appointment();
            appointment.setDoctor(doctor);
            appointment.setPatient(patient);
            appointment.setFromDatetime(from);
            appointment.setToDatetime(to);
            appointment.setDescription(description);

            return appointmentRepository.save(appointment);
        } else {
            log.error("Requested timeslot is unavailable for doctor {}", doctor);
            throw new IllegalArgumentException("Requested timeslot is unavailable for doctor");
        }
    }

    private boolean canScheduleAppointment(final Long doctorId, final LocalDateTime from, final LocalDateTime to) {

        final var desiredTimeslot = new CalendarEventDto(from, to);

        final var appointments = appointmentRepository
                .findAppointmentByDoctorIdAndFromDatetimeGreaterThanEqualAndToDatetimeLessThanEqual(doctorId, from, to);

        final var doctor = doctorRepository.findById(doctorId).orElseThrow();
        final var currentAbsences = absenceRepository.findAbsencesByDoctorForGivenPeriod(doctor, from, to);

        final var appointmentsSlots = appointments.stream().map(ap -> new CalendarEventDto(ap.getFromDatetime(),
                ap.getToDatetime())).collect(Collectors.toSet());
        final var existingTimeOffSlots = currentAbsences.stream().map(ab -> new CalendarEventDto(ab.getStartPeriod(),
                ab.getEndPeriod())).collect(Collectors.toSet());

        return !appointmentsSlots.contains(desiredTimeslot) && !existingTimeOffSlots.contains(desiredTimeslot);

    }

    /**
     * patient domain operation to check a doctor's availability.
     *
     * @param doctorId the doctor id
     * @return a doctor's availability
     */
    public List<CalendarEventDto> getDoctorAvailability(final Long doctorId,
                                                        final LocalDateTime from,
                                                        final LocalDateTime to) {

        final var calendarEvent = new CalendarEventDto(from, to);
        if (!calendarManager.checkIfPeriodIsWithinWeekRange(calendarEvent)) {
            throw new IllegalArgumentException("Please select the maximum period of a week");
        }
        final var doctor = doctorRepository.findById(doctorId).orElseThrow();
        final var appointments = appointmentRepository
                .findAppointmentByDoctorIdAndFromDatetimeGreaterThanEqualAndToDatetimeLessThanEqual(doctorId, from, to);

        final var absences = absenceRepository.findAbsencesByDoctorForGivenPeriod(doctor, from, to);
        final var result = fetchAvailableSlots(appointments, absences, from, to);

        return new ArrayList<>(result);
    }

    private Set<CalendarEventDto> fetchAvailableSlots(final List<Appointment> appointments, final List<Absence> absences,
                                                      final LocalDateTime from, final LocalDateTime to) {

        final var existingAppointments = appointments.stream().map(ap -> new CalendarEventDto(ap.getFromDatetime(),
                ap.getToDatetime())).collect(Collectors.toSet());

        // May exist absences greater than 1h opposed to appointments that last only 1h max
        final var absencesAs1hSlots = new HashSet<>();
        for (final Absence absence: absences) {
            final var currentAbsences = calendarManager.extractTimeslots(new CalendarEventDto(absence.getStartPeriod(), absence.getEndPeriod()));
            absencesAs1hSlots.addAll(currentAbsences);
        }

        final var calendarEvent = new CalendarEventDto(from, to);
        final var timeSlots = calendarManager.extractTimeslots(calendarEvent);

        timeSlots.removeAll(existingAppointments);
        timeSlots.removeAll(absencesAs1hSlots);

        return timeSlots;
    }

    public List<DoctorDto> getDoctors() {
        final var doctors = new ArrayList<DoctorDto>();
        doctorRepository.findAll().iterator().forEachRemaining(d -> doctors.add(doctorConverter.convert(d)));

        return doctors;
    }

    private void checkIfNewAbsenceRequestOverlapsWithExistingAbsentPeriods(final Long doctorId, final AbsenceRequestDto absenceRequesDto) {
        log.info("Checking for available periods between {} and {}", absenceRequesDto.getFrom(), absenceRequesDto.getTo());

        final var doctor = doctorRepository.findById(doctorId).orElseThrow();
        final var slots = absenceRepository.findAbsencesByDoctorForGivenPeriod(doctor, absenceRequesDto.getFrom(), absenceRequesDto.getTo());

        if (!slots.isEmpty()) {
            throw new IllegalArgumentException("Defined time off overlaps with existing period...Choose another available slot");
        }
    }

}
