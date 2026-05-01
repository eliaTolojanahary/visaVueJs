package visa.backoffice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import visa.backoffice.entity.Demande;

@Repository
public interface DemandeRepository extends JpaRepository<Demande, Long> {
    
    // Recherche par numéro de demande (refDemande)
    Optional<Demande> findByRefDemande(String refDemande);
    
    // Recherche toutes les demandes d'un passeport par son numéro, ordre chronologique du plus récent au plus ancien
    List<Demande> findByPasseportNumPasseportOrderByCreatedAtDesc(String numPasseport);
    
    // Variante avec pagination si besoin
    // Page<Demande> findByPasseportNumPasseportOrderByCreatedAtDesc(String numPasseport, Pageable pageable);
}