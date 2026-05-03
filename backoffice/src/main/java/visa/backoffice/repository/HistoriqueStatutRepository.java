package visa.backoffice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import visa.backoffice.entity.HistoriqueStatut;

@Repository
public interface HistoriqueStatutRepository extends JpaRepository<HistoriqueStatut, Long> {
    
    // Recherche de l'historique complet d'une demande, ordre chronologique
    List<HistoriqueStatut> findByDemandeIdOrderByDateChangementAsc(Long demandeId);
    
    // Dernier statut d'une demande
    List<HistoriqueStatut> findTopByDemandeIdOrderByDateChangementDesc(Long demandeId);
}