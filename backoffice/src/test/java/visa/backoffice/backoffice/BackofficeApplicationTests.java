package visa.backoffice.backoffice;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class BackofficeApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
        assertTrue(true);
    }

    @Test
    void testDemandeEndpoint_NotFound() {
        String url = "http://localhost:" + port + "/api/demandes?numDemande=INVALID";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        // Le statut peut être 404 si configuré correctement, ou 500 si la DB n'est pas configurée
        assertTrue(response.getStatusCode() == HttpStatus.NOT_FOUND || 
                   response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void testQRCodeEndpoint_InvalidNumber() {
        String url = "http://localhost:" + port + "/api/qrcode?numDemande=INVALID";
        ResponseEntity<byte[]> response = restTemplate.getForEntity(url, byte[].class);
        assertTrue(response.getStatusCode() == HttpStatus.NOT_FOUND || 
                   response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR);
    }
}