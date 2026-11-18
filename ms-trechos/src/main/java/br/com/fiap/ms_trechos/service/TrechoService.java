package br.com.fiap.ms_trechos.service;

import br.com.fiap.ms_trechos.dto.TrechoRequestDto;
import br.com.fiap.ms_trechos.dto.TrechoResponseDto;
import br.com.fiap.ms_trechos.entities.StatusTrecho;
import br.com.fiap.ms_trechos.entities.Trecho;
import br.com.fiap.ms_trechos.exceptions.DatabaseException;
import br.com.fiap.ms_trechos.exceptions.ResourceNotFoundException;
import br.com.fiap.ms_trechos.repositories.TrechoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TrechoService {

    @Autowired
    private TrechoRepository trechoRepository;

    @Transactional(readOnly = true)
    public List<TrechoResponseDto> findAllTrechos(StatusTrecho status) {

        List<Trecho> trechos = (status == null)
                ? trechoRepository.findAll()
                : trechoRepository.findByStatus(status);

        return trechos.stream().map(TrechoResponseDto::new).toList();
    }

    @Transactional(readOnly = true)
    public TrechoResponseDto findTrechoById(Long id) {

        Trecho trecho = trechoRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado. ID: " + id)
        );

        return new TrechoResponseDto(trecho);
    }

    @Transactional
    public TrechoResponseDto saveTrecho(TrechoRequestDto requestDto) {

        Trecho trecho = new Trecho();
        copyDtoToTrecho(requestDto, trecho);
        trecho.setStatus(StatusTrecho.LIVRE);
        trecho.setDataAtualizacao(LocalDateTime.now());
        trecho = trechoRepository.save(trecho);
        return new TrechoResponseDto(trecho);
    }

    @Transactional
    public TrechoResponseDto updateTrecho(Long id, TrechoRequestDto requestDto) {

        try {
            Trecho trecho = trechoRepository.getReferenceById(id);
            copyDtoToTrecho(requestDto, trecho);
            trecho = trechoRepository.save(trecho);
            return new TrechoResponseDto(trecho);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Recurso não encontrado. ID: " + id);
        }
    }

    @Transactional
    public void deleteTrechoById(Long id) {

        Trecho trecho = trechoRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado. ID: " + id)
        );

        if (trecho.getStatus() != StatusTrecho.LIVRE) {
            throw new DatabaseException("Não foi possível excluir o trecho. Ele está " + trecho.getStatus()
                    + " porque existem obstáculos ativos registrados nele.");
        }

        trechoRepository.delete(trecho);
    }

    @Transactional
    public TrechoResponseDto bloquearTrecho(Long id) {

        return alterarStatus(id, StatusTrecho.BLOQUEADO);
    }

    @Transactional
    public TrechoResponseDto restringirTrecho(Long id) {

        return alterarStatus(id, StatusTrecho.RESTRITO);
    }

    @Transactional
    public TrechoResponseDto liberarTrecho(Long id) {

        return alterarStatus(id, StatusTrecho.LIVRE);
    }

    private TrechoResponseDto alterarStatus(Long id, StatusTrecho novoStatus) {

        Trecho trecho = trechoRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado. ID: " + id)
        );

        // o ms-obstaculos pode reenviar a mesma situação; só registra quando ela muda
        if (trecho.getStatus() != novoStatus) {
            trecho.setStatus(novoStatus);
            trecho.setDataAtualizacao(LocalDateTime.now());
            trecho = trechoRepository.save(trecho);
        }

        return new TrechoResponseDto(trecho);
    }

    private void copyDtoToTrecho(TrechoRequestDto requestDto, Trecho trecho) {

        trecho.setLogradouro(requestDto.getLogradouro());
        trecho.setReferencia(requestDto.getReferencia());
        trecho.setBairro(requestDto.getBairro());
        trecho.setCidade(requestDto.getCidade());
        trecho.setLatitude(requestDto.getLatitude());
        trecho.setLongitude(requestDto.getLongitude());
        trecho.setExtensaoMetros(requestDto.getExtensaoMetros());
    }
}
