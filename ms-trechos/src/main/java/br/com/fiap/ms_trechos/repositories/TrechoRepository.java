package br.com.fiap.ms_trechos.repositories;

import br.com.fiap.ms_trechos.entities.StatusTrecho;
import br.com.fiap.ms_trechos.entities.Trecho;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrechoRepository extends JpaRepository<Trecho, Long> {

    List<Trecho> findByStatus(StatusTrecho status);
}
