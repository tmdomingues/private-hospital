package io.cloudmobility.tiago.domain.dto;


import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(name = "AbsenceRequest", description = "Request for setting time off for a given doctor")
public class AbsenceRequestDto {

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Schema(description = "Start of absence", type = "string", example = "2021-03-09T17:00")
    private LocalDateTime from;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Schema(description = "End of absence", type = "string", example = "2021-03-09T19:00")
    private LocalDateTime to;

    @Schema(description = "Motive of the absence", example = "Personal business")
    private String reason;
}
