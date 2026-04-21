import java.util.Scanner;

public class RailFenceCipher {

    public static String encrypt(String text, int rails) {
        if (rails <= 1 || text.length() <= 1) {
            return text;
        }

        StringBuilder[] fence = new StringBuilder[rails];
        for (int i = 0; i < rails; i++) {
            fence[i] = new StringBuilder();
        }

        int row = 0;
        boolean down = true;

        for (int i = 0; i < text.length(); i++) {
            fence[row].append(text.charAt(i));

            if (row == 0) {
                down = true;
            } else if (row == rails - 1) {
                down = false;
            }

            row += down ? 1 : -1;
        }

        StringBuilder cipher = new StringBuilder();
        for (StringBuilder rail : fence) {
            cipher.append(rail);
        }
        return cipher.toString();
    }

    public static String decrypt(String cipherText, int rails) {
        if (rails <= 1 || cipherText.length() <= 1) {
            return cipherText;
        }

        int length = cipherText.length();
        boolean[][] mark = new boolean[rails][length];

        int row = 0;
        boolean down = true;
        for (int col = 0; col < length; col++) {
            mark[row][col] = true;

            if (row == 0) {
                down = true;
            } else if (row == rails - 1) {
                down = false;
            }

            row += down ? 1 : -1;
        }

        char[][] fence = new char[rails][length];
        int index = 0;
        for (int r = 0; r < rails; r++) {
            for (int c = 0; c < length; c++) {
                if (mark[r][c] && index < length) {
                    fence[r][c] = cipherText.charAt(index++);
                }
            }
        }

        StringBuilder plain = new StringBuilder();
        row = 0;
        down = true;
        for (int col = 0; col < length; col++) {
            plain.append(fence[row][col]);

            if (row == 0) {
                down = true;
            } else if (row == rails - 1) {
                down = false;
            }

            row += down ? 1 : -1;
        }

        return plain.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Experiment performed by Yash Parbat -- C60");
        System.out.println("Rail Fence Cipher");
        System.out.println("1. Encrypt");
        System.out.println("2. Decrypt");
        System.out.print("Choose (1/2): ");
        String choice = scanner.nextLine().trim();

        System.out.print("Enter text: ");
        String text = scanner.nextLine();

        System.out.print("Enter number of rails: ");
        int rails;
        try {
            rails = Integer.parseInt(scanner.nextLine().trim());
            if (rails < 2) {
                System.out.println("Number of rails must be at least 2.");
                scanner.close();
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid number of rails.");
            scanner.close();
            return;
        }

        if ("1".equals(choice)) {
            System.out.println("Encrypted: " + encrypt(text, rails));
        } else if ("2".equals(choice)) {
            System.out.println("Decrypted: " + decrypt(text, rails));
        } else {
            System.out.println("Invalid choice. Please choose 1 or 2.");
        }

        scanner.close();
    }
}
