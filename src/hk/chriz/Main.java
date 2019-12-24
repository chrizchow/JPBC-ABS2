package hk.chriz;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

public class Main {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        String [] attributes = { "apple", "orange", "peach" , "banana", "dragonfruit",
                                 "pear", "lemon", "watermelon", "durian", "pineapple" };
        String message = "Hello World";

        // Setup Stage:
        ABS abs = new ABS();
        abs.setup();
        System.out.println("Setup Completed...\n");

        // Key Generation for attributes:
        ABSPrivKeyComp SK = abs.keygen(attributes);
        System.out.println("Keygen Completed...\n");

        // Sign using all attributes in private key:
        ABSSignatureComp signature = abs.sign(message, SK);
        System.out.println("Signature Completed...\n");

        // Verify all attributes:
        if (abs.verify(message, attributes, signature)) {
            System.out.println("Signature Verified Successfully!\n");
        } else {
            System.out.println("SIGNATURE INVALID !!!\n");
        }

    }
}
