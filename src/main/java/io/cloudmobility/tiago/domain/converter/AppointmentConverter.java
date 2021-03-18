package io.cloudmobility.tiago.domain.converter;


import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import io.cloudmobility.tiago.domain.dto.AppointmentResponseDto;
import io.cloudmobility.tiago.domain.model.Appointment;

@Component
public class AppointmentConverter implements Converter<Appointment, AppointmentResponseDto> {

    @Override
    public AppointmentResponseDto convert(final Appointment appointment) {
        return new AppointmentResponseDto(appointment.getStartPeriod(),
                appointment.getEndPeriod(),
                appointment.getPatient().getName());
    }
}
