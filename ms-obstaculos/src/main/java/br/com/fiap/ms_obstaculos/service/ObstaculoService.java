package br.com.fiap.ms_obstaculos.service;

import br.com.fiap.ms_obstaculos.client.TrechoClient;
import br.com.fiap.ms_obstaculos.dto.ObstaculoRequestDto;
import br.com.fiap.ms_obstaculos.dto.ObstaculoResponseDto;
import br.com.fiap.ms_obstaculos.entities.Gravidade;
import br.com.fiap.ms_obstaculos.entities.Obstaculo;
import br.com.fiap.ms_obstaculos.entities.StatusObstaculo;
import br.com.fiap.ms_obstaculos.exceptions.DatabaseException;
import br.com.fiap.ms_obstaculos.exceptions.OperacaoNaoPermitidaException;
import br.com.fiap.ms_obstaculos.exceptions.ResourceNotFoundException;
import br.com.fiap.ms_obstaculos.repositories.ObstaculoRepository;
import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ObstaculoService {

    // obstáculos que contam na situação do trecho
    private static final List<StatusObstaculo> STATUS_ATIVOS =
            List.of(StatusObstaculo.CONFIRMADO, StatusObstaculo.CONFIRMADO_SEM_INTEGRACAO);

    // obstáculos cuja alteração ainda não chegou ao ms-trechos
    private static final List<StatusObstaculo> STATUS_SEM_INTEGRACAO =
            List.of(StatusObstaculo.CONFIRMADO_SEM_INTEGRACAO, StatusObstaculo.RESOLVIDO_SEM_INTEGRACAO);

    @Autowired
    private ObstaculoRepository obstaculoRepository;

    @Autowired
    private TrechoClient trechoClient;

    @Transactional(readOnly = true)
    public List<ObstaculoResponseDto> findAllObstaculos(Long trechoId) {

        List<Obstaculo> obstaculos = (trechoId == null)
                ? obstaculoRepository.findAll()
                : obstaculoRepository.findByTrechoId(trechoId);

        return obstaculos.stream().map(ObstaculoResponseDto::new).toList();
    }

    @Transactional(readOnly = true)
    public ObstaculoResponseDto findObstaculoById(Long id) {

        Obstaculo obstaculo = obstaculoRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado. ID: " + id)
        );

        return new ObstaculoResponseDto(obstaculo);
    }

    @Transactional
    public ObstaculoResponseDto saveObstaculo(ObstaculoRequestDto requestDto) {

        Obstaculo obstaculo = new Obstaculo();
        copyDtoToObstaculo(requestDto, obstaculo);
        obstaculo.setStatus(StatusObstaculo.REPORTADO);
        obstaculo.setDataRegistro(LocalDateTime.now());
        obstaculo = obstaculoRepository.save(obstaculo);
        return new ObstaculoResponseDto(obstaculo);
    }

    @Transactional
    public ObstaculoResponseDto updateObstaculo(Long id, ObstaculoRequestDto requestDto) {

        try {
            Obstaculo obstaculo = obstaculoRepository.getReferenceById(id);

            if (obstaculo.getStatus() != StatusObstaculo.REPORTADO) {
                throw new OperacaoNaoPermitidaException(String.format(
                        "Obstáculo id %d está %s. Somente obstáculos com status REPORTADO podem ser alterados.",
                        id, obstaculo.getStatus()));
            }

            copyDtoToObstaculo(requestDto, obstaculo);
            obstaculo = obstaculoRepository.save(obstaculo);
            return new ObstaculoResponseDto(obstaculo);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Recurso não encontrado. ID: " + id);
        }
    }

    @Transactional
    public void deleteObstaculoById(Long id) {

        Obstaculo obstaculo = obstaculoRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado. ID: " + id)
        );

        if (STATUS_ATIVOS.contains(obstaculo.getStatus()) || STATUS_SEM_INTEGRACAO.contains(obstaculo.getStatus())) {
            throw new DatabaseException(String.format(
                    "Não foi possível excluir o obstáculo. Ele está %s e a situação do trecho id %d ainda depende dele.",
                    obstaculo.getStatus(), obstaculo.getTrechoId()));
        }

        obstaculoRepository.delete(obstaculo);
    }

    @Transactional
    public ObstaculoResponseDto confirmarObstaculo(Long id) {

        Obstaculo obstaculo = findObstaculoParaConfirmar(id);
        obstaculo.setStatus(StatusObstaculo.CONFIRMADO);
        obstaculo = obstaculoRepository.save(obstaculo);

        atualizarSituacaoDoTrecho(obstaculo.getTrechoId());

        return new ObstaculoResponseDto(obstaculo);
    }

    @Transactional
    public ObstaculoResponseDto confirmarObstaculoSemIntegracao(Long id) {

        Obstaculo obstaculo = findObstaculoParaConfirmar(id);
        obstaculo.setStatus(StatusObstaculo.CONFIRMADO_SEM_INTEGRACAO);
        obstaculo = obstaculoRepository.save(obstaculo);
        return new ObstaculoResponseDto(obstaculo);
    }

    @Transactional
    public ObstaculoResponseDto resolverObstaculo(Long id) {

        Obstaculo obstaculo = findObstaculoParaResolver(id);
        registrarResolucao(obstaculo, StatusObstaculo.RESOLVIDO);
        obstaculo = obstaculoRepository.save(obstaculo);

        atualizarSituacaoDoTrecho(obstaculo.getTrechoId());

        return new ObstaculoResponseDto(obstaculo);
    }

    @Transactional
    public ObstaculoResponseDto resolverObstaculoSemIntegracao(Long id) {

        Obstaculo obstaculo = findObstaculoParaResolver(id);
        registrarResolucao(obstaculo, StatusObstaculo.RESOLVIDO_SEM_INTEGRACAO);
        obstaculo = obstaculoRepository.save(obstaculo);
        return new ObstaculoResponseDto(obstaculo);
    }

    @Transactional
    public ObstaculoResponseDto descartarObstaculo(Long id) {

        Obstaculo obstaculo = obstaculoRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado. ID: " + id)
        );

        if (obstaculo.getStatus() != StatusObstaculo.REPORTADO) {
            throw new OperacaoNaoPermitidaException(String.format(
                    "Obstáculo id %d está %s. Somente obstáculos com status REPORTADO podem ser descartados.",
                    id, obstaculo.getStatus()));
        }

        obstaculo.setStatus(StatusObstaculo.DESCARTADO);
        obstaculo = obstaculoRepository.save(obstaculo);
        return new ObstaculoResponseDto(obstaculo);
    }

    private void atualizarSituacaoDoTrecho(Long trechoId) {

        List<Obstaculo> ativos = obstaculoRepository.findByTrechoIdAndStatusIn(trechoId, STATUS_ATIVOS);

        try {
            if (ativos.stream().anyMatch(o -> o.getGravidade() == Gravidade.ALTA)) {
                trechoClient.bloquearTrecho(trechoId);
            } else if (!ativos.isEmpty()) {
                trechoClient.restringirTrecho(trechoId);
            } else {
                trechoClient.liberarTrecho(trechoId);
            }
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException("Trecho não encontrado. ID: " + trechoId);
        }

        // a situação enviada já considera os obstáculos do trecho que estavam sem integração
        List<Obstaculo> pendentes = obstaculoRepository.findByTrechoIdAndStatusIn(trechoId, STATUS_SEM_INTEGRACAO);
        for (Obstaculo pendente : pendentes) {
            pendente.setStatus(pendente.getStatus() == StatusObstaculo.CONFIRMADO_SEM_INTEGRACAO
                    ? StatusObstaculo.CONFIRMADO
                    : StatusObstaculo.RESOLVIDO);
        }
        obstaculoRepository.saveAll(pendentes);
    }

    private Obstaculo findObstaculoParaConfirmar(Long id) {

        Obstaculo obstaculo = obstaculoRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado. ID: " + id)
        );

        if (obstaculo.getStatus() != StatusObstaculo.REPORTADO
                && obstaculo.getStatus() != StatusObstaculo.CONFIRMADO_SEM_INTEGRACAO) {
            throw new OperacaoNaoPermitidaException(String.format(
                    "Obstáculo id %d está %s. Somente obstáculos com status REPORTADO "
                            + "ou CONFIRMADO_SEM_INTEGRACAO podem ser confirmados.",
                    id, obstaculo.getStatus()));
        }

        return obstaculo;
    }

    private Obstaculo findObstaculoParaResolver(Long id) {

        Obstaculo obstaculo = obstaculoRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado. ID: " + id)
        );

        if (obstaculo.getStatus() != StatusObstaculo.CONFIRMADO
                && !STATUS_SEM_INTEGRACAO.contains(obstaculo.getStatus())) {
            throw new OperacaoNaoPermitidaException(String.format(
                    "Obstáculo id %d está %s. Somente obstáculos com status CONFIRMADO, "
                            + "CONFIRMADO_SEM_INTEGRACAO ou RESOLVIDO_SEM_INTEGRACAO podem ser resolvidos.",
                    id, obstaculo.getStatus()));
        }

        return obstaculo;
    }

    private void registrarResolucao(Obstaculo obstaculo, StatusObstaculo status) {

        obstaculo.setStatus(status);
        // ao repetir a resolução de um RESOLVIDO_SEM_INTEGRACAO, mantém a data em que ele foi resolvido
        if (obstaculo.getDataResolucao() == null) {
            obstaculo.setDataResolucao(LocalDateTime.now());
        }
    }

    private void copyDtoToObstaculo(ObstaculoRequestDto requestDto, Obstaculo obstaculo) {

        obstaculo.setTrechoId(requestDto.getTrechoId());
        obstaculo.setTipo(requestDto.getTipo());
        obstaculo.setGravidade(requestDto.getGravidade());
        obstaculo.setDescricao(requestDto.getDescricao());
        obstaculo.setLatitude(requestDto.getLatitude());
        obstaculo.setLongitude(requestDto.getLongitude());
        obstaculo.setOrigem(requestDto.getOrigem());
    }
}
