package br.com.fiap.ms_trechos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TrechoRequestDto {

    @NotBlank(message = "Campo logradouro é obrigatório")
    @Size(min = 3, max = 150, message = "O campo logradouro deve ter entre 3 e 150 caracteres")
    @Schema(example = "Rua Vergueiro")
    private String logradouro;

    @NotBlank(message = "Campo referência é obrigatório")
    @Size(min = 3, max = 150, message = "O campo referência deve ter entre 3 e 150 caracteres")
    @Schema(example = "Entre a Rua Pedroso e a Rua Tomás Carvalhal")
    private String referencia;

    @NotBlank(message = "Campo bairro é obrigatório")
    @Size(min = 2, max = 100, message = "O campo bairro deve ter entre 2 e 100 caracteres")
    @Schema(example = "Paraíso")
    private String bairro;

    @NotBlank(message = "Campo cidade é obrigatório")
    @Size(min = 2, max = 100, message = "O campo cidade deve ter entre 2 e 100 caracteres")
    @Schema(example = "São Paulo")
    private String cidade;

    @NotNull(message = "Campo latitude é obrigatório")
    @DecimalMin(value = "-90.0", message = "O campo latitude deve estar entre -90 e 90")
    @DecimalMax(value = "90.0", message = "O campo latitude deve estar entre -90 e 90")
    @Schema(example = "-23.574520")
    private Double latitude;

    @NotNull(message = "Campo longitude é obrigatório")
    @DecimalMin(value = "-180.0", message = "O campo longitude deve estar entre -180 e 180")
    @DecimalMax(value = "180.0", message = "O campo longitude deve estar entre -180 e 180")
    @Schema(example = "-46.640380")
    private Double longitude;

    @NotNull(message = "Campo extensão é obrigatório")
    @Positive(message = "O campo extensão deve ser um número positivo e maior que zero")
    @Schema(example = "220")
    private Integer extensaoMetros;
}
