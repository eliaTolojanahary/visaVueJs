package visa.backoffice.controller;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import visa.backoffice.backoffice.BackofficeApplication;
import visa.backoffice.service.DemandeService;
import visa.backoffice.service.QRCodeService;

@SpringBootTest(classes = BackofficeApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class QRCodeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QRCodeService qrCodeService;

    @MockBean
    private DemandeService demandeService;

    @Test
    void generateQRCode_WithValidNumber_ShouldReturnPNGImage() throws Exception {
        String numDemande = "20250422-143012-001";
        byte[] qrCodeBytes = { (byte)0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A };
        
        when(demandeService.existeParNumeroDemande(numDemande)).thenReturn(true);
        when(qrCodeService.generateQRCodeBytes(eq(numDemande), anyInt(), anyInt())).thenReturn(qrCodeBytes);

        mockMvc.perform(get("/api/qrcode")
                .param("numDemande", numDemande))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG));
    }

    @Test
    void generateQRCode_WithInvalidNumber_ShouldReturn404() throws Exception {
        String numDemande = "INVALID";
        when(demandeService.existeParNumeroDemande(numDemande)).thenReturn(false);

        mockMvc.perform(get("/api/qrcode")
                .param("numDemande", numDemande))
                .andExpect(status().isNotFound());
    }

    @Test
    void generateQRCodeLegacy_WithValidNumber_ShouldReturnPNGImage() throws Exception {
        String numDemande = "20250422-143012-001";
        byte[] qrCodeBytes = { (byte)0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A };

        when(demandeService.existeParNumeroDemande(numDemande)).thenReturn(true);
        when(qrCodeService.generateQRCodeBytes(eq(numDemande), anyInt(), anyInt())).thenReturn(qrCodeBytes);

        mockMvc.perform(get("/api/qrcodes/{numDemande}.png", numDemande))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG));
    }

    @Test
    void generateQRCodeBase64_WithValidNumber_ShouldReturnBase64() throws Exception {
        String numDemande = "20250422-143012-001";
        String qrCodeBase64 = "dGVzdC1iYXNlNjQtcXItY29kZQ==";
        
        when(demandeService.existeParNumeroDemande(numDemande)).thenReturn(true);
        when(qrCodeService.generateQRCodeBase64(numDemande)).thenReturn(qrCodeBase64);

        mockMvc.perform(get("/api/qrcode/base64")
                .param("numDemande", numDemande)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numDemande").value(numDemande));
    }

    @Test
    void generateQRCodeBase64_WithInvalidNumber_ShouldReturn404() throws Exception {
        String numDemande = "INVALID";
        when(demandeService.existeParNumeroDemande(numDemande)).thenReturn(false);

        mockMvc.perform(get("/api/qrcode/base64")
                .param("numDemande", numDemande)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}