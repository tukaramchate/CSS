import java.util.Scanner;

public class CaesarCipher {

    public static String encrypt(String text, int shift) {
        StringBuilder result = new StringBuilder();
        shift = shift % 26;

        for (char ch : text.toCharArray()) {
            if (ch >= 'A' && ch <= 'Z') {
                char encrypted = (char) ((ch - 'A' + shift + 26) % 26 + 'A');
                result.append(encrypted);
            } else if (ch >= 'a' && ch <= 'z') {
                char encrypted = (char) ((ch - 'a' + shift + 26) % 26 + 'a');
                result.append(encrypted);
            } else {
                result.append(ch);
            }
        }

        return result.toString();
    }

    public static String decrypt(String text, int shift) {
        return encrypt(text, -shift);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Experiment performed by Yash Parbat -- C60");

        System.out.println("Caesar Cipher");
        System.out.println("1. Encrypt");
        System.out.println("2. Decrypt");
        System.out.print("Choose (1/2): ");
        String choice = scanner.nextLine().trim();

        System.out.print("Enter message: ");
        String message = scanner.nextLine();

        System.out.print("Enter shift value: ");
        int shift;
        try {
            shift = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Shift must be an integer.");
            scanner.close();
            return;
        }

        if ("1".equals(choice)) {
            System.out.println("Encrypted: " + encrypt(message, shift));
        } else if ("2".equals(choice)) {
            System.out.println("Decrypted: " + decrypt(message, shift));
        } else {
            System.out.println("Invalid choice. Please choose 1 or 2.");
        }

        scanner.close();
    }
}
