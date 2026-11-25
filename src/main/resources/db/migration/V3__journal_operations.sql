-- Journal d'audit : chaque operation inseree est tracee par un trigger (comme en R3.07).
CREATE TABLE journal_operation (
    id           BIGSERIAL PRIMARY KEY,
    operation_id BIGINT    NOT NULL,
    date_journal TIMESTAMP NOT NULL DEFAULT now()
);

CREATE FUNCTION journaliser_operation() RETURNS trigger AS $$
BEGIN
    INSERT INTO journal_operation (operation_id) VALUES (NEW.id);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_journal_operation
    AFTER INSERT ON operation
    FOR EACH ROW EXECUTE FUNCTION journaliser_operation();
