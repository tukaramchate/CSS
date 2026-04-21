import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Scanner;

public class DiffieHellmanKeyExchange {

    private static final SecureRandom RANDOM = new SecureRandom();

    private static BigInteger generatePrivateKey(BigInteger p) {
        BigInteger min = BigInteger.TWO;
        BigInteger max = p.subtract(BigInteger.TWO);
        BigInteger privateKey;

        do {
            privateKey = new BigInteger(p.bitLength(), RANDOM);
        } while (privateKey.compareTo(min) < 0 || privateKey.compareTo(max) > 0);

        return privateKey;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Experiment performed by Yash Parbat -- C60");

        try {
            System.out.println("Diffie-Hellman Key Exchange");
            System.out.println("1. Use default parameters");
            System.out.println("2. Enter custom parameters");
            System.out.print("Choose (1/2): ");
            String choice = scanner.nextLine().trim();

            BigInteger p;
            BigInteger g;

            if ("2".equals(choice)) {
                System.out.print("Enter prime number p: ");
                p = new BigInteger(scanner.nextLine().trim());

                if (!p.isProbablePrime(50)) {
                    System.out.println("Error: p must be a prime number.");
                    scanner.close();
                    return;
                }

                System.out.print("Enter generator g: ");
                g = new BigInteger(scanner.nextLine().trim());

                if (g.compareTo(BigInteger.TWO) < 0 || g.compareTo(p.subtract(BigInteger.ONE)) >= 0) {
                    System.out.println("Error: g must be in range [2, p-2].");
                    scanner.close();
                    return;
                }
            } else {
                // Small demo-safe defaults for classroom use.
                p = new BigInteger("23");
                g = new BigInteger("5");
            }

            BigInteger a = generatePrivateKey(p);
            BigInteger b = generatePrivateKey(p);

            BigInteger alicePublic = g.modPow(a, p);
            BigInteger bobPublic = g.modPow(b, p);

            BigInteger aliceSharedSecret = bobPublic.modPow(a, p);
            BigInteger bobSharedSecret = alicePublic.modPow(b, p);

            System.out.println("\nPublic Parameters:");
            System.out.println("p = " + p);
            System.out.println("g = " + g);

            System.out.println("\nAlice:");
            System.out.println("Private key (a) = " + a);
            System.out.println("Public key (A = g^a mod p) = " + alicePublic);

            System.out.println("\nBob:");
            System.out.println("Private key (b) = " + b);
            System.out.println("Public key (B = g^b mod p) = " + bobPublic);

            System.out.println("\nShared Secrets:");
            System.out.println("Alice computes (B^a mod p) = " + aliceSharedSecret);
            System.out.println("Bob computes   (A^b mod p) = " + bobSharedSecret);

            if (aliceSharedSecret.equals(bobSharedSecret)) {
                System.out.println("\nKey Exchange Successful: Shared secret matches.");
            } else {
                System.out.println("\nKey Exchange Failed: Shared secret does not match.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter valid numeric values.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
