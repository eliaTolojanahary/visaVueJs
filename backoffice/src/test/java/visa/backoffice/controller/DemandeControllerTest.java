package visa.backoffice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import visa.backoffice.backoffice.BackofficeApplication;
import visa.backoffice.entity.Demande;
import visa.backoffice.entity.Passeport;
import visa.backoffice.service.DemandeService;
import visa.backoffice.service.HistoriqueStatutService;
import visa.backoffice.service.QRCodeService;

import java.sql.Timestamp;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = BackofficeApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class DemandeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DemandeService demandeService;

    @MockBean
    private HistoriqueStatutService historiqueService;

    @MockBean
    private QRCodeService qrCodeService;

    private Demande demande;
    private Passeport passeport;
    private List<Demande> autresDemandes;

    @BeforeEach
    void setUp() {
        passeport = new Passeport();
        passeport.setId(1L);
        passeport.setNumPasseport("P12345678");

        demande = new Demande();
        demande.setId(1L);
        demande.setRefDemande("20250422-143012-001");
        demande.setPasseport(passeport);
        demande.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        Demande autreDemande = new Demande();
        autreDemande.setId(2L);
        autreDemande.setRefDemande("20250422-143012-002");
        autreDemande.setPasseport(passeport);
        autreDemande.setCreatedAt(new Timestamp(System.currentTimeMillis() - 86400000));

        autresDemandes = Collections.singletonList(autreDemande);
    }

    @Test
    void getDemandes_ByNumDemande_ShouldReturnDemandeWithOthers() throws Exception {
        String numDemande = "20250422-143012-001";
        Map<String, Object> serviceResult = new HashMap<>();
        serviceResult.put("demandeCourante", demande);
        serviceResult.put("autresDemandes", autresDemandes);
        serviceResult.put("nombreTotalDemandes", 2);

        List<visa.backoffice.entity.HistoriqueStatut> historique = Collections.emptyList();
        String qrCodeBase64 = "dGVzdC1xci1jb2Rl";

        when(demandeService.rechercherParNumeroDemande(numDemande)).thenReturn(serviceResult);
        when(historiqueService.getHistoriqueParDemandeId(demande.getId())).thenReturn(historique);
        when(qrCodeService.generateQRCodeBase64(numDemande)).thenReturn(qrCodeBase64);

        mockMvc.perform(get("/api/demandes")
                .param("numDemande", numDemande)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.demandeCourante.refDemande").value("20250422-143012-001"))
                .andExpect(jsonPath("$.autresDemandes.length()").value(1))
                .andExpect(jsonPath("$.nombreTotalDemandes").value(2));
    }

    @Test
    void getDemandes_ByNumDemande_NotFound_ShouldReturn404() throws Exception {
        String numDemande = "INVALID";
        when(demandeService.rechercherParNumeroDemande(numDemande)).thenReturn(Collections.emptyMap());

        mockMvc.perform(get("/api/demandes")
                .param("numDemande", numDemande)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getDemandes_ByNumPasseport_ShouldReturnDemandesList() throws Exception {
        String numPasseport = "P12345678";
        List<Demande> demandes = Arrays.asList(demande, autresDemandes.get(0));
        
        when(demandeService.rechercherParNumeroPasseport(numPasseport)).thenReturn(demandes);

        mockMvc.perform(get("/api/demandes")
                .param("numPasseport", numPasseport)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getDemandes_WithoutParameters_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/demandes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}