package visa.backoffice.entity;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="passeport")
public class Passeport {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Demandeur demandeur;

    private String numPasseport;
    private Date dateDelivrance;
    private Date dateExp;
    private String paysDelivrance;
    private Date createdAT;
    private Date updatedAT;
    
    /* Setters */
    public Demandeur getDemandeur(){ return this.demandeur ; }
    public String getNumPasseport(){ return this.numPasseport ; }
    public String getPaysDelivrance(){ return this.paysDelivrance ; }
    public Date getCreatedAT(){ return this.createdAT; }
    public Date getUpdatedAT(){ return this.updatedAT; }
    public Date getDateDelivrance(){ return this.dateDelivrance; }
    public Date getDateExp(){ return this.dateExp; }
    public long getId(){ return this.id; }

    /* Getters */
    public void setDemandeur(Demandeur Demandeur){  this.demandeur = Demandeur; }
    public void setNumPasseport(String NumPasseport){  this.numPasseport = NumPasseport; }
    public void setPaysDelivrance(String PaysDelivrance){  this.paysDelivrance = PaysDelivrance; }
    public void setCreatedAT(Date CreatedAT){ this.createdAT = CreatedAT; }
    public void setUpdatedAT(Date UpdatedAT){ this.updatedAT = UpdatedAT; }
    public void setDateDelivrance(Date DateDelivrance){ this.dateDelivrance = DateDelivrance; }
    public void setDateExp(Date DateExp){ this.dateExp = DateExp; }
    public void setId(long Id){ this.id = Id; }

}
