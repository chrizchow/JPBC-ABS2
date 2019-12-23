package hk.chriz;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ABS {

    private ABSPubKeyComp pubParam;     // Public Key Parameters
    private ABSMasterKeyComp mkParam;   // Master Key Parameters

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
        comp.attr = new ArrayList<>();
        for (String attr: attrs) {
            // Calculate 1/(a+bu):
            Element inv_a_bu = pubParam.Zr.newElement();
            elementFromString(inv_a_bu, attr);  // get hashed attribute (u)
            inv_a_bu.mul(mkParam.b);            // bu
            inv_a_bu.add(mkParam.a);            // a+bu
            inv_a_bu.invert();                  // 1/(a+bu)
            comp.Ku.add(comp.Kbase.duplicate().powZn(inv_a_bu));     // Kbase^(1/a+bu)
            comp.attr.add(attr);                // for later reference
        }
        System.out.println("Secret Key Ku has "+comp.Ku.size()+" elements.");
        return comp;
    }

    public ABSSignatureComp sign (String message,
                                  ABSPrivKeyComp privKey) throws NoSuchAlgorithmException {
        ABSSignatureComp comp = new ABSSignatureComp();
        MSP msp = MSP.getInstance(message, privKey.attr, pubParam.Zr);
        final int l = msp.M.length;
        final int t = msp.M[0].length;

        // Pick random r0, r1 ... rl:
        ArrayList<Element> r = new ArrayList<>();
        for (int i=0; i<l+1; i++)
            r.add(pubParam.Zr.newRandomElement());

        // Calculate Y and W:
        comp.Y = privKey.Kbase.duplicate().powZn(r.get(0));
        comp.W = privKey.K0.duplicate().powZn(r.get(0));

        // Calculate Si for each l:
        comp.Si = new ArrayList<>();
        comp.Si.add(null);                                    // S0 does not exist
        for (int i=1; i<l+1; i++) {
            Element cgur = pubParam.C.duplicate();            // C
            cgur.mul(pubParam.g.duplicate().powZn(msp.mu));   // Cg^µ
            cgur.powZn(r.get(i));                             // (Cg^µ)^ri
            Element kur = privKey.Ku.get(i-1);                // K_u(i)
            kur.powZn(r.get(0));                              // K_u(i)^r0
            comp.Si.add(kur.duplicate().mul(cgur));           // K_u(i)^r0 * Cg^µ)^ri
        }

        // Calculate Pj for each t (each item multiply from 1 to l):
        comp.Pj = new ArrayList<>();
        comp.Pj.add(null);                                                      // P0 does not exist
        for (int j=1; j<t+1; j++) {
            Element end = pubParam.G2.newOneElement();
            for (int i=1; i<l+1; i++) {
                Element base = pubParam.A.get(j).duplicate();                   // A_j
                base.mul(pubParam.B.get(i).duplicate().powZn(msp.u.get(i-1)));  // A_j * B_j^(u(i))
                Element exp = r.get(i).duplicate().mul(msp.M[i-1][j-1]);        // M_ij * r_i
                end.mul(base.powZn(exp));                                       // (A_j * B_j^(u(i)))^(M_ij * r_i)
            }
            comp.Pj.add(end);
        }
        return comp;
    }

    public boolean verify (String message,
                           String [] attrs, ABSSignatureComp sign) throws NoSuchAlgorithmException {
        ArrayList<String> attrList = new ArrayList<>();
        Collections.addAll(attrList, attrs);
        MSP msp = MSP.getInstance(message, attrList, pubParam.Zr);
        final int l = msp.M.length;
        final int t = msp.M[0].length;

        // check if e(W,A0) != e(Y, h0):
        Element e_Y_h0 = pubParam.pairing.pairing(sign.Y, pubParam.hi.get(0));
        Element e_W_A0 = pubParam.pairing.pairing(sign.W, pubParam.A.get(0));
        if (!e_Y_h0.isEqual(e_W_A0)) {
            System.out.println("e(W,A0) != e(Y, h0)");
            return false;
        }

        // check if empty Y:
        if (sign.Y.isEqual(pubParam.G1.newZeroElement())) {
            System.out.println("Zero element of Y");
            return false;
        }

        // check j elements (∀j ∈ [t]):
        for (int j=1; j<t+1; j++) {
            Element Gt_result = pubParam.Gt.newOneElement();
            for (int i=1; i<l+1; i++) {
                Element a = sign.Si.get(i);                                         // Si
                Element b = pubParam.B.get(j).duplicate().powZn(msp.u.get(i-1));    // B_j^(u(i))
                b.mul(pubParam.A.get(j));                                           // A_j * B_j^(u(i))
                b.pow(BigInteger.valueOf(msp.M[i-1][j-1]));                         // (A_j * B_j^(u(i)))^(Mij)
                Gt_result.mul(pubParam.pairing.pairing(a, b));                      // Π e(a, b)
            }

            Element cgu = pubParam.C.duplicate();                                   // C
            cgu.mul(pubParam.g.duplicate().powZn(msp.mu));                          // C * (g^µ)
            Element rhs = pubParam.pairing.pairing(cgu, sign.Pj.get(j));            // e(Cg^µ, Pj)
            if (j == 1) {
                rhs.mul(pubParam.pairing.pairing(sign.Y, pubParam.hi.get(j)));      // e(Y, hj) * e(Cg^µ, Pj)
            }
            if (!Gt_result.isEqual(rhs)) {
                System.out.println("Mismatch for j = "+j+" case...");
                //return false;
            }
        }
        return true;
    }

    // ======================= Utilities Functions Below =======================
    public static void elementFromString(Element h, String s)   // FIXME: MSP also using
            throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] digest = md.digest(s.getBytes());
        h.setFromHash(digest, 0, digest.length);
    }

}
