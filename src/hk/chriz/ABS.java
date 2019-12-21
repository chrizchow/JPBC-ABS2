package hk.chriz;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class ABS {

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

    ArrayList<int [][]> cheatyMSP = new ArrayList<>();

    private ABSPubKeyComp pubParam;     // Public Key Parameters
    private ABSMasterKeyComp mkParam;   // Master Key Parameters

    public ABS() {
        // Add Cheaty MSP:
        cheatyMSP.add(M1);
        cheatyMSP.add(M2);
        cheatyMSP.add(M3);
        cheatyMSP.add(M4);
        cheatyMSP.add(M5);
        cheatyMSP.add(M6);
        cheatyMSP.add(M7);
        cheatyMSP.add(M8);
        cheatyMSP.add(M9);
        cheatyMSP.add(M10);
    }

    public void setup() {

        // Initialise Pairing:
        pubParam = new ABSPubKeyComp();
        pubParam.pairing = PairingFactory.getPairing("a.properties");

        // just for convenience:
        pubParam.G1 = pubParam.pairing.getG1();
        pubParam.G2 = pubParam.pairing.getG2();
        pubParam.Gt = pubParam.pairing.getGT();
        pubParam.Zr = pubParam.pairing.getZr();

        // Set g = random generator:
        pubParam.g = pubParam.G1.newRandomElement();

        // Set master key a0, a, b, c = random:
        mkParam = new ABSMasterKeyComp();
        mkParam.a0 = pubParam.Zr.newRandomElement();
        mkParam.a = pubParam.Zr.newRandomElement();
        mkParam.b = pubParam.Zr.newRandomElement();
        mkParam.c = pubParam.Zr.newRandomElement();

        // Set C = g^c (at the end of P.20):
        pubParam.C = pubParam.g.duplicate();
        pubParam.C.powZn(mkParam.c);

        // Set h_0 to h_tmax = random in G2:
        pubParam.hi = new ArrayList<>();
        for (int i=0; i<pubParam.tmax+1; i++)   // from 0 to t_max totally t_max+1 items
            pubParam.hi.add(pubParam.G2.newRandomElement());

        // Set A0 = h0^a0:
        pubParam.A = new ArrayList<>();
        pubParam.A.add(pubParam.hi.get(0).duplicate().powZn(mkParam.a0));

        // Set A_1 ~ A_tmax:
        for (int i=1; i<pubParam.hi.size(); i++) {
            pubParam.A.add(pubParam.hi.get(i).duplicate().powZn(mkParam.a));
        }

        // Set B_1 ~ B_tmax:
        pubParam.B = new ArrayList<>();
        for (int i=1; i<pubParam.hi.size(); i++) {
            pubParam.B.add(pubParam.hi.get(i).duplicate().powZn(mkParam.b));
        }

        System.out.println("Size of H: "+pubParam.hi.size());
        System.out.println("Size of A: "+pubParam.A.size());
        System.out.println("Size of B: "+pubParam.B.size());

    }

    public ABSPrivKeyComp keygen(String [] attrs) throws NoSuchAlgorithmException {
        ABSPrivKeyComp comp = new ABSPrivKeyComp();

        // Set Kbase = random:
        comp.Kbase = pubParam.G1.newRandomElement();

        // Calculate K0 = Kbase^(1/a0):
        Element inv_a0 = mkParam.a0.duplicate().invert();
        comp.K0 = comp.Kbase.duplicate();
        comp.K0.powZn(inv_a0);

        // Calculate Ku (u ∈ attributes):
        comp.Ku = new ArrayList<>();
        for (String attr: attrs) {
            // Calculate 1/(a+bu):
            Element inv_a_bu = pubParam.Zr.newElement();
            elementFromString(inv_a_bu, attr);  // get hashed attribute (u)
            inv_a_bu.mul(mkParam.b);            // bu
            inv_a_bu.add(mkParam.a);            // a+bu
            inv_a_bu.invert();                  // 1/(a+bu)
            comp.Ku.add(comp.Kbase.duplicate().powZn(inv_a_bu));     // Kbase^(1/a+bu)
        }
        System.out.println("Secret Key Ku has "+comp.Ku.size()+" elements.");
        return comp;
    }



    // ======================= Utilities Functions Below =======================
    private static void elementFromString(Element h, String s)
            throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] digest = md.digest(s.getBytes());
        h.setFromHash(digest, 0, digest.length);
    }

}
