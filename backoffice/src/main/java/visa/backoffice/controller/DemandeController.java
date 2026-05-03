package visa.backoffice.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import visa.backoffice.dto.DemandeResponseDTO;
import visa.backoffice.entity.Demande;
import visa.backoffice.entity.HistoriqueStatut;
import visa.backoffice.service.DemandeService;
import visa.backoffice.service.HistoriqueStatutService;

@RestController
@RequestMapping("/api")
// Ne pas mettre @CrossOrigin ici - utiliser CorsConfig à la place
public class DemandeController {
    
    @Autowired
    private DemandeService demandeService;
    
    @Autowired
    private HistoriqueStatutService historiqueService;
    
    // @Autowired
    // private QRCodeService qrCodeService;
    
    @GetMapping("/demandes")
    public ResponseEntity<?> getDemandes(
            @RequestParam(required = false) String numDemande,
            @RequestParam(required = false) String numPasseport) {
        
        if (numDemande != null && !numDemande.isEmpty()) {
            return getDemandeByNumero(numDemande);
        }
        
        if (numPasseport != null && !numPasseport.isEmpty()) {
            return getDemandesByPasseport(numPasseport);
        }
        
        return ResponseEntity.badRequest().body("Veuillez fournir numDemande ou numPasseport");
    }
    
    private ResponseEntity<?> getDemandeByNumero(String numDemande) {
        Map<String, Object> result = demandeService.rechercherParNumeroDemande(numDemande);
        
        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Aucune demande trouvée avec le numéro: " + numDemande);
        }
        
        Demande demandeCourante = (Demande) result.get("demandeCourante");
        @SuppressWarnings("unchecked")
        List<Demande> autresDemandes = (List<Demande>) result.get("autresDemandes");
        int nombreTotal = (int) result.get("nombreTotalDemandes");
        
        List<HistoriqueStatut> historique = historiqueService.getHistoriqueParDemandeId(demandeCourante.getId());
        // String qrCodeBase64 = qrCodeService.generateQRCodeBase64(numDemande);
        
        DemandeResponseDTO response = new DemandeResponseDTO(
            demandeCourante, autresDemandes, nombreTotal, historique
        );
        // response.setQrCodeBase64(qrCodeBase64);
        
        return ResponseEntity.ok(response);
    }
    
    private ResponseEntity<?> getDemandesByPasseport(String numPasseport) {
        List<Demande> demandes = demandeService.rechercherParNumeroPasseport(numPasseport);
        
        if (demandes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Aucune demande trouvée pour le passeport: " + numPasseport);
        }
        
        return ResponseEntity.ok(demandes);
    }
}