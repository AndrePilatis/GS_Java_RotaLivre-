CREATE TABLE IF NOT EXISTS tb_obstaculo (
    id BIGINT NOT NULL AUTO_INCREMENT,
    trecho_id BIGINT NOT NULL,
    tipo VARCHAR(30) NOT NULL,
    gravidade VARCHAR(10) NOT NULL,
    descricao VARCHAR(255) NOT NULL,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL,
    origem VARCHAR(20) NOT NULL,
    status VARCHAR(30) NOT NULL,
    data_registro DATETIME(6) NOT NULL,
    data_resolucao DATETIME(6),
    CONSTRAINT pk_tb_obstaculo PRIMARY KEY (id),
    INDEX idx_tb_obstaculo_trecho (trecho_id)
);
