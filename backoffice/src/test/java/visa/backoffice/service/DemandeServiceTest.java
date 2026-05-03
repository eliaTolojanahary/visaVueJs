package visa.backoffice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import visa.backoffice.entity.Demande;
import visa.backoffice.entity.Passeport;
import visa.backoffice.repository.DemandeRepository;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DemandeServiceTest {

    @Mock
    private DemandeRepository demandeRepository;

    @InjectMocks
    private DemandeService demandeService;

    private Demande demande1;
    private Demande demande2;
    private Demande demande3;
    private Passeport passeport;

    @BeforeEach
    void setUp() {
        // Création d'un passeport
        passeport = new Passeport();
        passeport.setId(1L);
        passeport.setNumPasseport("P12345678");

        // Création des demandes
        demande1 = new Demande();
        demande1.setId(1L);
        demande1.setRefDemande("20250422-143012-001");
        demande1.setPasseport(passeport);
        demande1.setCreatedAt(new Timestamp(System.currentTimeMillis() - 86400000)); // hier

        demande2 = new Demande();
        demande2.setId(2L);
        demande2.setRefDemande("20250422-143012-002");
        demande2.setPasseport(passeport);
        demande2.setCreatedAt(new Timestamp(System.currentTimeMillis())); // aujourd'hui

        demande3 = new Demande();
        demande3.setId(3L);
        demande3.setRefDemande("20250422-143012-003");
        demande3.setPasseport(passeport);
        demande3.setCreatedAt(new Timestamp(System.currentTimeMillis() - 172800000)); // avant-hier
    }

    @Test
    void rechercherParNumeroDemande_WithValidNumber_ShouldReturnDemandeAndOthers() {
        // Given
        String refDemande = "20250422-143012-002";
        List<Demande> toutesDemandes = Arrays.asList(demande2, demande1, demande3);
        
        when(demandeRepository.findByRefDemande(refDemande)).thenReturn(Optional.of(demande2));
        when(demandeRepository.findByPasseportNumPasseportOrderByCreatedAtDesc(passeport.getNumPasseport()))
            .thenReturn(toutesDemandes);

        // When
        Map<String, Object> result = demandeService.rechercherParNumeroDemande(refDemande);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(demande2, result.get("demandeCourante"));
        
        @SuppressWarnings("unchecked")
        List<Demande> autresDemandes = (List<Demande>) result.get("autresDemandes");
        assertEquals(2, autresDemandes.size());
        assertFalse(autresDemandes.contains(demande2));
        
        assertEquals(3, result.get("nombreTotalDemandes"));
        
        verify(demandeRepository).findByRefDemande(refDemande);
        verify(demandeRepository).findByPasseportNumPasseportOrderByCreatedAtDesc(passeport.getNumPasseport());
    }

    @Test
    void rechercherParNumeroDemande_WithInvalidNumber_ShouldReturnEmptyMap() {
        // Given
        String invalidRef = "INVALID-REF";
        when(demandeRepository.findByRefDemande(invalidRef)).thenReturn(Optional.empty());

        // When
        Map<String, Object> result = demandeService.rechercherParNumeroDemande(invalidRef);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(demandeRepository).findByRefDemande(invalidRef);
        verify(demandeRepository, never()).findByPasseportNumPasseportOrderByCreatedAtDesc(anyString());
    }

    @Test
    void rechercherParNumeroPasseport_ShouldReturnDemandesOrderedByDate() {
        // Given
        String numPasseport = "P12345678";
        List<Demande> demandesAttendues = Arrays.asList(demande2, demande1, demande3);
        
        when(demandeRepository.findByPasseportNumPasseportOrderByCreatedAtDesc(numPasseport))
            .thenReturn(demandesAttendues);

        // When
        List<Demande> result = demandeService.rechercherParNumeroPasseport(numPasseport);

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(demande2, result.get(0)); // Le plus récent en premier
        assertEquals(demande1, result.get(1));
        assertEquals(demande3, result.get(2));
        
        verify(demandeRepository).findByPasseportNumPasseportOrderByCreatedAtDesc(numPasseport);
    }

    @Test
    void existeParNumeroDemande_ShouldReturnTrueWhenExists() {
        // Given
        String refDemande = "20250422-143012-001";
        when(demandeRepository.findByRefDemande(refDemande)).thenReturn(Optional.of(demande1));

        // When
        boolean exists = demandeService.existeParNumeroDemande(refDemande);

        // Then
        assertTrue(exists);
        verify(demandeRepository).findByRefDemande(refDemande);
    }

    @Test
    void existeParNumeroDemande_ShouldReturnFalseWhenNotExists() {
        // Given
        String refDemande = "NON-EXISTANT";
        when(demandeRepository.findByRefDemande(refDemande)).thenReturn(Optional.empty());

        // When
        boolean exists = demandeService.existeParNumeroDemande(refDemande);

        // Then
        assertFalse(exists);
        verify(demandeRepository).findByRefDemande(refDemande);
    }

    @Test
    void compterDemandesParPasseport_ShouldReturnCorrectCount() {
        // Given
        String numPasseport = "P12345678";
        List<Demande> demandes = Arrays.asList(demande1, demande2, demande3);
        
        when(demandeRepository.findByPasseportNumPasseportOrderByCreatedAtDesc(numPasseport))
            .thenReturn(demandes);

        // When
        long count = demandeService.compterDemandesParPasseport(numPasseport);

        // Then
        assertEquals(3, count);
        verify(demandeRepository).findByPasseportNumPasseportOrderByCreatedAtDesc(numPasseport);
    }
}