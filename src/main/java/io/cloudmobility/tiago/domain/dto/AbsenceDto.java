package io.cloudmobility.tiago.domain.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "AbsenceRequest", description = "Request for setting time off for a given doctor")
public class AbsenceDto extends CalendarEventDto {

    public AbsenceDto(final LocalDateTime from, final LocalDateTime to, final String reason) {
        super(from, to);
        this.reason = reason;
    }

    @Schema(description = "Motive of the absence", example = "Personal business")
    private String reason;
}

