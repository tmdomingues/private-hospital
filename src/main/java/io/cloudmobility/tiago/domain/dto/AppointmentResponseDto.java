package io.cloudmobility.tiago.domain.dto;


import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(name = "AppointmentResponse", description = "Information about a doctor's appointments")
public class AppointmentResponseDto {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @Schema(description = "Start of absence", type = "string", example = "2021-03-09 17:00")
    private LocalDateTime from;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @Schema(description = "End of absence", type = "string", example = "2021-03-09 19:00")
    private LocalDateTime to;

    @Schema(description = "Name of the patient", example = "John Couves")
    private String patientName;

}
