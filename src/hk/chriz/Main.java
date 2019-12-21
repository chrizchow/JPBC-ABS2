package hk.chriz;

import java.security.NoSuchAlgorithmException;

public class Main {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        String [] attributes = { "apple", "orange", "peach" };
        ABS abs = new ABS();
        abs.setup();
        ABSPrivKeyComp SK = abs.keygen(attributes);
    }
}
