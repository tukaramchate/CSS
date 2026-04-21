import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.Cipher;

public class RSASecureSystem {

    public static KeyPair generateKeyPair(int keySize) throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(keySize);
        return generator.generateKeyPair();
    }

    public static String encrypt(String plainText, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String decrypt(String cipherTextBase64, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] encryptedBytes = Base64.getDecoder().decode(cipherTextBase64);
        byte[] decrypted = cipher.doFinal(encryptedBytes);
        return new String(decrypted, StandardCharsets.UTF_8);
    }

    public static String sign(String message, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(message.getBytes(StandardCharsets.UTF_8));
        byte[] signed = signature.sign();
        return Base64.getEncoder().encodeToString(signed);
    }

    public static boolean verify(String message, String signatureBase64, PublicKey publicKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(message.getBytes(StandardCharsets.UTF_8));
        byte[] signedBytes = Base64.getDecoder().decode(signatureBase64);
        return signature.verify(signedBytes);
    }

    public static PublicKey parsePublicKey(String base64PublicKey) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(base64PublicKey);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return factory.generatePublic(spec);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("RSA Secure System");
            System.out.println("Generating 2048-bit RSA key pair...");
            KeyPair keyPair = generateKeyPair(2048);
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            String publicKeyBase64 = Base64.getEncoder().encodeToString(publicKey.getEncoded());
            System.out.println("Public Key (Base64):");
            System.out.println(publicKeyBase64);

            System.out.println("\n1. Encrypt and Decrypt");
            System.out.println("2. Sign and Verify");
            System.out.print("Choose (1/2): ");
            String choice = scanner.nextLine().trim();

            if ("1".equals(choice)) {
                System.out.print("Enter plaintext message: ");
                String plainText = scanner.nextLine();

                String cipherText = encrypt(plainText, publicKey);
                String decryptedText = decrypt(cipherText, privateKey);

                System.out.println("Ciphertext (Base64): " + cipherText);
                System.out.println("Decrypted Text:      " + decryptedText);
            } else if ("2".equals(choice)) {
                System.out.print("Enter message to sign: ");
                String message = scanner.nextLine();

                String digitalSignature = sign(message, privateKey);
                boolean isValid = verify(message, digitalSignature, publicKey);

                System.out.println("Signature (Base64): " + digitalSignature);
                System.out.println("Verification: " + (isValid ? "VALID" : "INVALID"));
            } else {
                System.out.println("Invalid choice. Please choose 1 or 2.");
            }

            // This parse call demonstrates how to reconstruct a public key from Base64.
            parsePublicKey(publicKeyBase64);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
