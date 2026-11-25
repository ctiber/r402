CREATE TABLE operation (
    id        BIGSERIAL PRIMARY KEY,
    compte_id BIGINT         NOT NULL REFERENCES compte (id),
    type      VARCHAR(20)    NOT NULL,
    montant   NUMERIC(12, 2) NOT NULL,
    date      TIMESTAMP      NOT NULL
);

CREATE INDEX idx_operation_compte_date ON operation (compte_id, date);
