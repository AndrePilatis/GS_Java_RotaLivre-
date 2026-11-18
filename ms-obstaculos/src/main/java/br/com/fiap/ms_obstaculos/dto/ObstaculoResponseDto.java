package br.com.fiap.ms_obstaculos.dto;

import br.com.fiap.ms_obstaculos.entities.Gravidade;
import br.com.fiap.ms_obstaculos.entities.Obstaculo;
import br.com.fiap.ms_obstaculos.entities.OrigemRegistro;
import br.com.fiap.ms_obstaculos.entities.StatusObstaculo;
import br.com.fiap.ms_obstaculos.entities.TipoObstaculo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ObstaculoResponseDto {

    private Long id;
    private Long trechoId;
    private TipoObstaculo tipo;
    private Gravidade gravidade;
    private String descricao;
    private Double latitude;
    private Double longitude;
    private OrigemRegistro origem;
    private StatusObstaculo status;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataRegistro;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataResolucao;

    public ObstaculoResponseDto(Obstaculo obstaculo) {
        id = obstaculo.getId();
        trechoId = obstaculo.getTrechoId();
        tipo = obstaculo.getTipo();
        gravidade = obstaculo.getGravidade();
        descricao = obstaculo.getDescricao();
        latitude = obstaculo.getLatitude();
        longitude = obstaculo.getLongitude();
        origem = obstaculo.getOrigem();
        status = obstaculo.getStatus();
        dataRegistro = obstaculo.getDataRegistro();
        dataResolucao = obstaculo.getDataResolucao();
    }
}
