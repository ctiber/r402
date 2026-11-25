-- Donnees rechargees avant chaque test de bout en bout (BaseE2ETest)
TRUNCATE journal_operation, operation, compte RESTART IDENTITY CASCADE;

INSERT INTO compte (numero, type, devise, solde, decouvert_autorise, titulaire) VALUES
    ('FR76-001', 'COURANT', 'EUR', 5000.00, 0.00, 'alice'),
    ('FR76-002', 'EPARGNE', 'EUR',  100.00, 0.00, 'alice'),
    ('FR76-003', 'COURANT', 'EUR',   20.00, 0.00, 'bruno');
