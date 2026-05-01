package visa.backoffice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="type_document")
public class TypeDocument {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
    private String libelle;
    
    /* Getters */
    public String getLibelle(){ return this.libelle; }
    public long getId(){ return this.id; }
    
    /* Setters */
    public void setLibelle(String libelle){ this.libelle = libelle; }
    public void setId(long id){ this.id = id; }
}