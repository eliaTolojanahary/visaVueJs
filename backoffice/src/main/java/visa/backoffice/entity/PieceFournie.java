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
@Table(name="piece_fournie")
public class PieceFournie {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
    
    @ManyToOne
    @JoinColumn(name="demande_id")
    private Demande demande;
    
    @ManyToOne
    @JoinColumn(name="piece_ref_id")
    private PieceJustificativeRef pieceRef;
    
    private String cheminFichier;
    private String nomFichier;
    private Long tailleBytes;
    private String mimeType;
    private Timestamp uploadedAt;
    
    /* Getters */
    public long getId(){ return this.id; }
    public Demande getDemande(){ return this.demande; }
    public PieceJustificativeRef getPieceRef(){ return this.pieceRef; }
    public String getCheminFichier(){ return this.cheminFichier; }
    public String getNomFichier(){ return this.nomFichier; }
    public Long getTailleBytes(){ return this.tailleBytes; }
    public String getMimeType(){ return this.mimeType; }
    public Timestamp getUploadedAt(){ return this.uploadedAt; }
    
    /* Setters */
    public void setId(long id){ this.id = id; }
    public void setDemande(Demande demande){ this.demande = demande; }
    public void setPieceRef(PieceJustificativeRef pieceRef){ this.pieceRef = pieceRef; }
    public void setCheminFichier(String cheminFichier){ this.cheminFichier = cheminFichier; }
    public void setNomFichier(String nomFichier){ this.nomFichier = nomFichier; }
    public void setTailleBytes(Long tailleBytes){ this.tailleBytes = tailleBytes; }
    public void setMimeType(String mimeType){ this.mimeType = mimeType; }
    public void setUploadedAt(Timestamp uploadedAt){ this.uploadedAt = uploadedAt; }
}