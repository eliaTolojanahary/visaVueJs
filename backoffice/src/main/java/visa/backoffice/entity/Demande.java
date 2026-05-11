package visa.backoffice.entity;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="demande")
public class Demande {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
    
    private String refDemande;  // Identifiant metier: YYYYMMDD-HHMMSS-<code_type_demande>
    
    /* Foreign keys */
    @ManyToOne
    @JoinColumn(name="passeport_id")
    private Passeport passeport;
    
    @ManyToOne
    @JoinColumn(name="type_demande_id")
    private TypeDemande typeDemande;

    @ManyToOne
    @JoinColumn(name="type_titre_id")
    private TypeTitre typeTitre;

    @ManyToOne
    @JoinColumn(name="statut_id")
    private StatutDemande statutDemande;
    
    @ManyToOne
    @JoinColumn(name="type_document_id")
    private TypeDocument typeDocument;

    private Date visaDateEntree;
    private String visaLieuEntree;
    private Date visaDateExpiration;
    private Boolean verrouille;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    @OneToMany(mappedBy = "demande")
    @JsonBackReference
    private List<HistoriqueStatut> historiqueStatuts;

    /* Getters */
    public TypeDemande getTypeDemande(){ return this.typeDemande; }
    public TypeTitre getTypeTitre(){ return this.typeTitre; }
    public StatutDemande getStatutDemande(){ return this.statutDemande; }
    public TypeDocument getTypeDocument(){ return this.typeDocument; }
    public Timestamp getCreatedAt(){ return this.createdAt; }
    public Timestamp getUpdatedAt(){ return this.updatedAt; }
    public Date getVisaDateExpiration(){ return this.visaDateExpiration; }
    public String getVisaLieuEntree(){ return this.visaLieuEntree; }
    public Date getVisaDateEntree(){ return this.visaDateEntree; }
    public long getId(){ return this.id; }
    public String getRefDemande(){ return this.refDemande; }
    public Passeport getPasseport(){ return this.passeport; }
    public Boolean getVerrouille(){ return this.verrouille; }
    public List<HistoriqueStatut> getHistoriqueStatuts(){ return this.historiqueStatuts; }
    
    /* Setters */
    public void setTypeDemande(TypeDemande typeDemande){ this.typeDemande = typeDemande; }
    public void setTypeTitre(TypeTitre typeTitre){ this.typeTitre = typeTitre; }
    public void setStatutDemande(StatutDemande statutDemande){ this.statutDemande = statutDemande; }
    public void setTypeDocument(TypeDocument typeDocument){ this.typeDocument = typeDocument; }
    public void setCreatedAt(Timestamp createdAt){ this.createdAt = createdAt; }
    public void setUpdatedAt(Timestamp updatedAt){ this.updatedAt = updatedAt; }
    public void setVisaDateExpiration(Date visaDateExpiration){ this.visaDateExpiration = visaDateExpiration; }
    public void setVisaLieuEntree(String visaLieuEntree){ this.visaLieuEntree = visaLieuEntree; }
    public void setVisaDateEntree(Date visaDateEntree){ this.visaDateEntree = visaDateEntree; }
    public void setId(long id){ this.id = id; }
    public void setRefDemande(String refDemande){ this.refDemande = refDemande; }
    public void setPasseport(Passeport passeport){ this.passeport = passeport; }
    public void setVerrouille(Boolean verrouille){ this.verrouille = verrouille; }
    public void setHistoriqueStatuts(List<HistoriqueStatut> historiqueStatuts){ this.historiqueStatuts = historiqueStatuts; }
}