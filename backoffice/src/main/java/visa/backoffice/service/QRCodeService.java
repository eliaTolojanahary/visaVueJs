package visa.backoffice.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.Enumeration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

@Service
public class QRCodeService {
    
    @Value("${frontend.url:http://localhost:4200}")
    private String frontendUrl;
    
    @Value("${frontend.suivi.path:/suivi}")
    private String suiviPath;
    
    /**
     * Génère un QR code en format Base64 (pour retour JSON)
     * 
     * @param refDemande Numéro de la demande
     * @return Image QR code en Base64
     */
    public String generateQRCodeBase64(String refDemande) {
        try {
            String url = buildUrl(refDemande);
            byte[] qrCodeImage = generateQRCodeBytes(url, 250, 250);
            return Base64.getEncoder().encodeToString(qrCodeImage);
        } catch (WriterException | IOException e) {
            throw new RuntimeException("Erreur lors de la génération du QR code", e);
        }
    }
    
    /**
     * Génère un QR code en byte array (pour retour image)
     * 
     * @param refDemande Numéro de la demande
     * @param width Largeur en pixels
     * @param height Hauteur en pixels
     * @return Image QR code en bytes (PNG)
     */
    public byte[] generateQRCodeBytes(String refDemande, int width, int height) 
            throws WriterException, IOException {
        String url = buildUrl(refDemande);
        return generateQRCodeBytesFromURL(url, width, height);
    }
    
    /**
     * Construit l'URL complète avec le numéro de demande
     */
    private String buildUrl(String refDemande) {
        String resolvedFrontendUrl = resolveFrontendUrl();
        return resolvedFrontendUrl + suiviPath + "?numDemande=" + refDemande;
    }

    private String resolveFrontendUrl() {
        try {
            URI frontendUri = new URI(frontendUrl);
            String host = frontendUri.getHost();

            if (host == null || isLocalhost(host)) {
                host = resolveLocalIpv4Address();
            }

            int port = frontendUri.getPort();
            String scheme = frontendUri.getScheme() != null ? frontendUri.getScheme() : "http";
            String portPart = port >= 0 ? ":" + port : "";

            return scheme + "://" + host + portPart;
        } catch (URISyntaxException e) {
            return frontendUrl;
        }
    }

    private boolean isLocalhost(String host) {
        return "localhost".equalsIgnoreCase(host)
                || "127.0.0.1".equals(host)
                || "::1".equals(host);
    }

    private String resolveLocalIpv4Address() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                if (!networkInterface.isUp() || networkInterface.isLoopback() || networkInterface.isVirtual()) {
                    continue;
                }

                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    if (inetAddress instanceof Inet4Address && !inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }

            return InetAddress.getLocalHost().getHostAddress();
        } catch (IOException e) {
            return "127.0.0.1";
        }
    }
    
    /**
     * Génère un QR code à partir d'une URL
     */
    private byte[] generateQRCodeBytesFromURL(String url, int width, int height) 
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, width, height);
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        return outputStream.toByteArray();
    }
}