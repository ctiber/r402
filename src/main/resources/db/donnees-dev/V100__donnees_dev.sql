-- Comptes de demonstration (profil dev uniquement)
INSERT INTO compte (numero, type, devise, solde, decouvert_autorise, titulaire) VALUES
    ('FR76-001', 'COURANT', 'EUR', 500.00, 200.00, 'alice'),
    ('FR76-002', 'EPARGNE', 'EUR', 100.00,   0.00, 'alice'),
    ('FR76-003', 'COURANT', 'EUR',  20.00,   0.00, 'bruno'),
    ('US-042',   'COURANT', 'USD', 250.00,   0.00, 'bruno');
