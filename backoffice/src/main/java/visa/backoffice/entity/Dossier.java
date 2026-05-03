package visa.backoffice.entity;

import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.OneToMany;
import java.util.List;

@Entity
@Table(name="dossier")
public class Dossier {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
    
    private String previousDemandeRef;
    private String newDemandeRef;
    private String mention;
    private Boolean visaApprouveConfirme;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    @OneToMany(mappedBy = "dossier")
    private List<DossierDemande> dossierDemandes;
    
    /* Getters */
    public long getId(){ return this.id; }
    public String getPreviousDemandeRef(){ return this.previousDemandeRef; }
    public String getNewDemandeRef(){ return this.newDemandeRef; }
    public String getMention(){ return this.mention; }
    public Boolean getVisaApprouveConfirme(){ return this.visaApprouveConfirme; }
    public Timestamp getCreatedAt(){ return this.createdAt; }
    public Timestamp getUpdatedAt(){ return this.updatedAt; }
    public List<DossierDemande> getDossierDemandes(){ return this.dossierDemandes; }
    
    /* Setters */
    public void setId(long id){ this.id = id; }
    public void setPreviousDemandeRef(String previousDemandeRef){ this.previousDemandeRef = previousDemandeRef; }
    public void setNewDemandeRef(String newDemandeRef){ this.newDemandeRef = newDemandeRef; }
    public void setMention(String mention){ this.mention = mention; }
    public void setVisaApprouveConfirme(Boolean visaApprouveConfirme){ this.visaApprouveConfirme = visaApprouveConfirme; }
    public void setCreatedAt(Timestamp createdAt){ this.createdAt = createdAt; }
    public void setUpdatedAt(Timestamp updatedAt){ this.updatedAt = updatedAt; }
    public void setDossierDemandes(List<DossierDemande> dossierDemandes){ this.dossierDemandes = dossierDemandes; }
}