package visa.backoffice.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import visa.backoffice.entity.Demande;
import visa.backoffice.repository.DemandeRepository;

@Service
public class DemandeService {
    
    @Autowired
    private DemandeRepository demandeRepository;
    
    /**
     * Recherche par numéro de demande
     * Retourne la demande trouvée + toutes les autres demandes du même passeport
     * 
     * @param refDemande Numéro de demande (ex: 20250422-143012-DUP)
     * @return Map contenant :
     *         - "demandeCourante": la demande recherchée
     *         - "autresDemandes": les autres demandes du même passeport
     *         (si demande non trouvée, retourne une map vide)
     */
    @Transactional(readOnly = true)
    public Map<String, Object> rechercherParNumeroDemande(String refDemande) {
        Map<String, Object> result = new HashMap<>();
        
        Optional<Demande> demandeOpt = demandeRepository.findByRefDemande(refDemande);
        
        if (demandeOpt.isEmpty()) {
            return Collections.emptyMap();
        }
        
        Demande demandeCourante = demandeOpt.get();

        // Vérifier que le passeport existe et a un numéro
        if (demandeCourante.getPasseport() == null || demandeCourante.getPasseport().getNumPasseport() == null) {
            return Collections.emptyMap();
        }

        String numPasseport = demandeCourante.getPasseport().getNumPasseport();
        
        // Récupérer toutes les demandes du même passeport (ordre chronologique récent)
        List<Demande> toutesDemandes = demandeRepository.findByPasseportNumPasseportOrderByCreatedAtDesc(numPasseport);
        
        // Séparer la demande courante des autres en utilisant == sur l'id primitif
        List<Demande> autresDemandes = toutesDemandes.stream()
                .filter(d -> d.getId() != demandeCourante.getId())  // Correction : utiliser != au lieu de .equals()
                .toList();
        
        result.put("demandeCourante", demandeCourante);
        result.put("autresDemandes", autresDemandes);
        result.put("nombreTotalDemandes", toutesDemandes.size());
        
        return result;
    }
    
    /**
     * Recherche par numéro de passeport
     * Retourne toutes les demandes par ordre chronologique du plus récent au plus ancien
     * 
     * @param numPasseport Numéro du passeport
     * @return Liste des demandes associées à ce passeport (ordre du plus récent)
     */
    @Transactional(readOnly = true)
    public List<Demande> rechercherParNumeroPasseport(String numPasseport) {
        return demandeRepository.findByPasseportNumPasseportOrderByCreatedAtDesc(numPasseport);
    }
    
    /**
     * Vérifie si une demande existe par son numéro
     */
    @Transactional(readOnly = true)
    public boolean existeParNumeroDemande(String refDemande) {
        return demandeRepository.findByRefDemande(refDemande).isPresent();
    }
    
    /**
     * Compte le nombre de demandes pour un passeport donné
     */
    @Transactional(readOnly = true)
    public long compterDemandesParPasseport(String numPasseport) {
        return demandeRepository.findByPasseportNumPasseportOrderByCreatedAtDesc(numPasseport).size();
    }
}