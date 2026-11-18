CREATE TABLE IF NOT EXISTS tb_trecho (
    id BIGINT NOT NULL AUTO_INCREMENT,
    logradouro VARCHAR(150) NOT NULL,
    referencia VARCHAR(150) NOT NULL,
    bairro VARCHAR(100) NOT NULL,
    cidade VARCHAR(100) NOT NULL,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL,
    extensao_metros INT NOT NULL,
    status VARCHAR(20) NOT NULL,
    data_atualizacao DATETIME(6) NOT NULL,
    CONSTRAINT pk_tb_trecho PRIMARY KEY (id)
);
