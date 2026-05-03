package visa.backoffice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name="piece_justificative_ref")
public class PieceJustificativeRef {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
    
    private String libelle;
    
    @ManyToOne
    @JoinColumn(name="id_type_titre")
    private TypeTitre typeTitre;
    
    /* Getters */
    public long getId(){ return this.id; }
    public String getLibelle(){ return this.libelle; }
    public TypeTitre getTypeTitre(){ return this.typeTitre; }
    
    /* Setters */
    public void setId(long id){ this.id = id; }
    public void setLibelle(String libelle){ this.libelle = libelle; }
    public void setTypeTitre(TypeTitre typeTitre){ this.typeTitre = typeTitre; }
}