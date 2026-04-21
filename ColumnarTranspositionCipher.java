import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class ColumnarTranspositionCipher {

    private static class KeyOrder {
        int index;
        char ch;

        KeyOrder(int index, char ch) {
            this.index = index;
            this.ch = ch;
        }
    }

    private static int[] getColumnOrder(String key) {
        KeyOrder[] keyOrders = new KeyOrder[key.length()];
        for (int i = 0; i < key.length(); i++) {
            keyOrders[i] = new KeyOrder(i, key.charAt(i));
        }

        Arrays.sort(keyOrders, Comparator
                .comparingInt((KeyOrder k) -> k.ch)
                .thenComparingInt(k -> k.index));

        int[] order = new int[key.length()];
        for (int i = 0; i < keyOrders.length; i++) {
            order[i] = keyOrders[i].index;
        }
        return order;
    }

    public static String encrypt(String plainText, String key) {
        plainText = plainText.replaceAll("\\s+", "").toUpperCase();
        key = key.toUpperCase();

        int cols = key.length();
        int rows = (int) Math.ceil((double) plainText.length() / cols);
        char[][] matrix = new char[rows][cols];

        int k = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (k < plainText.length()) {
                    matrix[r][c] = plainText.charAt(k++);
                } else {
                    matrix[r][c] = 'X';
                }
            }
        }

        int[] order = getColumnOrder(key);
        StringBuilder cipherText = new StringBuilder();

        for (int idx : order) {
            for (int r = 0; r < rows; r++) {
                cipherText.append(matrix[r][idx]);
            }
        }

        return cipherText.toString();
    }

    public static String decrypt(String cipherText, String key) {
        cipherText = cipherText.replaceAll("\\s+", "").toUpperCase();
        key = key.toUpperCase();

        int cols = key.length();
        int rows = cipherText.length() / cols;
        char[][] matrix = new char[rows][cols];

        int[] order = getColumnOrder(key);
        int k = 0;

        for (int idx : order) {
            for (int r = 0; r < rows; r++) {
                matrix[r][idx] = cipherText.charAt(k++);
            }
        }

        StringBuilder plainText = new StringBuilder();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                plainText.append(matrix[r][c]);
            }
        }

        return plainText.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Experiment performed by Yash Parbat -- C60");
        System.out.println("Columnar Transposition Cipher");
        System.out.println("1. Encrypt");
        System.out.println("2. Decrypt");
        System.out.print("Choose (1/2): ");
        String choice = scanner.nextLine().trim();

        System.out.print("Enter text: ");
        String text = scanner.nextLine();

        System.out.print("Enter key (letters only): ");
        String key = scanner.nextLine().trim();

        if (key.isEmpty()) {
            System.out.println("Key cannot be empty.");
            scanner.close();
            return;
        }

        if (!key.matches("[a-zA-Z]+")) {
            System.out.println("Key must contain letters only.");
            scanner.close();
            return;
        }

        if ("1".equals(choice)) {
            System.out.println("Encrypted: " + encrypt(text, key));
        } else if ("2".equals(choice)) {
            String cleaned = text.replaceAll("\\s+", "");
            if (cleaned.length() % key.length() != 0) {
                System.out.println("For decryption, ciphertext length must be divisible by key length.");
            } else {
                System.out.println("Decrypted: " + decrypt(text, key));
            }
        } else {
            System.out.println("Invalid choice. Please choose 1 or 2.");
        }

        scanner.close();
    }
}
