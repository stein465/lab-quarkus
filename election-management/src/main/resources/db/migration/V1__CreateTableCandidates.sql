CREATE TABLE candidates (
    id VARCHAR(40) NOT NULL,
    photo VARCHAR(255) NOT NULL,  -- Corrigido o tipo de dado e removido o 'DEFAULT'
    given_name VARCHAR(50) NOT NULL,
    family_name VARCHAR(50) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(50) NOT NULL,
    job_title VARCHAR(50) NOT NULL,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);
