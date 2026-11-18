package br.com.fiap.ms_trechos.dto;

import br.com.fiap.ms_trechos.entities.StatusTrecho;
import br.com.fiap.ms_trechos.entities.Trecho;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TrechoResponseDto {

    private Long id;
    private String logradouro;
    private String referencia;
    private String bairro;
    private String cidade;
    private Double latitude;
    private Double longitude;
    private Integer extensaoMetros;
    private StatusTrecho status;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataAtualizacao;

    public TrechoResponseDto(Trecho trecho) {
        id = trecho.getId();
        logradouro = trecho.getLogradouro();
        referencia = trecho.getReferencia();
        bairro = trecho.getBairro();
        cidade = trecho.getCidade();
        latitude = trecho.getLatitude();
        longitude = trecho.getLongitude();
        extensaoMetros = trecho.getExtensaoMetros();
        status = trecho.getStatus();
        dataAtualizacao = trecho.getDataAtualizacao();
    }
}
