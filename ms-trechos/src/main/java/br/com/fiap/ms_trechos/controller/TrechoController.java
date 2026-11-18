package br.com.fiap.ms_trechos.controller;

import br.com.fiap.ms_trechos.dto.TrechoRequestDto;
import br.com.fiap.ms_trechos.dto.TrechoResponseDto;
import br.com.fiap.ms_trechos.entities.StatusTrecho;
import br.com.fiap.ms_trechos.service.TrechoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Tag(name = "Trechos", description = "Trechos de calçada mapeados pelo RotaLivre e a situação de acessibilidade de cada um")
@RestController
@RequestMapping("/api/v1/trechos")
public class TrechoController {

    @Autowired
    private TrechoService trechoService;

    @Operation(summary = "Informa a porta da instância que respondeu (teste do Load Balancer)")
    @GetMapping("/port")
    public ResponseEntity<String> port(@Value("${local.server.port}") String porta) {

        return ResponseEntity.ok("Instância respondeu na porta: " + porta);
    }

    @Operation(summary = "Lista os trechos, com filtro opcional pela situação (LIVRE, RESTRITO ou BLOQUEADO)")
    @GetMapping
    public ResponseEntity<List<TrechoResponseDto>> getAllTrechos(@RequestParam(required = false)
                                                                 StatusTrecho status) {

        List<TrechoResponseDto> list = trechoService.findAllTrechos(status);

        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Busca um trecho pelo id")
    @GetMapping("/{id}")
    public ResponseEntity<TrechoResponseDto> getTrechoById(@PathVariable Long id) {

        TrechoResponseDto trechoDto = trechoService.findTrechoById(id);

        return ResponseEntity.ok(trechoDto);
    }

    @Operation(summary = "Cadastra um trecho (situação inicial LIVRE)")
    @ApiResponse(responseCode = "201", description = "Trecho cadastrado")
    @PostMapping
    public ResponseEntity<TrechoResponseDto> createTrecho(@RequestBody @Valid
                                                          TrechoRequestDto requestDto) {

        TrechoResponseDto trechoDto = trechoService.saveTrecho(requestDto);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(trechoDto.getId())
                .toUri();

        return ResponseEntity.created(uri).body(trechoDto);
    }

    @Operation(summary = "Atualiza os dados de um trecho (a situação só muda pelos obstáculos)")
    @PutMapping("/{id}")
    public ResponseEntity<TrechoResponseDto> updateTrecho(@PathVariable Long id,
                                                          @RequestBody @Valid
                                                          TrechoRequestDto requestDto) {

        TrechoResponseDto trechoDto = trechoService.updateTrecho(id, requestDto);

        return ResponseEntity.ok(trechoDto);
    }

    @Operation(summary = "Bloqueia o trecho: um obstáculo impede a passagem (chamado pelo ms-obstaculos via OpenFeign)")
    @PutMapping("/{id}/bloquear")
    public ResponseEntity<TrechoResponseDto> bloquearTrecho(@PathVariable Long id) {

        TrechoResponseDto trechoDto = trechoService.bloquearTrecho(id);

        return ResponseEntity.ok(trechoDto);
    }

    @Operation(summary = "Restringe o trecho: há obstáculos que dificultam a passagem (chamado pelo ms-obstaculos via OpenFeign)")
    @PutMapping("/{id}/restringir")
    public ResponseEntity<TrechoResponseDto> restringirTrecho(@PathVariable Long id) {

        TrechoResponseDto trechoDto = trechoService.restringirTrecho(id);

        return ResponseEntity.ok(trechoDto);
    }

    @Operation(summary = "Libera o trecho: não há obstáculos ativos (chamado pelo ms-obstaculos via OpenFeign)")
    @PutMapping("/{id}/liberar")
    public ResponseEntity<TrechoResponseDto> liberarTrecho(@PathVariable Long id) {

        TrechoResponseDto trechoDto = trechoService.liberarTrecho(id);

        return ResponseEntity.ok(trechoDto);
    }

    @Operation(summary = "Exclui um trecho que esteja LIVRE")
    @ApiResponse(responseCode = "204", description = "Trecho excluído")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrecho(@PathVariable Long id) {

        trechoService.deleteTrechoById(id);

        return ResponseEntity.noContent().build();
    }
}
