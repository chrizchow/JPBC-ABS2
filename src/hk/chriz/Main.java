package hk.chriz;

import java.security.NoSuchAlgorithmException;

public class Main {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        String [] attributes = { "apple", "orange", "peach" };
        String message = "I google everything.";

        // Setup Stage:
        ABS abs = new ABS();
        abs.setup();
        System.out.println("Setup Completed...");

        // Key Generation for attributes:
        ABSPrivKeyComp SK = abs.keygen(attributes);
        System.out.println("Keygen Completed...");

        // Sign using all attributes in private key:
        ABSSignatureComp signature = abs.sign(message, SK);
        System.out.println("Signature Completed...");

        // Verify all attributes:
        if (abs.verify(message, attributes, signature)) {
            System.out.println("Signature Verified Successfully!");
        } else {
            System.out.println("SIGNATURE INVALID !!!");
        }
    }
}
