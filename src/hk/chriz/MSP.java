package hk.chriz;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import org.w3c.dom.html.HTMLLabelElement;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class MSP {

    static int [][] M1 = {
            {1}};

    static int [][] M2 = {
            {1,  1},
            {0, -1}};

    static int [][] M3 = {
            {1,  1,  0},
            {0, -1,  1},
            {0,  0, -1}};

    static int [][] M4 = {
            {1,  1,  0,  0},
            {0, -1,  1,  0},
            {0,  0, -1,  1},
            {0,  0,  0, -1}};

    static int [][] M5 = {
            {1,  1,  0,  0,  0},
            {0, -1,  1,  0,  0},
            {0,  0, -1,  1,  0},
            {0,  0,  0, -1,  1},
            {0,  0,  0,  0, -1}};

    static int [][] M6 = {
            {1,  1,  0,  0,  0,  0},
            {0, -1,  1,  0,  0,  0},
            {0,  0, -1,  1,  0,  0},
            {0,  0,  0, -1,  1,  0},
            {0,  0,  0,  0, -1,  1},
            {0,  0,  0,  0,  0, -1}};

    static int [][] M7 = {
            {1,  1,  0,  0,  0,  0,  0},
            {0, -1,  1,  0,  0,  0,  0},
            {0,  0, -1,  1,  0,  0,  0},
            {0,  0,  0, -1,  1,  0,  0},
            {0,  0,  0,  0, -1,  1,  0},
            {0,  0,  0,  0,  0, -1,  1},
            {0,  0,  0,  0,  0,  0, -1}};

    static int [][] M8 = {
            {1,  1,  0,  0,  0,  0,  0,  0},
            {0, -1,  1,  0,  0,  0,  0,  0},
            {0,  0, -1,  1,  0,  0,  0,  0},
            {0,  0,  0, -1,  1,  0,  0,  0},
            {0,  0,  0,  0, -1,  1,  0,  0},
            {0,  0,  0,  0,  0, -1,  1,  0},
            {0,  0,  0,  0,  0,  0, -1,  1},
            {0,  0,  0,  0,  0,  0,  0, -1}};

    static int [][] M9 = {
            {1,  1,  0,  0,  0,  0,  0,  0,  0},
            {0, -1,  1,  0,  0,  0,  0,  0,  0},
            {0,  0, -1,  1,  0,  0,  0,  0,  0},
            {0,  0,  0, -1,  1,  0,  0,  0,  0},
            {0,  0,  0,  0, -1,  1,  0,  0,  0},
            {0,  0,  0,  0,  0, -1,  1,  0,  0},
            {0,  0,  0,  0,  0,  0, -1,  1,  0},
            {0,  0,  0,  0,  0,  0,  0, -1,  1},
            {0,  0,  0,  0,  0,  0,  0,  0, -1}};

    static int [][] M10 = {
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

    static int [][][] cheatyMSPs = {null, M1, M2, M3, M4, M5, M6, M7, M8, M9, M10};

    int [][] M;
    ArrayList<Element> u;
    Element mu;

    private MSP () {}

    public static MSP getInstance(String message,
                                  ArrayList<String> attrs, Field Zr) throws NoSuchAlgorithmException {
        assert (attrs.size() > 10) : "currently only support no more than 10 attributes";
        MSP msp = new MSP();
        msp.M = cheatyMSPs[attrs.size()];
        msp.u = new ArrayList<>();
        for (String attr: attrs) {
            Element e = Zr.newElement();
            ABS.elementFromString(e, attr);
            msp.u.add(e);
        }
        msp.mu = Zr.newElement();
        String mu_str = message + String.join(" AND ", attrs);
        ABS.elementFromString(msp.mu, mu_str);
        System.out.println("mu_str: "+mu_str);
        return msp;
    }

}
