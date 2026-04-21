import java.security.SecureRandom;
import java.util.Scanner;

public class OneTimePadCipher {

    private static final SecureRandom RANDOM = new SecureRandom();

    private static String normalize(String text) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char ch = Character.toUpperCase(text.charAt(i));
            if (ch >= 'A' && ch <= 'Z') {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    public static String generateRandomKey(int length) {
        StringBuilder key = new StringBuilder();
        for (int i = 0; i < length; i++) {
            key.append((char) ('A' + RANDOM.nextInt(26)));
        }
        return key.toString();
    }

    public static String encrypt(String plainText, String key) {
        String text = normalize(plainText);
        String normalizedKey = normalize(key);

        if (text.length() != normalizedKey.length()) {
            throw new IllegalArgumentException("Key length must match plaintext length (letters only).");
        }

        StringBuilder cipher = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            int p = text.charAt(i) - 'A';
            int k = normalizedKey.charAt(i) - 'A';
            cipher.append((char) ((p + k) % 26 + 'A'));
        }
        return cipher.toString();
    }

    public static String decrypt(String cipherText, String key) {
        String text = normalize(cipherText);
        String normalizedKey = normalize(key);

        if (text.length() != normalizedKey.length()) {
            throw new IllegalArgumentException("Key length must match ciphertext length (letters only).");
        }

        StringBuilder plain = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            int c = text.charAt(i) - 'A';
            int k = normalizedKey.charAt(i) - 'A';
            plain.append((char) ((c - k + 26) % 26 + 'A'));
        }
        return plain.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Experiment performed by Yash Parbat -- C60");
        System.out.println("One Time Pad Cipher");
        System.out.println("1. Encrypt");
        System.out.println("2. Decrypt");
        System.out.print("Choose (1/2): ");
        String choice = scanner.nextLine().trim();

        try {
            if ("1".equals(choice)) {
                System.out.print("Enter plaintext: ");
                String plainText = scanner.nextLine();
                String normalizedText = normalize(plainText);

                if (normalizedText.isEmpty()) {
                    System.out.println("Plaintext must contain at least one letter.");
                    scanner.close();
                    return;
                }

                System.out.print("Enter key (leave blank to auto-generate): ");
                String enteredKey = scanner.nextLine();

                String key;
                if (enteredKey.trim().isEmpty()) {
                    key = generateRandomKey(normalizedText.length());
                } else {
                    key = normalize(enteredKey);
                    if (key.length() != normalizedText.length()) {
                        System.out.println("Error: Key length must match plaintext length (letters only).");
                        scanner.close();
                        return;
                    }
                }

                String cipher = encrypt(plainText, key);

                System.out.println("Normalized Plaintext: " + normalizedText);
                System.out.println("Key Used:            " + key);
                System.out.println("Ciphertext:          " + cipher);
            } else if ("2".equals(choice)) {
                System.out.print("Enter ciphertext: ");
                String cipherText = scanner.nextLine();
                System.out.print("Enter key: ");
                String key = scanner.nextLine();

                String plain = decrypt(cipherText, key);
                System.out.println("Decrypted Text: " + plain);
            } else {
                System.out.println("Invalid choice. Please choose 1 or 2.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }

        scanner.close();
    }
}
