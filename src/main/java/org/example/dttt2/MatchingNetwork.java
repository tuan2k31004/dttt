package org.example.dttt2;


import lombok.*;

import java.util.List;
import java.util.Map;

import static org.example.dttt2.DC.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MatchingNetwork {
    private double F;
    private double Zin;
    private double RL;
    private DC dc;
    private double Q;
    private Type type;
    private double w;



    public List<Map<?,?>> calculate() {
        w = 2 * Math.PI * F;
        return switch (type) {
            case L -> dc == FEED ? List.of(LF1(), LF2()) : List.of(LB1(), LB2());
            case T -> dc == FEED ? List.of(TF()) : List.of(TB());
            case PI -> dc == FEED ? List.of(PIF()) : List.of(PIB());
        };
    }

    //    HI_LOW
    private Map<?, ?> LF1() {
        double QL = Math.sqrt((Zin / RL) - 1);
        double L = (RL * QL) / w;
        double Lp = (QL * QL + 1) * L / QL;
        double C = 1 / (Lp * w * w);
        return Map.of("img", "images/LF1.png", "L", L, "C", C, "Q", QL);
    }
    //    LOW_HI
    private Map<?, ?> LF2() {
        double QL = Math.sqrt((RL / Zin) - 1);
        double C = QL / (RL * w);
        double Cs = C * (QL * QL + 1) / (QL * QL);
        double L = 1 / (Cs * w * w);
        return Map.of("img", "images/LF2.png", "L", L, "C", C, "Q", QL);
    }
    //    HI_LOW
    private Map<?, ?> LB1() {
        double QL = Math.sqrt((Zin / RL) - 1);
        double C = 1 / (QL * RL * w);
        double Cp = C * QL * QL / (QL * QL + 1);
        double L = 1 / (Cp * w * w);
        return Map.of("img", "images/LB1.png", "L", L, "C", C, "Q", QL);
    }
    //LOW_HI
    private Map<?, ?> LB2() {
        double QL = Math.sqrt((RL / Zin) - 1);
        double L = RL / (w * QL);
        double Ls = L * ((QL * QL) / (QL * QL + 1));
        double C = 1 / (Ls * w * w);
        return Map.of("img", "images/LB2.png", "L", L, "C", C, "Q", QL);
    }

    private Map<?, ?> TF() {
        double R_center = Math.min(Zin, RL) * (Q * Q + 1);
        if (Zin < RL) {
            double Cs = Q / (R_center * w);
            double Ls = Zin * Q / w;
            double Q2 = Math.sqrt(R_center / RL - 1);
            double Cl = Q2 / (R_center * w);
            double Ll = RL * Q2 / w;
            double C = Cs + Cl;
            return Map.of("img", "images/TF.png", "C", C, "LS", Ls, "LL", Ll);
        } else {
            double Cl = Q / (R_center * w);
            double Ll = RL * Q / w;
            double Q2 = Math.sqrt(R_center / Zin - 1);
            double Cs = Q2 / (R_center * w);
            double Ls = Zin * Q2 / w;
            double C = Cs + Cl;
            return Map.of("img", "images/TF.png", "C", C, "LS", Ls, "LL", Ll);
        }
    }

    private Map<?, ?> TB() {
        double R_center = Math.min(Zin, RL) * (Q * Q + 1);
        if (Zin < RL) {
            double Ls = R_center / (Q * w);
            double Cs = 1 / (w * Q * Zin);
            double Q2 = Math.sqrt(R_center / RL - 1);
            double Ll = R_center / (Q2 * w);
            double Cl = 1 / (w * Q2 * RL);
            double L = 1 / (1 / Ls + 1 / Ll);
            return Map.of("img", "images/TB.png", "L", L, "CS", Cs, "CL", Cl);
        } else {
            double Ll = R_center / (Q * w);
            double Cl = 1 / (w * Q * RL);
            double Q2 = Math.sqrt(R_center / Zin - 1);
            double Ls = R_center / (Q2 * w);
            double Cs = 1 / (w * Q2 * Zin);
            double L = 1 / (1 / Ls + 1 / Ll);
            return Map.of("img", "images/TB.png", "L", L, "CS", Cs, "CL", Cl);
        }
    }

    private Map<?, ?> PIF() {
        double R_center = Math.max(Zin, RL) / (Q * Q + 1);
        if (Zin > RL) {
            double Ls = R_center * Q / w;
            double Cs = Q / (w * Zin);
            double Q2 = Math.sqrt(RL / R_center - 1);
            double Ll = R_center * Q2 / w;
            double Cl = Q2 / (RL * w);
            double L = Ll + Ls;
            return Map.of("img", "images/PIF.png", "L", L, "CS", Cs, "CL", Cl);
        } else {
            double Ll = R_center * Q / w;
            double Cl = Q / (w * RL);
            double Q2 = Math.sqrt(Zin / R_center - 1);
            double Ls = R_center * Q2 / w;
            double Cs = Q2 / (Zin * w);
            double L = Ll + Ls;
            return Map.of("img", "images/PIF.png", "L", L, "CS", Cs, "CL", Cl);
        }
    }

    private Map<?, ?> PIB() {
        double R_center = Math.max(Zin, RL) / (Q * Q + 1);
        if (Zin > RL) {
            double Cs = 1 / (w * Q * R_center);
            double Ls = Zin / (w * Q);
            double Q2 = Math.sqrt(RL / R_center - 1);
            double Cl = 1 / (w * Q2 * R_center);
            double Ll = RL / (w * Q2);
            double C = 1 / (1 / Cl + 1 / Cs);
            return Map.of("img", "images/PIB.png", "C", C, "LS", Ls, "LL", Ll);
        } else {
            double Cl = 1 / (w * Q * R_center);
            double Ll = RL / (w * Q);
            double Q2 = Math.sqrt(Zin / R_center - 1);
            double Cs = 1 / (w * Q2 * R_center);
            double Ls = Zin / (w * Q2);
            double C = 1 / (1 / Cl + 1 / Cs);
            return Map.of("img", "images/PIB.png", "C", C, "LS", Ls, "LL", Ll);
        }
    }
}
