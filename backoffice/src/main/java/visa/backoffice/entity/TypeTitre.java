package visa.backoffice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="type_titre")
public class TypeTitre {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
    private String libelle;
    
    /* Getters */
    public String getLibelle(){ return this.libelle; }
    public long getId(){ return this.id; }
    
    /*Setters */
    public void setLibelle(String Libelle){ this.libelle = Libelle; }
    public void setId(long Id){ this.id = Id; }

}
