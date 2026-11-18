package br.com.fiap.ms_obstaculos.dto;

import br.com.fiap.ms_obstaculos.entities.Gravidade;
import br.com.fiap.ms_obstaculos.entities.OrigemRegistro;
import br.com.fiap.ms_obstaculos.entities.TipoObstaculo;
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
public class ObstaculoRequestDto {

    @NotNull(message = "Campo trecho é obrigatório")
    @Positive(message = "O campo trecho deve ser um número positivo e maior que zero")
    @Schema(example = "6")
    private Long trechoId;

    @NotNull(message = "Campo tipo é obrigatório")
    @Schema(example = "BURACO")
    private TipoObstaculo tipo;

    @NotNull(message = "Campo gravidade é obrigatório")
    @Schema(example = "ALTA")
    private Gravidade gravidade;

    @NotBlank(message = "Campo descrição é obrigatório")
    @Size(min = 10, max = 255, message = "O campo descrição deve ter entre 10 e 255 caracteres")
    @Schema(example = "Buraco de cerca de 60 cm em frente ao número 120, ocupando toda a largura da calçada.")
    private String descricao;

    @NotNull(message = "Campo latitude é obrigatório")
    @DecimalMin(value = "-90.0", message = "O campo latitude deve estar entre -90 e 90")
    @DecimalMax(value = "90.0", message = "O campo latitude deve estar entre -90 e 90")
    @Schema(example = "-23.560480")
    private Double latitude;

    @NotNull(message = "Campo longitude é obrigatório")
    @DecimalMin(value = "-180.0", message = "O campo longitude deve estar entre -180 e 180")
    @DecimalMax(value = "180.0", message = "O campo longitude deve estar entre -180 e 180")
    @Schema(example = "-46.643870")
    private Double longitude;

    @NotNull(message = "Campo origem é obrigatório")
    @Schema(example = "COMUNIDADE")
    private OrigemRegistro origem;
}
