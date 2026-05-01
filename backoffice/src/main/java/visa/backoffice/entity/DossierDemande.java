package visa.backoffice.entity;

import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name="dossier_demande")
public class DossierDemande {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
    
    @ManyToOne
    @JoinColumn(name="dossier_id")
    private Dossier dossier;
    
    @ManyToOne
    @JoinColumn(name="demande_id")
    private Demande demande;
    
    private Timestamp createdAt;
    
    /* Getters */
    public long getId(){ return this.id; }
    public Dossier getDossier(){ return this.dossier; }
    public Demande getDemande(){ return this.demande; }
    public Timestamp getCreatedAt(){ return this.createdAt; }
    
    /* Setters */
    public void setId(long id){ this.id = id; }
    public void setDossier(Dossier dossier){ this.dossier = dossier; }
    public void setDemande(Demande demande){ this.demande = demande; }
    public void setCreatedAt(Timestamp createdAt){ this.createdAt = createdAt; }
}