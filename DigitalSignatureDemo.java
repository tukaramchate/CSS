import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;
import java.util.Scanner;

public class DigitalSignatureDemo {

    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    public static String createSignature(String message, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(message.getBytes(StandardCharsets.UTF_8));
        byte[] signedBytes = signature.sign();
        return Base64.getEncoder().encodeToString(signedBytes);
    }

    public static boolean verifySignature(String message, String signatureBase64, PublicKey publicKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(message.getBytes(StandardCharsets.UTF_8));
        byte[] signatureBytes = Base64.getDecoder().decode(signatureBase64);
        return signature.verify(signatureBytes);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Experiment performed by Yash Parbat -- C60");

        try {
            System.out.println("Digital Signature Algorithm in Java");
            System.out.println("Generating RSA key pair...");

            KeyPair keyPair = generateKeyPair();
            PrivateKey privateKey = keyPair.getPrivate();
            PublicKey publicKey = keyPair.getPublic();

            System.out.print("Enter message to sign: ");
            String message = scanner.nextLine();

            String digitalSignature = createSignature(message, privateKey);
            boolean isValid = verifySignature(message, digitalSignature, publicKey);

            System.out.println("\nOriginal Message: " + message);
            System.out.println("Digital Signature (Base64): " + digitalSignature);
            System.out.println("Verification Result: " + (isValid ? "VALID" : "INVALID"));

            System.out.print("\nEnter a tampered message to test verification (or press Enter to skip): ");
            String tamperedMessage = scanner.nextLine();
            if (!tamperedMessage.isEmpty()) {
                boolean tamperedValid = verifySignature(tamperedMessage, digitalSignature, publicKey);
                System.out.println("Verification for Tampered Message: " + (tamperedValid ? "VALID" : "INVALID"));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
