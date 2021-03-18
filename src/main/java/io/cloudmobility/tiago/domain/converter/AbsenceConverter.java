package io.cloudmobility.tiago.domain.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import io.cloudmobility.tiago.domain.dto.AbsenceDto;
import io.cloudmobility.tiago.domain.model.Absence;

@Component
public class AbsenceConverter implements Converter<AbsenceDto, Absence> {

    @Override
    public Absence convert(final AbsenceDto absenceDto) {
        final Absence absence = new Absence();
        absence.setReason(absenceDto.getReason());
        absence.setStartPeriod(absenceDto.getFrom());
        absence.setEndPeriod(absenceDto.getTo());

        return absence;
    }

    public AbsenceDto convertToDto(final Absence absence) {
        return new AbsenceDto(absence.getStartPeriod(), absence.getEndPeriod(), absence.getReason());
    }
}
