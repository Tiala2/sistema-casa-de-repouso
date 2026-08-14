-- Criacao do banco de dados
CREATE DATABASE IF NOT EXISTS idosoapp
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE idosoapp;

-- Tabela Idosa
CREATE TABLE IF NOT EXISTS idosa (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(20) NOT NULL,
    data_nascimento DATE,
    nome_mae VARCHAR(100),
    cartao_sus VARCHAR(30),
    data_entrada DATE,
    INDEX idx_idosa_nome (nome),
    INDEX idx_idosa_cpf (cpf)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela ProfissionalSaude
CREATE TABLE IF NOT EXISTS profissional_saude (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    especialidade VARCHAR(100),
    registro_profissional VARCHAR(50),
    INDEX idx_profissional_nome (nome)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela ProntuarioMedico
CREATE TABLE IF NOT EXISTS prontuario_medico (
    id INT PRIMARY KEY AUTO_INCREMENT,
    data_hora_idosa DATETIME,
    idosa_id INT NOT NULL,
    INDEX idx_prontuario_idosa (idosa_id),
    CONSTRAINT fk_prontuario_idosa
        FOREIGN KEY (idosa_id)
        REFERENCES idosa(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela Consulta
CREATE TABLE IF NOT EXISTS consulta (
    id INT PRIMARY KEY AUTO_INCREMENT,
    data_hora DATETIME NOT NULL,
    profissional_id INT NOT NULL,
    tipo VARCHAR(50),
    motivo VARCHAR(200),
    diagnostico VARCHAR(200),
    prontuario_id INT,
    INDEX idx_consulta_profissional (profissional_id),
    INDEX idx_consulta_prontuario (prontuario_id),
    INDEX idx_consulta_data_hora (data_hora),
    CONSTRAINT fk_consulta_profissional
        FOREIGN KEY (profissional_id)
        REFERENCES profissional_saude(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_consulta_prontuario
        FOREIGN KEY (prontuario_id)
        REFERENCES prontuario_medico(id)
        ON UPDATE CASCADE
        ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela Prescricao
CREATE TABLE IF NOT EXISTS prescricao (
    id INT PRIMARY KEY AUTO_INCREMENT,
    medicamento VARCHAR(100) NOT NULL,
    posologia VARCHAR(100),
    duracao VARCHAR(50),
    observacoes VARCHAR(200),
    prontuario_id INT NOT NULL,
    INDEX idx_prescricao_prontuario (prontuario_id),
    CONSTRAINT fk_prescricao_prontuario
        FOREIGN KEY (prontuario_id)
        REFERENCES prontuario_medico(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela Vacina
CREATE TABLE IF NOT EXISTS vacina (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    data_ocorrencia DATE NOT NULL,
    prontuario_id INT NOT NULL,
    INDEX idx_vacina_prontuario (prontuario_id),
    INDEX idx_vacina_data (data_ocorrencia),
    CONSTRAINT fk_vacina_prontuario
        FOREIGN KEY (prontuario_id)
        REFERENCES prontuario_medico(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela EventoSentinela
CREATE TABLE IF NOT EXISTS evento_sentinela (
    id INT PRIMARY KEY AUTO_INCREMENT,
    tipo VARCHAR(100) NOT NULL,
    data_ocorrencia DATE NOT NULL,
    prontuario_id INT NOT NULL,
    INDEX idx_evento_prontuario (prontuario_id),
    INDEX idx_evento_tipo_periodo (tipo, data_ocorrencia),
    CONSTRAINT fk_evento_prontuario
        FOREIGN KEY (prontuario_id)
        REFERENCES prontuario_medico(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela Relatorio
CREATE TABLE IF NOT EXISTS relatorio (
    id INT PRIMARY KEY AUTO_INCREMENT,
    descricao VARCHAR(200) NOT NULL,
    tipo VARCHAR(100) NOT NULL,
    prontuario_id INT NOT NULL,
    INDEX idx_relatorio_prontuario (prontuario_id),
    CONSTRAINT fk_relatorio_prontuario
        FOREIGN KEY (prontuario_id)
        REFERENCES prontuario_medico(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
