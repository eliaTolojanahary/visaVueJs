package visa.backoffice.entity;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="historique_statut")
public class HistoriqueStatut {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
    
    @ManyToOne
    @JoinColumn(name="statut_id")
    private StatutDemande statut;
    
    private Timestamp dateChangement;
    
    @ManyToOne
    @JoinColumn(name="demande_id")
    @JsonManagedReference
    private Demande demande;
    
    /* Getters */
    public long getId(){ return this.id; }
    public StatutDemande getStatut(){ return this.statut; }
    public Timestamp getDateChangement(){ return this.dateChangement; }
    public Demande getDemande(){ return this.demande; }
    
    /* Setters */
    public void setId(long id){ this.id = id; }
    public void setStatut(StatutDemande statut){ this.statut = statut; }
    public void setDateChangement(Timestamp dateChangement){ this.dateChangement = dateChangement; }
    public void setDemande(Demande demande){ this.demande = demande; }
}