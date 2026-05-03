package visa.backoffice.dto;

import visa.backoffice.entity.Demande;
import visa.backoffice.entity.HistoriqueStatut;

import java.util.List;

public class DemandeResponseDTO {
    private Demande demandeCourante;
    private List<Demande> autresDemandes;
    private int nombreTotalDemandes;
    private List<HistoriqueStatut> historique;
    private String qrCodeBase64;
    
    // Constructeurs
    public DemandeResponseDTO() {}
    
    public DemandeResponseDTO(Demande demandeCourante, List<Demande> autresDemandes, 
                              int nombreTotalDemandes, List<HistoriqueStatut> historique) {
        this.demandeCourante = demandeCourante;
        this.autresDemandes = autresDemandes;
        this.nombreTotalDemandes = nombreTotalDemandes;
        this.historique = historique;
    }
    
    // Getters et Setters
    public Demande getDemandeCourante() {
        return demandeCourante;
    }
    
    public void setDemandeCourante(Demande demandeCourante) {
        this.demandeCourante = demandeCourante;
    }
    
    public List<Demande> getAutresDemandes() {
        return autresDemandes;
    }
    
    public void setAutresDemandes(List<Demande> autresDemandes) {
        this.autresDemandes = autresDemandes;
    }
    
    public int getNombreTotalDemandes() {
        return nombreTotalDemandes;
    }
    
    public void setNombreTotalDemandes(int nombreTotalDemandes) {
        this.nombreTotalDemandes = nombreTotalDemandes;
    }
    
    public List<HistoriqueStatut> getHistorique() {
        return historique;
    }
    
    public void setHistorique(List<HistoriqueStatut> historique) {
        this.historique = historique;
    }
    
    public String getQrCodeBase64() {
        return qrCodeBase64;
    }
    
    public void setQrCodeBase64(String qrCodeBase64) {
        this.qrCodeBase64 = qrCodeBase64;
    }
}