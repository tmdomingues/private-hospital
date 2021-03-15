package io.cloudmobility.tiago.domain.converter;


import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import io.cloudmobility.tiago.domain.dto.AbsenceRequestDto;
import io.cloudmobility.tiago.domain.model.Absence;

@Component
public class AbsenceConverter implements Converter<AbsenceRequestDto, Absence> {

    @Override
    public Absence convert(final AbsenceRequestDto absenceDto) {
        final Absence absence = new Absence();

        absence.setReason(absenceDto.getReason());
        absence.setStartPeriod(absenceDto.getFrom());
        absence.setEndPeriod(absenceDto.getTo());

        return absence;
    }
}
