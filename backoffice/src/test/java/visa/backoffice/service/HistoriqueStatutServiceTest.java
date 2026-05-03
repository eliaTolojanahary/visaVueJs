package visa.backoffice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import visa.backoffice.entity.Demande;
import visa.backoffice.entity.HistoriqueStatut;
import visa.backoffice.entity.StatutDemande;
import visa.backoffice.repository.HistoriqueStatutRepository;

import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistoriqueStatutServiceTest {

    @Mock
    private HistoriqueStatutRepository historiqueStatutRepository;

    @Mock
    private DemandeService demandeService;

    @InjectMocks
    private HistoriqueStatutService historiqueStatutService;

    private Demande demande;
    private StatutDemande statut1;
    private StatutDemande statut2;
    private StatutDemande statut3;
    private List<HistoriqueStatut> historiqueList;

    @BeforeEach
    void setUp() {
        demande = new Demande();
        demande.setId(1L);
        demande.setRefDemande("20250422-143012-001");

        statut1 = new StatutDemande();
        statut1.setId(1L);
        statut1.setLibelle("CREEE");

        statut2 = new StatutDemande();
        statut2.setId(2L);
        statut2.setLibelle("EN_COURS");

        statut3 = new StatutDemande();
        statut3.setId(3L);
        statut3.setLibelle("APPROUVEE");

        HistoriqueStatut hist1 = new HistoriqueStatut();
        hist1.setId(1L);
        hist1.setDemande(demande);
        hist1.setStatut(statut1);
        hist1.setDateChangement(new Timestamp(System.currentTimeMillis() - 172800000));

        HistoriqueStatut hist2 = new HistoriqueStatut();
        hist2.setId(2L);
        hist2.setDemande(demande);
        hist2.setStatut(statut2);
        hist2.setDateChangement(new Timestamp(System.currentTimeMillis() - 86400000));

        HistoriqueStatut hist3 = new HistoriqueStatut();
        hist3.setId(3L);
        hist3.setDemande(demande);
        hist3.setStatut(statut3);
        hist3.setDateChangement(new Timestamp(System.currentTimeMillis()));

        historiqueList = Arrays.asList(hist1, hist2, hist3);
    }

    @Test
    void getHistoriqueParDemandeId_ShouldReturnOrderedHistory() {
        // Given
        Long demandeId = 1L;
        when(historiqueStatutRepository.findByDemandeIdOrderByDateChangementAsc(demandeId))
            .thenReturn(historiqueList);

        // When
        List<HistoriqueStatut> result = historiqueStatutService.getHistoriqueParDemandeId(demandeId);

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(statut1, result.get(0).getStatut());
        assertEquals(statut2, result.get(1).getStatut());
        assertEquals(statut3, result.get(2).getStatut());
        
        verify(historiqueStatutRepository).findByDemandeIdOrderByDateChangementAsc(demandeId);
    }

    @Test
    void getHistoriqueParNumeroDemande_ShouldReturnHistory() {
        // Given
        String refDemande = "20250422-143012-001";
        Map<String, Object> searchResult = new HashMap<>();
        searchResult.put("demandeCourante", demande);
        
        when(demandeService.rechercherParNumeroDemande(refDemande)).thenReturn(searchResult);
        when(historiqueStatutRepository.findByDemandeIdOrderByDateChangementAsc(demande.getId()))
            .thenReturn(historiqueList);

        // When
        List<HistoriqueStatut> result = historiqueStatutService.getHistoriqueParNumeroDemande(refDemande);

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(demandeService).rechercherParNumeroDemande(refDemande);
        verify(historiqueStatutRepository).findByDemandeIdOrderByDateChangementAsc(demande.getId());
    }

    @Test
    void getHistoriqueParNumeroDemande_WithInvalidNumber_ShouldReturnEmptyList() {
        // Given
        String invalidRef = "INVALID";
        when(demandeService.rechercherParNumeroDemande(invalidRef)).thenReturn(Collections.emptyMap());

        // When
        List<HistoriqueStatut> result = historiqueStatutService.getHistoriqueParNumeroDemande(invalidRef);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(demandeService).rechercherParNumeroDemande(invalidRef);
        verify(historiqueStatutRepository, never()).findByDemandeIdOrderByDateChangementAsc(anyLong());
    }

    @Test
    void getDernierStatut_ShouldReturnLatestStatus() {
        // Given
        Long demandeId = 1L;
        List<HistoriqueStatut> dernier = Collections.singletonList(historiqueList.get(2));
        
        when(historiqueStatutRepository.findTopByDemandeIdOrderByDateChangementDesc(demandeId))
            .thenReturn(dernier);

        // When
        Optional<HistoriqueStatut> result = historiqueStatutService.getDernierStatut(demandeId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(statut3, result.get().getStatut());
        verify(historiqueStatutRepository).findTopByDemandeIdOrderByDateChangementDesc(demandeId);
    }

    @Test
    void getDernierStatut_WithNoHistory_ShouldReturnEmpty() {
        // Given
        Long demandeId = 999L;
        when(historiqueStatutRepository.findTopByDemandeIdOrderByDateChangementDesc(demandeId))
            .thenReturn(Collections.emptyList());

        // When
        Optional<HistoriqueStatut> result = historiqueStatutService.getDernierStatut(demandeId);

        // Then
        assertFalse(result.isPresent());
        verify(historiqueStatutRepository).findTopByDemandeIdOrderByDateChangementDesc(demandeId);
    }

    @Test
    void aEuStatut_ShouldReturnTrueWhenStatusExists() {
        // Given
        Long demandeId = 1L;
        Long statutId = 2L;
        
        when(historiqueStatutRepository.findByDemandeIdOrderByDateChangementAsc(demandeId))
            .thenReturn(historiqueList);

        // When
        boolean hasStatus = historiqueStatutService.aEuStatut(demandeId, statutId);

        // Then
        assertTrue(hasStatus);
    }

    @Test
    void aEuStatut_ShouldReturnFalseWhenStatusNotExists() {
        // Given
        Long demandeId = 1L;
        Long statutId = 99L;
        
        when(historiqueStatutRepository.findByDemandeIdOrderByDateChangementAsc(demandeId))
            .thenReturn(historiqueList);

        // When
        boolean hasStatus = historiqueStatutService.aEuStatut(demandeId, statutId);

        // Then
        assertFalse(hasStatus);
    }
}