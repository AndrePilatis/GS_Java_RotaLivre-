package br.com.fiap.ms_obstaculos.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "tb_obstaculo")
public class Obstaculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long trechoId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoObstaculo tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Gravidade gravidade;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrigemRegistro origem;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusObstaculo status;

    @Column(nullable = false)
    private LocalDateTime dataRegistro;

    private LocalDateTime dataResolucao;
}
