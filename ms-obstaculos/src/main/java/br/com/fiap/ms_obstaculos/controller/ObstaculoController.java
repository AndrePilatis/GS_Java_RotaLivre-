package br.com.fiap.ms_obstaculos.controller;

import br.com.fiap.ms_obstaculos.dto.ObstaculoRequestDto;
import br.com.fiap.ms_obstaculos.dto.ObstaculoResponseDto;
import br.com.fiap.ms_obstaculos.service.ObstaculoService;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

@Tag(name = "Obstáculos", description = "Obstáculos nas calçadas informados pela comunidade e pelos sensores IoT das cadeiras de rodas")
@RestController
@RequestMapping("/api/v1/obstaculos")
public class ObstaculoController {

    @Autowired
    private ObstaculoService obstaculoService;

    @Operation(summary = "Lista os obstáculos, com filtro opcional pelo trecho")
    @GetMapping
    public ResponseEntity<List<ObstaculoResponseDto>> getAllObstaculos(@RequestParam(required = false)
                                                                       Long trechoId) {

        List<ObstaculoResponseDto> list = obstaculoService.findAllObstaculos(trechoId);

        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Busca um obstáculo pelo id")
    @GetMapping("/{id}")
    public ResponseEntity<ObstaculoResponseDto> getObstaculoById(@PathVariable Long id) {

        ObstaculoResponseDto obstaculoDto = obstaculoService.findObstaculoById(id);

        return ResponseEntity.ok(obstaculoDto);
    }

    @Operation(summary = "Registra um obstáculo com a localização (status inicial REPORTADO)")
    @ApiResponse(responseCode = "201", description = "Obstáculo registrado")
    @PostMapping
    public ResponseEntity<ObstaculoResponseDto> createObstaculo(@RequestBody @Valid
                                                                ObstaculoRequestDto requestDto) {

        ObstaculoResponseDto obstaculoDto = obstaculoService.saveObstaculo(requestDto);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(obstaculoDto.getId())
                .toUri();

        return ResponseEntity.created(uri).body(obstaculoDto);
    }

    @Operation(summary = "Atualiza os dados de um obstáculo que ainda está REPORTADO")
    @PutMapping("/{id}")
    public ResponseEntity<ObstaculoResponseDto> updateObstaculo(@PathVariable Long id,
                                                                @RequestBody @Valid
                                                                ObstaculoRequestDto requestDto) {

        ObstaculoResponseDto obstaculoDto = obstaculoService.updateObstaculo(id, requestDto);

        return ResponseEntity.ok(obstaculoDto);
    }

    @Operation(summary = "Confirma o obstáculo e atualiza a situação do trecho no ms-trechos (OpenFeign)")
    @ApiResponse(responseCode = "200", description = "Obstáculo CONFIRMADO e situação do trecho atualizada")
    @ApiResponse(responseCode = "202", description = "ms-trechos indisponível (fallback): obstáculo CONFIRMADO_SEM_INTEGRACAO")
    @CircuitBreaker(name = "atualizarTrecho", fallbackMethod = "confirmarObstaculoSemIntegracao")
    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<ObstaculoResponseDto> confirmarObstaculo(@PathVariable Long id) {

        ObstaculoResponseDto obstaculoDto = obstaculoService.confirmarObstaculo(id);

        return ResponseEntity.ok(obstaculoDto);
    }

    @Operation(summary = "Resolve o obstáculo e atualiza a situação do trecho no ms-trechos (OpenFeign)")
    @ApiResponse(responseCode = "200", description = "Obstáculo RESOLVIDO e situação do trecho atualizada")
    @ApiResponse(responseCode = "202", description = "ms-trechos indisponível (fallback): obstáculo RESOLVIDO_SEM_INTEGRACAO")
    @CircuitBreaker(name = "atualizarTrecho", fallbackMethod = "resolverObstaculoSemIntegracao")
    @PatchMapping("/{id}/resolver")
    public ResponseEntity<ObstaculoResponseDto> resolverObstaculo(@PathVariable Long id) {

        ObstaculoResponseDto obstaculoDto = obstaculoService.resolverObstaculo(id);

        return ResponseEntity.ok(obstaculoDto);
    }

    @Operation(summary = "Descarta um obstáculo REPORTADO que não foi encontrado na vistoria")
    @PatchMapping("/{id}/descartar")
    public ResponseEntity<ObstaculoResponseDto> descartarObstaculo(@PathVariable Long id) {

        ObstaculoResponseDto obstaculoDto = obstaculoService.descartarObstaculo(id);

        return ResponseEntity.ok(obstaculoDto);
    }

    @Operation(summary = "Exclui um obstáculo que não conta mais na situação do trecho")
    @ApiResponse(responseCode = "204", description = "Obstáculo excluído")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteObstaculo(@PathVariable Long id) {

        obstaculoService.deleteObstaculoById(id);

        return ResponseEntity.noContent().build();
    }

    // Fallbacks do Circuit Breaker: falha ao chamar o ms-trechos (FeignException)
    // ou circuito aberto, quando a chamada nem é feita (CallNotPermittedException)

    public ResponseEntity<ObstaculoResponseDto> confirmarObstaculoSemIntegracao(Long id, FeignException e) {

        ObstaculoResponseDto obstaculoDto = obstaculoService.confirmarObstaculoSemIntegracao(id);

        return ResponseEntity.accepted().body(obstaculoDto);
    }

    public ResponseEntity<ObstaculoResponseDto> confirmarObstaculoSemIntegracao(Long id, CallNotPermittedException e) {

        ObstaculoResponseDto obstaculoDto = obstaculoService.confirmarObstaculoSemIntegracao(id);

        return ResponseEntity.accepted().body(obstaculoDto);
    }

    public ResponseEntity<ObstaculoResponseDto> resolverObstaculoSemIntegracao(Long id, FeignException e) {

        ObstaculoResponseDto obstaculoDto = obstaculoService.resolverObstaculoSemIntegracao(id);

        return ResponseEntity.accepted().body(obstaculoDto);
    }

    public ResponseEntity<ObstaculoResponseDto> resolverObstaculoSemIntegracao(Long id, CallNotPermittedException e) {

        ObstaculoResponseDto obstaculoDto = obstaculoService.resolverObstaculoSemIntegracao(id);

        return ResponseEntity.accepted().body(obstaculoDto);
    }
}
