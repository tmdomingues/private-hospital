package io.cloudmobility.tiago.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(name = "Doctor", description = "Information about a doctor")
public class DoctorDto {

    @Schema(description = "Doctor's id", example = "1")
    private Long id;

    @Schema(description = "Doctor's name", example = "Kurt Sloane")
    private String name;

    @Schema(description = "Doctor's specialization", example = "Cardiologist")
    private String specialization;
}
