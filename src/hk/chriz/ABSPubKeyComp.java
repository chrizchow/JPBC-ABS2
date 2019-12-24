package hk.chriz;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;

import java.util.ArrayList;

public class ABSPubKeyComp {
    public Pairing pairing;
    public Field G1, G2, Gt, Zr;        // the field from pairing object
    public Element g;                   // random generator

    public final int tmax = 10;         // max width of MSP width t
    public ArrayList<Element> hi;       // h0 to h_tmax in G2 (totally h_tmax+1 items)
    public ArrayList<Element> A, B;     // A0 to A_tmax, B1 to B_tmax
    public Element C;

}
