CREATE TABLE IF NOT EXISTS photo (
    id BIGSERIAL PRIMARY KEY,
    dossier_id BIGINT NOT NULL REFERENCES dossier(id) ON DELETE CASCADE,
    chemin_fichier VARCHAR(512) NOT NULL,
    nom_fichier VARCHAR(255) NOT NULL,
    taille_bytes BIGINT,
    mime_type VARCHAR(100),
    date_capture TIMESTAMP NOT NULL DEFAULT NOW(),
    uploaded_at TIMESTAMP NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_photo_dossier ON photo(dossier_id);

CREATE TABLE IF NOT EXISTS signature (
    id BIGSERIAL PRIMARY KEY,
    dossier_id BIGINT NOT NULL REFERENCES dossier(id) ON DELETE CASCADE,
    chemin_fichier VARCHAR(512) NOT NULL,
    nom_fichier VARCHAR(255) NOT NULL,
    taille_bytes BIGINT,
    mime_type VARCHAR(100),
    date_capture TIMESTAMP NOT NULL DEFAULT NOW(),
    uploaded_at TIMESTAMP NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_signature_dossier ON signature(dossier_id);

ALTER TABLE dossier 
    ADD COLUMN IF NOT EXISTS scan_termine BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS date_scan_complete TIMESTAMP;