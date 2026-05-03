-- / Table en plus : hostorique des status de dossier 
-- Table d'historique des statuts des demandes
CREATE TABLE IF NOT EXISTS historique_statut (
    id SERIAL PRIMARY KEY,
    statut_id BIGINT NOT NULL REFERENCES statut_demande(id),
    date_changement TIMESTAMP NOT NULL DEFAULT NOW(),
    demande_id BIGINT NOT NULL REFERENCES demande(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_historique_statut_demande ON historique_statut(demande_id);
CREATE INDEX IF NOT EXISTS idx_historique_statut_date ON historique_statut(date_changement);