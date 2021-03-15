package io.cloudmobility.tiago.domain.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(name = "CalendarEvent", description = "Refers to a calendar event")
public class CalendarEventDto {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @Schema(description = "Beginning of available slot", type = "string", example = "2021-03-22 18:00")
    private LocalDateTime from;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @Schema(description = "End of available slot", type = "string", example = "2021-03-24 10:00")
    private LocalDateTime to;
}
