package visa.backoffice.controller;

import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import visa.backoffice.service.DemandeService;
import visa.backoffice.service.QRCodeService;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api")
// Ne pas mettre @CrossOrigin ici
public class QRCodeController {
    
    @Autowired
    private QRCodeService qrCodeService;
    
    @Autowired
    private DemandeService demandeService;
    
    @GetMapping(value = "/qrcode", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> generateQRCode(@RequestParam String numDemande) {
        if (!demandeService.existeParNumeroDemande(numDemande)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        
        try {
            byte[] qrCodeImage = qrCodeService.generateQRCodeBytes(numDemande, 300, 300);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            headers.setContentDispositionFormData("filename", "qrcode_" + numDemande + ".png");
            
            return new ResponseEntity<>(qrCodeImage, headers, HttpStatus.OK);
        } catch (WriterException | IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/qrcode/base64")
    public ResponseEntity<?> generateQRCodeBase64(@RequestParam String numDemande) {
        if (!demandeService.existeParNumeroDemande(numDemande)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Aucune demande trouvée avec le numéro: " + numDemande);
        }
        
        try {
            String qrCodeBase64 = qrCodeService.generateQRCodeBase64(numDemande);
            return ResponseEntity.ok(Map.of(
                "numDemande", numDemande,
                "qrCode", "data:image/png;base64," + qrCodeBase64
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la génération du QR code: " + e.getMessage());
        }
    }
}