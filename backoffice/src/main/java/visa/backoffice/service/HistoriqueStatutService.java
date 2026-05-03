package visa.backoffice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import visa.backoffice.entity.Demande;
import visa.backoffice.entity.HistoriqueStatut;
import visa.backoffice.repository.HistoriqueStatutRepository;

@Service
public class HistoriqueStatutService {
    
    @Autowired
    private HistoriqueStatutRepository historiqueStatutRepository;
    
    @Autowired
    private DemandeService demandeService;
    
    /**
     * Récupère l'historique complet des statuts pour une demande
     * 
     * @param demandeId ID de la demande
     * @return Liste des historiques ordonnés du plus ancien au plus récent
     */
    @Transactional(readOnly = true)
    public List<HistoriqueStatut> getHistoriqueParDemandeId(Long demandeId) {
        return historiqueStatutRepository.findByDemandeIdOrderByDateChangementAsc(demandeId);
    }
    
    /**
     * Récupère l'historique complet des statuts pour une demande (par numéro de demande)
     * 
     * @param refDemande Numéro de la demande
     * @return Liste des historiques ordonnés du plus ancien au plus récent
     */
    @Transactional(readOnly = true)
    public List<HistoriqueStatut> getHistoriqueParNumeroDemande(String refDemande) {
        var result = demandeService.rechercherParNumeroDemande(refDemande);
        if (result.isEmpty()) {
            return List.of();
        }
        Demande demande = (Demande) result.get("demandeCourante");
        return historiqueStatutRepository.findByDemandeIdOrderByDateChangementAsc(demande.getId());
    }
    
    /**
     * Récupère le dernier statut d'une demande
     * 
     * @param demandeId ID de la demande
     * @return Dernier historique (ou empty si aucun)
     */
    @Transactional(readOnly = true)
    public Optional<HistoriqueStatut> getDernierStatut(Long demandeId) {
        List<HistoriqueStatut> derniers = historiqueStatutRepository.findTopByDemandeIdOrderByDateChangementDesc(demandeId);
        return derniers.isEmpty() ? Optional.empty() : Optional.of(derniers.get(0));
    }
    
    /**
     * Récupère le dernier statut d'une demande (par numéro de demande)
     */
    @Transactional(readOnly = true)
    public Optional<HistoriqueStatut> getDernierStatutParNumeroDemande(String refDemande) {
        var result = demandeService.rechercherParNumeroDemande(refDemande);
        if (result.isEmpty()) {
            return Optional.empty();
        }
        Demande demande = (Demande) result.get("demandeCourante");
        return getDernierStatut(demande.getId());
    }
    
    /**
     * Vérifie si une demande a déjà eu un certain statut
     */
    @Transactional(readOnly = true)
    public boolean aEuStatut(Long demandeId, Long statutId) {
        return historiqueStatutRepository.findByDemandeIdOrderByDateChangementAsc(demandeId)
                .stream()
                .anyMatch(h -> h.getStatut().getId() == statutId);
    }
}