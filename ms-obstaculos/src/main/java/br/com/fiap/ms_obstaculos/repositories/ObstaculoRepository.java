package br.com.fiap.ms_obstaculos.repositories;

import br.com.fiap.ms_obstaculos.entities.Obstaculo;
import br.com.fiap.ms_obstaculos.entities.StatusObstaculo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ObstaculoRepository extends JpaRepository<Obstaculo, Long> {

    List<Obstaculo> findByTrechoId(Long trechoId);

    List<Obstaculo> findByTrechoIdAndStatusIn(Long trechoId, List<StatusObstaculo> status);
}
