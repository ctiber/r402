CREATE TABLE compte (
    id                 BIGSERIAL PRIMARY KEY,
    numero             VARCHAR(34)    NOT NULL UNIQUE,
    type               VARCHAR(20)    NOT NULL,
    devise             VARCHAR(3)     NOT NULL,
    solde              NUMERIC(12, 2) NOT NULL,
    decouvert_autorise NUMERIC(12, 2) NOT NULL DEFAULT 0,
    cloture            BOOLEAN        NOT NULL DEFAULT FALSE,
    titulaire          VARCHAR(50)
);
