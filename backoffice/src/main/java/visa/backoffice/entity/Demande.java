package visa.backoffice.entity;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="demande")
public class Demande {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
    
    /* Foreign key */
    @ManyToOne
    private TypeDemande typeDemande;

    @ManyToOne
    private TypeTitre typeTitre;

    @ManyToOne    
    private StatutDemande statutDemande;

    private Date visaDateEntree;
    private String visaLieuEntree;
    private Date visaDateExp;
    private Date createdAT;
    private Date updatedAT;


    /* Getters */
    public TypeDemande getTypeDemande(){ return this.typeDemande; }
    public TypeTitre getTypeTitre(){ return this.typeTitre; }
    public StatutDemande getStatutDemande(){ return this.statutDemande; }
    public Date getCreatedAT(){ return this.createdAT; }
    public Date getUpdatedAT(){ return this.updatedAT; }
    public Date getVisaDateExp(){ return this.visaDateExp; }
    public String getVisaLieuEntree(){ return this.visaLieuEntree ; }
    public Date getVisaDateEntree(){ return this.visaDateEntree ; }
    public long getId(){ return this.id; }
    
    /* Setters */
    public void setTypeDemande(TypeDemande TypeDemande){  this.typeDemande = TypeDemande; }
    public void setTypeTitre(TypeTitre TypeTitre){  this.typeTitre = TypeTitre; }
    public void setStatutDemande(StatutDemande StatutDemande){  this.statutDemande = StatutDemande; }
    public void setCreatedAT(Date CreatedAT){ this.createdAT = CreatedAT; }
    public void setUpdatedAT(Date UpdatedAT){ this.updatedAT = UpdatedAT; }
    public void setVisaDateExp(Date VisaDateExp){ this.visaDateExp = VisaDateExp; }
    public void setVisaLieuEntree(String VisaLieuEntree){ this.visaLieuEntree = VisaLieuEntree; }
    public void setVisaDateEntree(Date VisaDateEntree){ this.visaDateEntree = VisaDateEntree; }
    public void setId(long Id){ this.id = Id; }

}
