package visa.backoffice.entity;

import java.sql.Date;
import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="demandeur")
public class Demandeur {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
    private String nom;
    private String prenom;
    private String nomJeuneFille;
    private Date dateNaissance;
    private Long situationFamilleId;
    private Long nationaliteId;
    private String adresseMadagascar;
    private String numeroTelephone;
    private String email;
    private String profession;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    /* Getters */
    public String getNom(){ return this.nom; }
    public String getPrenom(){ return this.prenom; }
    public String getNomJeuneFille(){ return this.nomJeuneFille; }
    public String getNumeroTelephone(){ return this.numeroTelephone; }
    public String getEmail(){ return this.email; }
    public String getProfession(){ return this.profession; }
    public Date getDateNaissance(){ return this.dateNaissance; }
    public Long getSituationFamilleId(){ return this.situationFamilleId; }
    public Long getNationaliteId(){ return this.nationaliteId; }
    public String getAdresseMadagascar(){ return this.adresseMadagascar; }
    public Timestamp getCreatedAt(){ return this.createdAt; }
    public Timestamp getUpdatedAt(){ return this.updatedAt; }
    public long getId(){ return this.id; }
    
    /* Setters */
    public void setNom(String nom){ this.nom = nom; }
    public void setPrenom(String prenom){ this.prenom = prenom; }
    public void setNomJeuneFille(String nomJeuneFille){ this.nomJeuneFille = nomJeuneFille; }
    public void setNumeroTelephone(String numeroTelephone){ this.numeroTelephone = numeroTelephone; }
    public void setEmail(String email){ this.email = email; }
    public void setProfession(String profession){ this.profession = profession; }
    public void setDateNaissance(Date dateNaissance){ this.dateNaissance = dateNaissance; }
    public void setSituationFamilleId(Long situationFamilleId){ this.situationFamilleId = situationFamilleId; }
    public void setNationaliteId(Long nationaliteId){ this.nationaliteId = nationaliteId; }
    public void setAdresseMadagascar(String adresseMadagascar){ this.adresseMadagascar = adresseMadagascar; }
    public void setCreatedAt(Timestamp createdAt){ this.createdAt = createdAt; }
    public void setUpdatedAt(Timestamp updatedAt){ this.updatedAt = updatedAt; }
    public void setId(long id){ this.id = id; }
}