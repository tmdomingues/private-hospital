package io.cloudmobility.tiago.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.cloudmobility.tiago.domain.dto.AbsenceRequestDto;
import io.cloudmobility.tiago.domain.dto.AppointmentResponseDto;
import io.cloudmobility.tiago.domain.dto.CalendarEventDto;
import io.cloudmobility.tiago.domain.dto.DoctorDto;
import io.cloudmobility.tiago.domain.model.Absence;
import io.cloudmobility.tiago.domain.model.Appointment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Tag(name = "PrivateHospitalController")
@RestController
@RequestMapping(path = "/v1/hospital", produces = "application/json")
public class PrivateHospitalController {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final PrivateHospitalService privateHospitalService;

    /* **************************** Doctor's operations ****************************** */

    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping("/doctors/{id}/appointments")
    @Operation(summary = "Get a list of a doctor's appointments")
    public List<AppointmentResponseDto> getDoctorAppointments(
            @Parameter(name = "id", example = "1") @PathVariable(value = "id") final Long id,
            @Parameter(name = "from", example = "2021-03-09 17:00") @RequestParam(value = "from") final String from,
            @Parameter(name = "to", example = "2021-03-09 20:00") @RequestParam(value = "to") final String to,
            @Parameter(name = "pageable") final Pageable pageable) {

        final var fromDateTime = LocalDateTime.parse(from, DATE_FORMATTER);
        final var toDateTime = LocalDateTime.parse(to, DATE_FORMATTER);

        return privateHospitalService.getDoctorAppointments(id, fromDateTime, toDateTime);
    }

    @PostMapping("/doctors/{id}/time-off")
    @Operation(summary = "Set a doctor's time off")
    public ResponseEntity<Absence> setDoctorTimeOff(
            @Parameter(name = "id", example = "1") @PathVariable(value = "id") final Long id,
            @RequestBody @Valid final AbsenceRequestDto absenceReqDto) {

        final Absence absence = privateHospitalService.setDoctorAbsence(id, absenceReqDto);

        return new ResponseEntity<>(absence, HttpStatus.CREATED);
    }

    /* **************************** Patient's operations ****************************** */

    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping("/doctors/{id}/availability")
    @Operation(summary = "Get a doctor's availability for a given week")
    public Page<CalendarEventDto> getDoctorAvailability(
            @Parameter(name = "id", example = "1") @PathVariable(value = "id") final Long id,
            @Parameter(name = "from", example = "2021-03-22 18:00") @RequestParam(value = "from") final String from,
            @Parameter(name = "to", example = "2021-03-24 10:00") @RequestParam(value = "to") final String to,
            @Parameter(name = "pageable") final Pageable pageable) {

        final var initDate = LocalDateTime.parse(from, DATE_FORMATTER);
        final var endDate = LocalDateTime.parse(to, DATE_FORMATTER);

        final var availability = privateHospitalService.getDoctorAvailability(id, initDate, endDate);

        final int start = (int) pageable.getOffset();
        final int end = Math.min(start + pageable.getPageSize(), availability.size());

        return new PageImpl<>(availability.subList(start, end), pageable, availability.size());

    }

    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping("/patients/{patientId}/doctors/{doctorId}/appointments/book")
    @Operation(summary = "Schedule an appointment with the chosen doctor")
    public Appointment bookAnAppointmentWithDoctor(
            @Parameter(name = "patientId", example = "4") @PathVariable(value = "patientId") final Long patientId,
            @Parameter(name = "doctorId", example = "3") @PathVariable(value = "doctorId") final Long doctorId,
            @Parameter(name = "from", example = "2021-03-25 14:00") @RequestParam(value = "from") final String from,
            @Parameter(name = "to", example = "2021-03-25 15:00") @RequestParam(value = "to") final String to,
            @Parameter(name = "description", example = "Follow up") @RequestParam(value = "description", required = false) final String description) {

        final var initDate = LocalDateTime.parse(from, DATE_FORMATTER);
        final var endDate = LocalDateTime.parse(to, DATE_FORMATTER);

        return privateHospitalService.scheduleAppointment(patientId, doctorId, initDate, endDate, description);
    }

    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping("/doctors/")
    @Operation(summary = "List the hospital doctors")
    public List<DoctorDto> getDoctors() {
        return privateHospitalService.getDoctors();
    }
}
