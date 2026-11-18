INSERT INTO tb_trecho (logradouro, referencia, bairro, cidade, latitude, longitude, extensao_metros, status, data_atualizacao) VALUES
('Avenida Paulista', 'Entre a Rua Pamplona e a Alameda Joaquim Eugênio de Lima', 'Bela Vista', 'São Paulo', -23.565600, -46.651100, 300, 'LIVRE', TIMESTAMPADD(DAY, -1, UTC_TIMESTAMP())),
('Rua Augusta', 'Entre a Avenida Paulista e a Rua Luís Coelho', 'Consolação', 'São Paulo', -23.557500, -46.657800, 250, 'BLOQUEADO', TIMESTAMPADD(DAY, -3, UTC_TIMESTAMP())),
('Rua da Consolação', 'Entre a Rua Maria Antônia e a Rua Dona Antônia de Queirós', 'Consolação', 'São Paulo', -23.547600, -46.651500, 180, 'RESTRITO', TIMESTAMPADD(DAY, -2, UTC_TIMESTAMP())),
('Rua Galvão Bueno', 'Entre a Rua dos Estudantes e a Rua São Joaquim', 'Liberdade', 'São Paulo', -23.558600, -46.635400, 400, 'LIVRE', TIMESTAMPADD(DAY, -15, UTC_TIMESTAMP())),
('Rua Teodoro Sampaio', 'Entre a Rua Henrique Schaumann e a Rua Oscar Freire', 'Pinheiros', 'São Paulo', -23.559900, -46.673600, 350, 'LIVRE', TIMESTAMPADD(DAY, -15, UTC_TIMESTAMP())),
('Rua Treze de Maio', 'Entre a Rua Santo Antônio e a Rua São Domingos', 'Bela Vista', 'São Paulo', -23.560200, -46.644100, 200, 'LIVRE', TIMESTAMPADD(DAY, -15, UTC_TIMESTAMP()));
