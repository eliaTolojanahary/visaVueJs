DROP TABLE IF EXISTS historique_statut; 

CREATE TABLE IF NOT EXISTS historique_statut (
    id BIGSERIAL PRIMARY KEY,
    date_changement TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    demande_id BIGINT NOT NULL,
    statut_id BIGINT NOT NULL,
    ancien_statut_id BIGINT,
    CONSTRAINT historique_statut_demande_id_fkey FOREIGN KEY (demande_id) REFERENCES demande(id),
    CONSTRAINT historique_statut_statut_id_fkey FOREIGN KEY (statut_id) REFERENCES statut_demande(id),
    CONSTRAINT historique_statut_ancien_statut_id_fkey FOREIGN KEY (ancien_statut_id) REFERENCES statut_demande(id)
);

-- Créer les index
CREATE INDEX idx_historique_statut_date ON historique_statut(date_changement);
CREATE INDEX idx_historique_statut_demande ON historique_statut(demande_id);