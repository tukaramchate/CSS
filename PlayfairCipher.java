import java.util.Scanner;

public class PlayfairCipher {

    private final char[][] matrix = new char[5][5];

    public PlayfairCipher(String key) {
        buildMatrix(key);
    }

    private void buildMatrix(String key) {
        boolean[] used = new boolean[26];
        used['J' - 'A'] = true;

        StringBuilder merged = new StringBuilder();

        String cleanedKey = normalizeText(key);
        for (int i = 0; i < cleanedKey.length(); i++) {
            char ch = cleanedKey.charAt(i);
            if (!used[ch - 'A']) {
                used[ch - 'A'] = true;
                merged.append(ch);
            }
        }

        for (char ch = 'A'; ch <= 'Z'; ch++) {
            if (!used[ch - 'A']) {
                used[ch - 'A'] = true;
                merged.append(ch);
            }
        }

        int idx = 0;
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                matrix[r][c] = merged.charAt(idx++);
            }
        }
    }

    private String normalizeText(String text) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char ch = Character.toUpperCase(text.charAt(i));
            if (ch >= 'A' && ch <= 'Z') {
                if (ch == 'J') {
                    ch = 'I';
                }
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    private String prepareForEncryption(String text) {
        String cleaned = normalizeText(text);
        StringBuilder prepared = new StringBuilder();

        int i = 0;
        while (i < cleaned.length()) {
            char first = cleaned.charAt(i);
            char second;

            if (i + 1 < cleaned.length()) {
                second = cleaned.charAt(i + 1);
                if (first == second) {
                    prepared.append(first).append('X');
                    i++;
                } else {
                    prepared.append(first).append(second);
                    i += 2;
                }
            } else {
                prepared.append(first).append('X');
                i++;
            }
        }

        if (prepared.length() % 2 != 0) {
            prepared.append('X');
        }

        return prepared.toString();
    }

    private int[] findPosition(char ch) {
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                if (matrix[r][c] == ch) {
                    return new int[] { r, c };
                }
            }
        }
        return new int[] { -1, -1 };
    }

    public String encrypt(String plainText) {
        String prepared = prepareForEncryption(plainText);
        StringBuilder cipher = new StringBuilder();

        for (int i = 0; i < prepared.length(); i += 2) {
            char a = prepared.charAt(i);
            char b = prepared.charAt(i + 1);

            int[] p1 = findPosition(a);
            int[] p2 = findPosition(b);

            if (p1[0] == p2[0]) {
                cipher.append(matrix[p1[0]][(p1[1] + 1) % 5]);
                cipher.append(matrix[p2[0]][(p2[1] + 1) % 5]);
            } else if (p1[1] == p2[1]) {
                cipher.append(matrix[(p1[0] + 1) % 5][p1[1]]);
                cipher.append(matrix[(p2[0] + 1) % 5][p2[1]]);
            } else {
                cipher.append(matrix[p1[0]][p2[1]]);
                cipher.append(matrix[p2[0]][p1[1]]);
            }
        }

        return cipher.toString();
    }

    public String decrypt(String cipherText) {
        String cleaned = normalizeText(cipherText);
        if (cleaned.length() % 2 != 0) {
            throw new IllegalArgumentException("Cipher text must have even length after removing non-letters.");
        }

        StringBuilder plain = new StringBuilder();

        for (int i = 0; i < cleaned.length(); i += 2) {
            char a = cleaned.charAt(i);
            char b = cleaned.charAt(i + 1);

            int[] p1 = findPosition(a);
            int[] p2 = findPosition(b);

            if (p1[0] == p2[0]) {
                plain.append(matrix[p1[0]][(p1[1] + 4) % 5]);
                plain.append(matrix[p2[0]][(p2[1] + 4) % 5]);
            } else if (p1[1] == p2[1]) {
                plain.append(matrix[(p1[0] + 4) % 5][p1[1]]);
                plain.append(matrix[(p2[0] + 4) % 5][p2[1]]);
            } else {
                plain.append(matrix[p1[0]][p2[1]]);
                plain.append(matrix[p2[0]][p1[1]]);
            }
        }

        return plain.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Experiment performed by Yash Parbat -- C60");
        System.out.println("Playfair Cipher");
        System.out.print("Enter key: ");
        String key = scanner.nextLine();

        PlayfairCipher playfair = new PlayfairCipher(key);

        System.out.println("1. Encrypt");
        System.out.println("2. Decrypt");
        System.out.print("Choose (1/2): ");
        String choice = scanner.nextLine().trim();

        System.out.print("Enter message: ");
        String message = scanner.nextLine();

        try {
            if ("1".equals(choice)) {
                System.out.println("Encrypted: " + playfair.encrypt(message));
            } else if ("2".equals(choice)) {
                System.out.println("Decrypted: " + playfair.decrypt(message));
            } else {
                System.out.println("Invalid choice. Please choose 1 or 2.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }

        scanner.close();
    }
}
