package io.cloudmobility.tiago.domain.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import io.cloudmobility.tiago.domain.dto.DoctorDto;
import io.cloudmobility.tiago.domain.model.Doctor;

@Component
public class DoctorConverter implements Converter<Doctor, DoctorDto> {

    @Override
    public DoctorDto convert(final Doctor doctor) {
        return new DoctorDto(doctor.getId(), doctor.getName(), doctor.getSpecialization());
    }
}
