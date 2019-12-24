package hk.chriz;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class MSP {

    static final int [][] M1 = {
            {1}};

    static final int [][] M2 = {
            {1,  1},
            {0, -1}};

    static final int [][] M3 = {
            {1,  1,  0},
            {0, -1,  1},
            {0,  0, -1}};

    static final int [][] M4 = {
            {1,  1,  0,  0},
            {0, -1,  1,  0},
            {0,  0, -1,  1},
            {0,  0,  0, -1}};

    static final int [][] M5 = {
            {1,  1,  0,  0,  0},
            {0, -1,  1,  0,  0},
            {0,  0, -1,  1,  0},
            {0,  0,  0, -1,  1},
            {0,  0,  0,  0, -1}};

    static final int [][] M6 = {
            {1,  1,  0,  0,  0,  0},
            {0, -1,  1,  0,  0,  0},
            {0,  0, -1,  1,  0,  0},
            {0,  0,  0, -1,  1,  0},
            {0,  0,  0,  0, -1,  1},
            {0,  0,  0,  0,  0, -1}};

    static final int [][] M7 = {
            {1,  1,  0,  0,  0,  0,  0},
            {0, -1,  1,  0,  0,  0,  0},
            {0,  0, -1,  1,  0,  0,  0},
            {0,  0,  0, -1,  1,  0,  0},
            {0,  0,  0,  0, -1,  1,  0},
            {0,  0,  0,  0,  0, -1,  1},
            {0,  0,  0,  0,  0,  0, -1}};

    static final int [][] M8 = {
            {1,  1,  0,  0,  0,  0,  0,  0},
            {0, -1,  1,  0,  0,  0,  0,  0},
            {0,  0, -1,  1,  0,  0,  0,  0},
            {0,  0,  0, -1,  1,  0,  0,  0},
            {0,  0,  0,  0, -1,  1,  0,  0},
            {0,  0,  0,  0,  0, -1,  1,  0},
            {0,  0,  0,  0,  0,  0, -1,  1},
            {0,  0,  0,  0,  0,  0,  0, -1}};

    static final int [][] M9 = {
            {1,  1,  0,  0,  0,  0,  0,  0,  0},
            {0, -1,  1,  0,  0,  0,  0,  0,  0},
            {0,  0, -1,  1,  0,  0,  0,  0,  0},
            {0,  0,  0, -1,  1,  0,  0,  0,  0},
            {0,  0,  0,  0, -1,  1,  0,  0,  0},
            {0,  0,  0,  0,  0, -1,  1,  0,  0},
            {0,  0,  0,  0,  0,  0, -1,  1,  0},
            {0,  0,  0,  0,  0,  0,  0, -1,  1},
            {0,  0,  0,  0,  0,  0,  0,  0, -1}};

    static final int [][] M10 = {
            {1,  1,  0,  0,  0,  0,  0,  0,  0,  0},
            {0, -1,  1,  0,  0,  0,  0,  0,  0,  0},
            {0,  0, -1,  1,  0,  0,  0,  0,  0,  0},
            {0,  0,  0, -1,  1,  0,  0,  0,  0,  0},
            {0,  0,  0,  0, -1,  1,  0,  0,  0,  0},
            {0,  0,  0,  0,  0, -1,  1,  0,  0,  0},
            {0,  0,  0,  0,  0,  0, -1,  1,  0,  0},
            {0,  0,  0,  0,  0,  0,  0, -1,  1,  0},
            {0,  0,  0,  0,  0,  0,  0,  0, -1,  1},
            {0,  0,  0,  0,  0,  0,  0,  0,  0, -1}};

    static final int [][][] cheatyMSPs = {null, M1, M2, M3, M4, M5, M6, M7, M8, M9, M10};

    int [][] M;
    ArrayList<Element> u;
    Element mu;

    private MSP () {}

    public static MSP getInstance(String message,
                                  ArrayList<String> attrs, Field Zr) throws NoSuchAlgorithmException {
        if (attrs.size() > 10) {
            System.err.println("currently only support no more than 10 attributes");
            System.exit(-1);
        }
        MSP msp = new MSP();

        // Get the Monotonic Span Program (MSP):
        msp.M = cheatyMSPs[attrs.size()];

        // Hash the attribute i into u(i):
        msp.u = new ArrayList<>();
        for (String attr: attrs) {
            Element e = Zr.newElement();
            elementFromString(e, attr);
            msp.u.add(e);
            System.out.println("Attr = "+attr+" ( u = "+e+" )");
        }

        // Get µ = H(m||Y):
        msp.mu = Zr.newElement();
        String mu_str = message + String.join(" AND ", attrs);
        elementFromString(msp.mu, mu_str);
        System.out.println("m||Y = \""+mu_str+"\" ( µ = "+msp.mu+" )");

        return msp;
    }

    // ======================= Utilities Functions Below =======================
    public static void elementFromString(Element h, String s)
            throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] digest = md.digest(s.getBytes());
        h.setFromHash(digest, 0, digest.length);
    }

}
