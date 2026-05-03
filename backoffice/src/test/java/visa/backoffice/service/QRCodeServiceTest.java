package visa.backoffice.service;

import com.google.zxing.WriterException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class QRCodeServiceTest {

    @InjectMocks
    private QRCodeService qrCodeService;

    @Test
    void generateQRCodeBase64_ShouldReturnValidBase64String() {
        // Given
        String refDemande = "20250422-143012-001";
        ReflectionTestUtils.setField(qrCodeService, "frontendUrl", "http://localhost:4200");
        ReflectionTestUtils.setField(qrCodeService, "suiviPath", "/suivi");

        // When
        String result = qrCodeService.generateQRCodeBase64(refDemande);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        
        // Vérifier que c'est un base64 valide
        assertDoesNotThrow(() -> Base64.getDecoder().decode(result));
    }

    @Test
    void generateQRCodeBytes_ShouldReturnValidByteArray() throws WriterException, IOException {
        // Given
        String refDemande = "20250422-143012-001";
        ReflectionTestUtils.setField(qrCodeService, "frontendUrl", "http://localhost:4200");
        ReflectionTestUtils.setField(qrCodeService, "suiviPath", "/suivi");

        // When
        byte[] result = qrCodeService.generateQRCodeBytes(refDemande, 250, 250);

        // Then
        assertNotNull(result);
        assertTrue(result.length > 0);
        
        // Vérifier l'en-tête PNG (premiers bytes)
        assertEquals((byte)0x89, result[0]); // PNG signature
        assertEquals((byte)0x50, result[1]);
        assertEquals((byte)0x4E, result[2]);
        assertEquals((byte)0x47, result[3]);
    }

    @Test
    void generateQRCodeBytes_WithDifferentSizes_ShouldWork() throws WriterException, IOException {
        // Given
        String refDemande = "TEST-123";
        ReflectionTestUtils.setField(qrCodeService, "frontendUrl", "http://localhost:4200");
        ReflectionTestUtils.setField(qrCodeService, "suiviPath", "/suivi");

        // When
        byte[] smallQR = qrCodeService.generateQRCodeBytes(refDemande, 100, 100);
        byte[] mediumQR = qrCodeService.generateQRCodeBytes(refDemande, 300, 300);
        byte[] largeQR = qrCodeService.generateQRCodeBytes(refDemande, 500, 500);

        // Then
        assertNotNull(smallQR);
        assertNotNull(mediumQR);
        assertNotNull(largeQR);
        
        // Les QR codes plus grands devraient générer plus de données
        assertTrue(largeQR.length >= mediumQR.length);
        assertTrue(mediumQR.length >= smallQR.length);
    }
}