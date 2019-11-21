package com.petroleumsoft.stimopti.algos;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Jai Mata Di
 */
public class WithDiverter22 {
//penetration 

    static Map<String, Double> keffective = new TreeMap<String, Double>();

    public static void Diverter(Map<String, String> inputfilemap, Map<String, String> kmap, int inj, Map<String, String> Penetration, String dtype, Map<String, String> QLT) throws FileNotFoundException, IOException {
        String divertertype = dtype;
        if (divertertype.equalsIgnoreCase("VISCOSIFIED")) {
            Viscosified(inputfilemap, kmap, inj);

        }
        if (divertertype.equalsIgnoreCase("VES")) {
            VES(inputfilemap, keffective, Penetration, inj, kmap);
        }
        if (divertertype.equalsIgnoreCase("PARTICULATE")) {
           ParticulateDiverter(inputfilemap, keffective, kmap, inj, QLT);
        }

    }

    public static void Viscosified(Map<String, String> inputfilemap, Map<String, String> kmap, int inj) throws FileNotFoundException, IOException {

        double re = Double.parseDouble(inputfilemap.get("DRAINAGE RADIUS (FT)"));
        double rw = Double.parseDouble(inputfilemap.get("WELLBORE RADIUS (FT)"));

        Map<String, Double> rwhlayer = new TreeMap <String, Double>();
        Map<String, Double> khder = new TreeMap <String, Double>();
        Map<String, Double> prek = new TreeMap <String, Double>();
        int layers = Integer.parseInt(inputfilemap.get("Layers"));
        double preavg1 = 0.0;
        for (int p1 = 0; p1 < layers; p1++) {
            preavg1 += Double.parseDouble(inputfilemap.get("Pre-Skin" + p1));
        }
        double pre_stim = preavg1 / layers;
        double rvis1 = 0;
        double permMD = 0.0;

        double kh = 0.0;
        double kh_der = 0;
        double skin = 0;
        double keff = 0;
        double totalpumprate = 0;
        double q = 0.0;
        double tao = 0.0;
        double rvis = 0.0;
        double k1h1 = 0.0;
        double sumkh = 0.0;
//****************shuru ho gya******************//
        double viscosity = Double.parseDouble(inputfilemap.get("VISCOSIFIED VISCOSITY"));
        double durations = Double.parseDouble(inputfilemap.get("Duration" + inj));
        double diffti_ = (inj == 0) ? Double.parseDouble(inputfilemap.get("Duration" + inj)) : Double.parseDouble(inputfilemap.get("Duration" + (inj - 1)));
        int diffti = (int) diffti_;
        totalpumprate = Double.parseDouble(inputfilemap.get("Pumprate" + inj));
        double sumkhder = 0.0;
        for (int ti = 0; ti < durations; ti++) {

            /* calculation of sumkhder */
            for (int k = 0; k <= (layers - 1); k++) {
                double h = Double.parseDouble(inputfilemap.get("ToDepth" + k)) - Double.parseDouble(inputfilemap.get("FromDepth" + k));
                double kperm = (inj == 0) ? Double.parseDouble(inputfilemap.get("Perm" + k)) : Double.parseDouble(kmap.get("permu" + "_" + k + "_" + (inj - 1) + "_" + (diffti - 1)));
                kh = kperm * h;
                sumkh += kh;
            }
            /* calculation of Qi vitip vwh rwh */
            if (ti > 0) {
                for (int k = 0; k <= (layers - 1); k++) {
                    kh_der = khder.get("khder_" + "_" + k + "_" + (ti - 1));
                    if (kh_der > 0) {
                        k1h1 = prek.get("k1h1" + "_" + k + "_" + (ti - 1));
                        rvis = rwhlayer.get("rwh" + "_" + k + "_" + (ti - 1));
                        kh_der = (k1h1 * Math.log((20 * Math.pow(rvis, 14)) / (Math.pow(rw, 15))));
                        khder.put("khder_" + "_" + k + "_" + ti, kh_der);
                        sumkhder += kh_der;
                    } else {
                        khder.put("khder_" + "_" + k + "_" + ti, 0.0);
                    }
                }
            }
            for (int k = 0; k <= (layers - 1); k++) {
                double Poro = Double.parseDouble(inputfilemap.get("Poro" + k));
                double h = Double.parseDouble(inputfilemap.get("ToDepth" + k)) - Double.parseDouble(inputfilemap.get("FromDepth" + k));
                if (ti == 0) {
                    if (inj != 0) {
                        permMD = Double.parseDouble(kmap.get("permu" + "_" + k + "_" + (inj - 1) + "_" + (diffti - 1)));
                    } else {
                        permMD = Double.parseDouble(inputfilemap.get("Perm" + k));
                    }
                    pre_stim = (inj == 0) ? pre_stim : Double.parseDouble(kmap.get("skin" + "_" + k + "_" + (inj - 1) + "_" + (diffti - 1)));
                    if (permMD > 0) {
                        kh = permMD * h;
                        tao = (kh / (sumkh - kh));
                        q = (tao * totalpumprate) / (1 + tao);
                        rvis = Math.sqrt((q / (3.14 * Poro * h)) + Math.pow(rw, 2));
                        keff = Math.log(re / rw) / ((Math.log(rvis / rw) / (Double.parseDouble(kmap.get("permu" + "_" + k + "_" + (inj - 1) + "_" + (diffti - 1))) / viscosity)) + (Math.log(re / rvis) / (Double.parseDouble(kmap.get("permu" + "_" + k + "_" + (inj - 1) + "_" + (diffti - 1))))));
                       // System.out.println(keff);
                        k1h1 = keff * h;
                        skin = ((permMD / keff) - 1) * Math.log(rvis / rw) + pre_stim;
                        kmap.put("skin" + "_" + k + "_" + inj + "_" + ti, Double.toString(skin));
                        khder.put("khder_" + "_" + k + "_" + ti, kh);
                        prek.put("k1h1" + "_" + k + "_" + ti, k1h1);
                        kmap.put("permu" + "_" + k + "_" + inj + "_" + ti, Double.toString(keff));
                        rwhlayer.put("rwh" + "_" + k + "_" + ti, rvis);
                    } else {
                        kmap.put("skin" + "_" + k + "_" + inj + "_" + ti, Double.toString(pre_stim));
                        khder.put("khder_" + "_" + k + "_" + ti, 0.0);
                        prek.put("k1h1" + "_" + k + "_" + ti, 0.0);
                        rwhlayer.put("rwh" + "_" + k + "_" + ti, rw);
                       // System.out.println(permMD);
                        kmap.put("permu" + "_" + k + "_" + inj + "_" + ti, Double.toString(permMD));
                    }

                } else {
                    pre_stim = (inj == 0) ? Double.parseDouble(kmap.get("skin" + "_" + k + "_" + (inj - 1) + "_" + (diffti - 1))) : Double.parseDouble(kmap.get("skin" + "_" + k + "_" + inj + "_" + (ti - 1)));
                    permMD = (ti == 0) ? Double.parseDouble(kmap.get("permu" + "_" + k + "_" + (inj - 1) + "_" + (diffti - 1))) : Double.parseDouble(kmap.get("permu" + "_" + k + "_" + inj + "_" + (ti - 1)));
                    if (permMD > 0) {
                        kh_der = khder.get("khder_" + "_" + k + "_" + (ti - 1));
                        tao = kh_der / (sumkhder - kh_der);
                        q = (tao * totalpumprate) / (1 + tao);
                        rvis = rwhlayer.get("rwh" + "_" + k + "_" + (ti - 1));
                        rvis1 = Math.sqrt((q / (3.14 * h * Poro)) + Math.pow(rvis, 2));
                        keff = Math.log(re / rw) / ((Math.log(rvis1 / rw) / (Double.parseDouble(kmap.get("permu" + "_" + k + "_" + (inj - 1) + "_" + (diffti - 1))) / viscosity)) + (Math.log(re / rvis1) / (Double.parseDouble(kmap.get("permu" + "_" + k + "_" + (inj - 1) + "_" + (diffti - 1))))));
                      //  System.out.println(keff);
                        k1h1 = keff * h;
                        kh = permMD * h;
                        skin = ((permMD / keff) - 1) * Math.log(rvis1 / rw) + pre_stim;
                        kmap.put("skin" + "_" + k + "_" + inj + "_" + ti, Double.toString(skin));
                        khder.put("khder_" + "_" + k + "_" + ti, kh);
                        prek.put("k1h1" + "_" + k + "_" + ti, k1h1);
                        kmap.put("permu" + "_" + k + "_" + inj + "_" + ti, Double.toString(keff));
                        rwhlayer.put("rwh" + "_" + k + "_" + ti, rvis1);
                    } else {
                        kmap.put("skin" + "_" + k + "_" + inj + "_" + ti, Double.toString(pre_stim));
                        khder.put("khder_" + "_" + k + "_" + ti, 0.0);
                        prek.put("k1h1" + "_" + k + "_" + ti, 0.0);
                        rwhlayer.put("rwh" + "_" + k + "_" + ti, rwhlayer.get("rwh" + "_" + k + "_" + (ti - 1)));
                       // System.out.println(permMD);
                        kmap.put("permu" + "_" + k + "_" + inj + "_" + ti, Double.toString(permMD));
                    }

                }

            }

        }

        
    }


    public static void Foam(Map<String, String> inputfilemap, Map<String, String> kmap, Map<String, String> Qtube, Map<String, String> Q1_Q2, Map<String, String> KHP, int Rlayers, int inj, int ti, double sumkhp, int Rlayers2, int part) throws FileNotFoundException, IOException {
        double re = Double.parseDouble(inputfilemap.get("DRAINAGE RADIUS (FT)"));
        double rw = Double.parseDouble(inputfilemap.get("WELLBORE RADIUS (FT)"));
        double foamQ = Double.parseDouble(inputfilemap.get("FOAM QUALITY (%)"));
        double Swc = Double.parseDouble(inputfilemap.get("CONNATE WATER SATURATION"));
        int layers = Integer.parseInt(inputfilemap.get("Layers"));
        double preavg1 = 0.0;
        for (int p1 = 0; p1 < layers; p1++) {
            preavg1 += Double.parseDouble(inputfilemap.get("pre-stim" + p1));
        }
        double pre_stim = preavg1 / layers;
        double r_div = 0;
        double permMD = 0.0;
        double pq = 0;
        double skin = 0;
        double skind = 0;
        double keff = 0;
        double q = 0.0;
        //****************shuru ho gya******************//
        double skinfoam = 0.0 ;
        double kfoam = 0.0;
        double viscosity = Double.parseDouble(inputfilemap.get("FOAMED VISCOSITY"));
        double bhp = Double.parseDouble(inputfilemap.get("BHPPSI" + inj));
        double diffti_ = (inj == 0) ? Double.parseDouble(inputfilemap.get("Duration" + inj)) : Double.parseDouble(inputfilemap.get("Duration" + (inj - 1)));
        int diffti = (int) diffti_;
        //    for (int ti = 0; ti < durations; ti++) {
        /* calculation of sumkhder */

        for (int k = 0; k <= (layers - 1); k++) {
            double h = Double.parseDouble(inputfilemap.get("ToDefthFT" + k)) - Double.parseDouble(inputfilemap.get("FromDefthFT" + k));
            if (inj == 0) {
                permMD = Double.parseDouble(inputfilemap.get("PermMD" + k));
            } else {
                permMD = Double.parseDouble(kmap.get("permu" + "_" + k + "_" + (inj - 1) + "_" + (diffti - 1)));
            }

            double phi = Double.parseDouble(inputfilemap.get("Poro" + k));
            double operm = Double.parseDouble(inputfilemap.get("PermMD" + k));
            double zonepress = Double.parseDouble(inputfilemap.get("ZonePressPSI" + k));
            if (permMD > 0) {
                if (ti == 0) {
                    if (Rlayers == 0) {
                        Rlayers = 1;
                    }
                    pre_stim = (inj == 0) ? pre_stim : Double.parseDouble(kmap.get("skin" + "_" + k + "_" + (inj - 1) + "_" + (diffti - 1)));
                    if (inputfilemap.get("INJECTION TYPE").equalsIgnoreCase("BULLHEAD")) {
                        double q3 = (Double.parseDouble(Qtube.get("Qt_" + (Rlayers - 1) + " " + inj + "_" + ti))) * (Double.parseDouble(KHP.get("KHP_" + k)) / sumkhp);
                        q = (Double.parseDouble(Q1_Q2.get("Q1_Q2" + k))) + q3;
                    } else {
                        double q3 = 0;
                        if (part == layers - 1) {
                            q3 = (Double.parseDouble(Qtube.get("Qt_" + (Rlayers - 1) + " " + inj + "_" + ti))) * (Double.parseDouble(KHP.get("KHP_" + k))) / sumkhp;

                        } else if (part == -1) {
                            q3 = (Double.parseDouble(Qtube.get("Qt_" + (Rlayers2 - 1) + " " + inj + "_" + ti))) * (Double.parseDouble(KHP.get("KHP_" + k))) / sumkhp;
                        } else {
                            q3 = (Double.parseDouble(Qtube.get("Qt_" + (Rlayers - 1) + " " + inj + "_" + ti)) + Double.parseDouble(Qtube.get("Qt_" + (Rlayers2 - 1) + " " + inj + "_" + ti))) * (Double.parseDouble(KHP.get("KHP_" + k))) / sumkhp;
                        }
                        q = (Double.parseDouble(Q1_Q2.get("Q1_Q2" + k))) + q3;

                    }

                    if (q >= 0.1) {
                        r_div = Math.sqrt(((((q * 0.1589) / 60) * foamQ * 60) / (3.14 * (h * 0.3048) * phi * (1 - Swc))) + Math.pow(rw, 2));
                        Qtube.put("rdiv" + "_" + ti + "_" + k, Double.toString(r_div));
                        skinfoam = ((permMD * h * (bhp - zonepress)) / (141.2 * q * 1440 * viscosity)) - Math.log(re / rw);
                        kfoam = permMD / (1 + (-skinfoam) / Math.log(r_div / rw));
                        keff = Math.log(re / rw) / ((Math.log(re / r_div) / operm) + (Math.log(r_div / rw) / kfoam));
                        skin = ((permMD / keff) - 1) * Math.log(r_div / rw);
                        skind = skin + pre_stim;
                        kmap.put("skinw" + "_" + k + "_" + inj + "_" + ti, Double.toString(skin));
                        kmap.put("skin" + "_" + k + "_" + inj + "_" + ti, Double.toString(skind));
                        kmap.put("permu" + "_" + k + "_" + inj + "_" + ti, Double.toString(keff));
                    } else {
                        keff = (inj == 0) ? ((ti == 0) ? Double.parseDouble(inputfilemap.get("PermMD" + k)) : Double.parseDouble(kmap.get("permu" + "_" + k + "_" + inj + "_" + (ti - 1)))) : ((ti == 0) ? Double.parseDouble(kmap.get("permu" + "_" + k + "_" + (inj - 1) + "_" + (diffti - 1))) : Double.parseDouble(kmap.get("permu" + "_" + k + "_" + inj + "_" + (ti - 1))));
                        Qtube.put("rdiv" + "_" + ti + "_" + k, Double.toString(rw));
                        kmap.put("permu" + "_" + k + "_" + inj + "_" + ti, Double.toString(keff));
                        kmap.put("skin" + "_" + k + "_" + inj + "_" + ti, Double.toString(pre_stim));
                        kmap.put("skinw" + "_" + k + "_" + inj + "_" + ti, Double.toString(0));
                    }
                    Q1_Q2.put("q" + "_" + ti + "_" + k, Double.toString(q));
                } else {
                    pre_stim = (inj == 0) ? Double.parseDouble(kmap.get("skin" + "_" + k + "_" + (inj - 1) + "_" + (diffti - 1))) : Double.parseDouble(kmap.get("skin" + "_" + k + "_" + inj + "_" + (ti - 1)));
                    if (inputfilemap.get("INJECTION TYPE").equalsIgnoreCase("BULLHEAD")) {
                        double q3 = (Double.parseDouble(Qtube.get("Qt_" + (Rlayers - 1) + " " + inj + "_" + ti))) * (Double.parseDouble(KHP.get("KHP_" + k)) / sumkhp);
                        q = (Double.parseDouble(Q1_Q2.get("Q1_Q2" + k))) + q3;
                    } else {
                        double q3 = 0;
                        if (part == layers - 1) {
                            q3 = (Double.parseDouble(Qtube.get("Qt_" + (Rlayers - 1) + " " + inj + "_" + ti))) * (Double.parseDouble(KHP.get("KHP_" + k))) / sumkhp;

                        } else if (part == -1) {
                            q3 = (Double.parseDouble(Qtube.get("Qt_" + (Rlayers2 - 1) + " " + inj + "_" + ti))) * (Double.parseDouble(KHP.get("KHP_" + k))) / sumkhp;
                        } else {
                            q3 = (Double.parseDouble(Qtube.get("Qt_" + (Rlayers - 1) + " " + inj + "_" + ti)) + Double.parseDouble(Qtube.get("Qt_" + (Rlayers2 - 1) + " " + inj + "_" + ti))) * (Double.parseDouble(KHP.get("KHP_" + k))) / sumkhp;
                        }
                        q = (Double.parseDouble(Q1_Q2.get("Q1_Q2" + k))) + q3;

                    }
                    if (q >= 0.1) {
                        pq = Double.parseDouble(Q1_Q2.get("q" + "_" + (ti - 1) + "_" + k));
                        if (pq > 0) {
                            r_div = Math.sqrt((((((q + pq) * 0.1589) / 60) * foamQ * 60) / (3.14 * (h * 0.3048) * phi * (1 - Swc))));
                        } else {
                            r_div = Math.sqrt(((((q * 0.1589) / 60) * foamQ * 60) / (3.14 * (h * 0.3048) * phi * (1 - Swc))) + Math.pow(rw, 2));
                        }
                        Qtube.put("rdiv" + "_" + ti + "_" + k, Double.toString(r_div));
                        skinfoam = ((permMD * h * (bhp - zonepress)) / (141.2 * (q + Double.parseDouble(Q1_Q2.get("q" + "_" + (ti - 1) + "_" + k))) * 1440 * viscosity)) - Math.log(re / rw);
                        kfoam = permMD / (1 + (-skinfoam) / Math.log(r_div / rw));
                        keff = Math.log(re / rw) / ((Math.log(re / r_div) / operm) + (Math.log(r_div / rw) / kfoam));
                        skin = ((permMD / keff) - 1) * Math.log(r_div / rw);
                        skind = skin + pre_stim;
                        kmap.put("skinw" + "_" + k + "_" + inj + "_" + ti, Double.toString(skin));
                        kmap.put("skin" + "_" + k + "_" + inj + "_" + ti, Double.toString(skind));
                    } else {
                        keff = (inj == 0) ? ((ti == 0) ? Double.parseDouble(inputfilemap.get("PermMD" + k)) : Double.parseDouble(kmap.get("permu" + "_" + k + "_" + inj + "_" + (ti - 1)))) : ((ti == 0) ? Double.parseDouble(kmap.get("permu" + "_" + k + "_" + (inj - 1) + "_" + (diffti - 1))) : Double.parseDouble(kmap.get("permu" + "_" + k + "_" + inj + "_" + (ti - 1))));
                        Qtube.put("rdiv" + "_" + ti + "_" + k, Qtube.get("rdiv" + "_" + (ti - 1) + "_" + k));
                        kmap.put("skin" + "_" + k + "_" + inj + "_" + ti, Double.toString(pre_stim));
                        kmap.put("skinw" + "_" + k + "_" + inj + "_" + ti, Double.toString(0));
                    }
                    Q1_Q2.put("q" + "_" + ti + "_" + k, Double.toString(q));
                }
                kmap.put("permu" + "_" + k + "_" + inj + "_" + ti, Double.toString(keff));
            } else {
                double ks = 0.0;
                ks = (inj == 0) ? ((ti == 0) ? Double.parseDouble(inputfilemap.get("PermMD" + k)) : Double.parseDouble(kmap.get("permu" + "_" + k + "_" + inj + "_" + (ti - 1)))) : ((ti == 0) ? Double.parseDouble(kmap.get("permu" + "_" + k + "_" + (inj - 1) + "_" + (diffti - 1))) : Double.parseDouble(kmap.get("permu" + "_" + k + "_" + inj + "_" + (ti - 1))));
                kmap.put("permu" + "_" + k + "_" + inj + "_" + ti, Double.toString(ks));
                if (inj == 0) {
                    Qtube.put("rdiv" + "_" + ti + "_" + k, Double.toString(rw));
                } else {
                    Qtube.put("rdiv" + "_" + ti + "_" + k, Qtube.get("rdiv" + "_" + (ti - 1) + "_" + k));
                }
                Q1_Q2.put("q" + "_" + ti + "_" + k, Double.toString(0));
            }
        }

        //  }
        
    }

    public static void ParticulateDiverter(Map<String, String> inputfilemap, Map<String, Double> kefffective, Map<String, String> kmap, int inj, Map<String, String> QLT) throws FileNotFoundException, IOException {
        int layers = Integer.parseInt(inputfilemap.get("NoofLayers"));
       
        double re = Double.parseDouble(inputfilemap.get("DRAINAGE RADIUS (FT)"));
        double rw = Double.parseDouble(inputfilemap.get("WELLBORE RADIUS (FT)"));
        String completiontype = inputfilemap.get("COMPLETION TYPE");
        double durations = Double.parseDouble(inputfilemap.get("Duration" + inj));
        double divervisco = Double.parseDouble(inputfilemap.get("PARTICULATE VISCOSITY"));
        double diffti_ = (inj == 0) ? Double.parseDouble(inputfilemap.get("Duration" + inj)) : Double.parseDouble(inputfilemap.get("Duration" + (inj - 1)));
        int diffti = (int) diffti_;
        double h = 0.0;
        double permMD = 0.0;
        double sumkh = 0;
        double kh = 0;
        double keff = 0;
        double q = 0.0;
        double delpcake = 0.0;
        double skincake = 0.0;
        Map<String, Double> cthik = new TreeMap <String, Double>();
        Map<String, Double> tempkeffective = new TreeMap <String, Double>();
        double cakeporosity = Double.parseDouble(inputfilemap.get("CAKE POROSITY"));
        double gs = Double.parseDouble(inputfilemap.get("PARTICULATE GRAIN SIZE (MICRO-M)"));
        double cakepermMD = 0.0;
        double gv = 1.333 * 3.14 * Math.pow((gs * 0.000001), 3);
        double sg = 4 * 3.14 * Math.pow((gs * 0.000001), 2);
        double kcake = ((6.67E-3 * Math.pow(cakeporosity, 3)) / Math.pow(1 - cakeporosity, 2)) * Math.pow((gv / sg), 2);
        cakepermMD = kcake * 1013250273830880.0;
        for (int k1 = 0; k1 <= layers - 1; k1++) {
            h = Double.parseDouble(inputfilemap.get("ToDefthFT" + k1)) - Double.parseDouble(inputfilemap.get("FromDefthFT" + k1));
            permMD = Double.parseDouble(inputfilemap.get("PermMD" + k1));
            kh = permMD * h;
            sumkh = sumkh + kh;
            cthik.put("cthik" + k1, 0.0);
        }

        if (completiontype.equals("OPEN HOLE") || completiontype.equals("SLOTTED LINER")) {
            double cthickness = 0.0;
            double rcake = 0.0;
            double diverConc = Double.parseDouble(inputfilemap.get("DIVERTING AGENT CONCENTRATION"));
            for (int ti = 0; ti <= durations; ti++) {
                /* calculation of sumkhder */

                for (int k = 0; k <= (layers - 1); k++) {

                    if (inj == 0) {
                        permMD = (ti == 0) ? Double.parseDouble(inputfilemap.get("PermMD" + k)) : Double.parseDouble(kmap.get("permu" + "_" + k + "_" + (inj - 1) + "_" + (diffti - 1)));
                    } else {
                        permMD = (ti == 0) ? Double.parseDouble(kmap.get("permu" + "_" + k + "_" + (inj - 1) + "_" + (diffti - 1))) : tempkeffective.get("keff" + "_" + (ti - 1) + " " + k);
                    }
                    q = Double.parseDouble(QLT.get("QLTW " + k + " " + (inj - 1) + "_" + (diffti - 1)));

                    if (q > 0) {
                        cthickness = (ti == 0) ? (diverConc * q) / ((1 - cakeporosity) * 2 * 3.1416 * rw * h) : ((diverConc * q) / ((1 - cakeporosity) * 2 * 3.1416 * rcake * h));
                        delpcake = (ti == 0) ? (divervisco * (q / (2 * 3.1416 * rw * h)) * cthickness) / (cakepermMD * 32.2) : (divervisco * (q / (2 * 3.1416 * rcake * h)) * cthickness) / (cakepermMD * 32.2);
                        skincake = (2 * 3.14 * permMD * h * delpcake) / (q * divervisco);                        
                        kmap.put("skin" + "_" + k + "_" + inj + "_" + ti, Double.toString(skincake));
                        rcake = (ti == 0) ? rw - cthickness : rw - cthickness;
                        keff = Math.log(re / rcake) / ((Math.log(rw / rcake) / (cakepermMD)) + (Math.log(re / rw)) / permMD);
                        kmap.put("permu" + "_" + k + "_" + inj + "_" + ti, Double.toString(keff));
                        tempkeffective.put("keff" + "_" + ti + " " + k, keff);
                        cthik.put("cthik" + k, cthickness);
                    } else {
                        if (inj == 0) {
                            permMD = (ti == 0) ? Double.parseDouble(inputfilemap.get("PermMD" + k)) : Double.parseDouble(kmap.get("permu" + "_" + k + "_" + (inj - 1) + "_" + (diffti - 1)));
                        } else {
                            permMD = (ti == 0) ? Double.parseDouble(kmap.get("permu" + "_" + k + "_" + (inj - 1) + "_" + (diffti - 1))) : tempkeffective.get("keff" + "_" + (ti - 1) + " " + k);
                        }
                        kmap.put("permu" + "_" + k + "_" + inj + "_" + ti, Double.toString(permMD));
                        kmap.put("skin" + "_" + k + "_" + inj + "_" + ti, Double.toString(0));
                        tempkeffective.put("keff" + "_" + ti + " " + k, permMD);

                    }
                }
            }
        } else if (completiontype.equals("CASED HOLE") || completiontype.equals("LIMITED ENTRY") || completiontype.equals("SLIDING SLEEVE")) {
            double cthickness = 0.0;
            double rcake = 0.0;
            double diverConc = Double.parseDouble(inputfilemap.get("DIVERTING AGENT CONCENTRATION"));
            for (int ti = 0; ti <= durations; ti++) {
                /* calculation of sumkhder */

                for (int k = 0; k <= (layers - 1); k++) {
                    if (ti == 0) {
                        permMD = Double.parseDouble(kmap.get("permu" + "_" + k + "_" + (inj - 1) + "_" + (diffti - 1)));

                    } else {
                        permMD = tempkeffective.get("keff" + "_" + (ti - 1) + " " + k);
                    }
                    q = Double.parseDouble(QLT.get("QLTW " + k + " " + (inj - 1) + "_" + (diffti - 1)));

                    if (q > 0) {

                        cthickness = (ti == 0) ? (diverConc * q) / ((1 - cakeporosity) * 2 * 3.1416 * rw * h) : ((diverConc * q) / ((1 - cakeporosity) * 2 * 3.1416 * rcake * h));
                        delpcake = (ti == 0) ? (divervisco * (q / (2 * 3.1416 * rw * h)) * cthickness) / (cakepermMD * 32.2) : (divervisco * (q / (2 * 3.1416 * rcake * h)) * cthickness) / (cakepermMD * 32.2);
                        skincake = (2 * 3.14 * permMD * h * delpcake) / (q * divervisco);
                        kmap.put("skin" + "_" + k + "_" + inj + "_" + ti, Double.toString(skincake));
                        rcake = (ti == 0) ? rw - cthickness : rw - cthickness;
                        keff = Math.log(re / rcake) / ((Math.log(rw / rcake) / (cakepermMD)) + (Math.log(re / rw)) / permMD);
                        kmap.put("permu" + "_" + k + "_" + inj + "_" + ti, Double.toString(keff));
                        tempkeffective.put("keff" + "_" + ti + " " + k, keff);
                        cthik.put("cthik" + k, cthickness);
                    } else {
                        if (inj == 0) {
                            permMD = (ti == 0) ? Double.parseDouble(inputfilemap.get("PermMD" + k)) : Double.parseDouble(kmap.get("permu" + "_" + k + "_" + (inj - 1) + "_" + (diffti - 1)));
                        } else {
                            permMD = (ti == 0) ? Double.parseDouble(kmap.get("permu" + "_" + k + "_" + (inj - 1) + "_" + (diffti - 1))) : tempkeffective.get("keff" + "_" + (ti - 1) + " " + k);
                        }
                        kmap.put("permu" + "_" + k + "_" + inj + "_" + ti, Double.toString(permMD));
                        kmap.put("skin" + "_" + k + "_" + inj + "_" + ti, Double.toString(0));
                        tempkeffective.put("keff" + "_" + ti + " " + k, permMD);

                    }

                }
            }
        }

    }

    public static void VES(Map<String, String> inputfilemap, Map<String, Double> kefffective, Map<String, String> Penetration, int inj, Map<String, String> kmap) throws FileNotFoundException, IOException {

        double re = Double.parseDouble(inputfilemap.get("DRAINAGE RADIUS (FT)"));
        double rw = Double.parseDouble(inputfilemap.get("WELLBORE RADIUS (FT)"));
        double diffti_ = (inj == 0) ? Double.parseDouble(inputfilemap.get("Duration" + inj)) : Double.parseDouble(inputfilemap.get("Duration" + (inj - 1)));
        int diffti = (int) diffti_;
        double Ca_concentration = 0;
        double RockDensity = Double.parseDouble(inputfilemap.get("ROCK DENSITY (G/CM3)"));
        double MaxViscosity = Double.parseDouble(inputfilemap.get("MAXIMUM VISCOSITY (cP)"));
        double sdva = Double.parseDouble(inputfilemap.get("SDVA CONCENTRATION"));
        double vph = Double.parseDouble(inputfilemap.get("VES PH"));
        double Ca_max = Double.parseDouble(inputfilemap.get("CALCIUM CONCENTRATION AT MAX VISCOSITY (%)"));
        double CalCarbonateConcentration = Double.parseDouble(inputfilemap.get("CALCITE CONCENTRATION (%)"));
        double W_two = 1.1;
        Map<String, Double> rwhlayer = new TreeMap <String, Double> ();
        Map<String, Double> khder = new TreeMap <String, Double> ();
        Map<String, Double> q_k_ti = new TreeMap <String, Double> ();
        Map<String, Double> deffective = new TreeMap <String, Double> ();
        List<String> camax = new ArrayList <String> ();
        int layers = Integer.parseInt(inputfilemap.get("NoofLayers"));
        double preavg1 = 0.0;
        for (int p1 = 0; p1 < layers; p1++) {
            preavg1 += Double.parseDouble(inputfilemap.get("pre-stim" + p1));
        }
        double pre_stim = preavg1 / layers;
        double r_div = 0;
        double permMD = 0.0;
        double sumkh = 0;
        double kh = 0;
        double kh_der = 0;
        double permMDT = 0;
        double keff = 0;
        double totalpumprate = 0;
        double q = 0.0;

        int chkstage = 0;
        //********check for previous stage if ACID**********//
        for (int i = 0; i < inj; i++) {
            String checkstage = inputfilemap.get("Stage" + i);
            if (checkstage.equalsIgnoreCase("ACID")) {
                chkstage++;
            }
        }
        //****************shuru ho gya******************//
        double sumkhder = 0.0;
        double viscosity = 0.0;
        double oviscosity = Double.parseDouble(inputfilemap.get("VES VISCOSITY"));
        double viscosityVES = Double.parseDouble(inputfilemap.get("VES VISCOSITY"));
        double durations = Double.parseDouble(inputfilemap.get("Duration" + inj));
        totalpumprate = Double.parseDouble(inputfilemap.get("PumpRateBMP" + inj));
        //double n = Double.parseDouble(inputfilemap.get("N (FOR DIVERTER)"));
        double Moles_mineral = (CalCarbonateConcentration * RockDensity * 10) / 100.1;//Rock density in g/cc
        double operm = 0.0;
        int mib = 0;
        for (int ti = 0; ti < durations; ti++) {
            /* calculation of sumkhder */
            for (int k = 0; k <= (layers - 1); k++) {
                double h = Double.parseDouble(inputfilemap.get("ToDefthFT" + k)) - Double.parseDouble(inputfilemap.get("FromDefthFT" + k));
                double Poro = Double.parseDouble(inputfilemap.get("Poro" + k));

                double Diverter_volume_litres = 0;
                if (inj == 0) {
                    permMD = (ti == 0) ? Double.parseDouble(inputfilemap.get("PermMD" + k)) : Double.parseDouble(kmap.get("permu" + "_" + k + "_" + (inj - 1) + "_" + (diffti - 1)));
                    if (mib == 0) {
                        operm = permMD;
                    }

                } else {
                    permMD = (ti == 0) ? Double.parseDouble(kmap.get("permu" + "_" + k + "_" + (inj - 1) + "_" + (diffti - 1))) : Double.parseDouble(kmap.get("permu" + "_" + k + "_" + inj + "_" + (ti - 1)));
                    if (mib == 0) {
                        operm = permMD;
                    }

                }
                if (ti == 0) {

                    if (permMD > 0) {
                        kh = permMD * h;
                        sumkh = sumkh + kh;
                        khder.put("khder_" + k + ti, kh);

                    } else {
                        khder.put("khder_" + k + ti, 0.0);
                    }
                    rwhlayer.put("rwh" + k + (ti - 1), rw);
                    kmap.put("permu" + "_" + k + "_" + inj + "_" + ti, Double.toString(permMD));

                } else {

                    if (inj == 0) {
                        permMD = (ti == 0) ? Double.parseDouble(inputfilemap.get("PermMD" + k)) : Double.parseDouble(kmap.get("permu" + "_" + k + "_" + (inj - 1) + "_" + (diffti - 1)));
                    } else {
                        permMD = (ti == 0) ? Double.parseDouble(kmap.get("permu" + "_" + k + "_" + (inj - 1) + "_" + (diffti - 1))) : Double.parseDouble(kmap.get("permu" + "_" + k + "_" + inj + "_" + (ti - 1)));
                    }
                    if (permMD > 0) {
                        double phi = (inj > 0) ? Double.parseDouble(kmap.get("porou" + "_" + k + "_" + (inj - 1) + "_" + (diffti - 1))) : Double.parseDouble(inputfilemap.get("Poro" + k));
                        //**Ves viscosity change**//

                        if (chkstage == 0) {
                            viscosity = viscosityVES + (MaxViscosity * Math.exp(-Math.pow((Ca_concentration - Ca_max) / W_two, 2)));
                            if (ti == (durations - 1)) {
                                camax.add(Double.toString(0));
                            }
                        } else {
                            for (int i = 0; i < ti; i++) {

                                Diverter_volume_litres += q_k_ti.get("q_k_ti" + k + "_" + (ti - 1));

                            }

                            Diverter_volume_litres = (Diverter_volume_litres * 159);

                            double test_rwh = Double.parseDouble(Penetration.get("Penet" + k + "_" + (inj - 1) + "_" + (diffti - 1)));
                            double MineralVolume_litres = 3.14 * (Math.pow(test_rwh, 2) - Math.pow(rw, 2)) * h * (1 - phi) * 28.31;

                            double VolumeXMole_Mineral = MineralVolume_litres * Moles_mineral;
                            Ca_concentration = VolumeXMole_Mineral / (Diverter_volume_litres + MineralVolume_litres);
                            camax.add(Double.toString(Ca_concentration));

                            double viscosity3 = Math.exp(-0.5 * Math.pow(((sdva - sdva) / 0.0173), 2));
                            double viscosity4 = Math.exp(-Math.pow((Ca_concentration - Ca_max) / 1.1, 2));
                            double viscosity5 = (ErrorFunction.erf(vph - 1) + 1) / 2.0;
                            viscosity = (oviscosity + (MaxViscosity * viscosity3 * viscosity4 * viscosity5));

                            //  viscosity = viscosityVES + (MaxViscosity * Math.exp(-Math.pow((Ca_concentration - Ca_max) / W_two, 2)));
                        }

                        double testqqqt = q_k_ti.get("q_k_ti" + k + "_" + (ti - 1));
                        r_div = Math.sqrt(((testqqqt * 5.614) / (3.14 * h * Poro)) + Math.pow(rw, 2));

                        if (ti == 1) {
                            if (inj == 0) {
                                permMDT = Double.parseDouble(inputfilemap.get("PermMD" + k)) / viscosity;
                            } else {
                                permMDT = Double.parseDouble(kmap.get("permu" + "_" + k + "_" + (inj - 1) + "_" + (diffti - 1))) / viscosity;
                            }

                            if (permMDT <= 0) {
                                permMDT = permMD = Double.parseDouble(inputfilemap.get("PermMD" + k));
                            }

                        } else {
                            permMDT = deffective.get("ti_" + (ti - 1) + "k_" + k) / viscosity;
                        }

                        keff = Math.log(re / rw) / ((Math.log(r_div / rw) / permMDT) + (Math.log(re / r_div) / permMD));
                        if (keff > operm) {
                            if (mib == 0) {
                                keff = Double.parseDouble(kmap.get("permu" + "_" + k + "_" + (inj - 1) + "_" + (diffti - 1)));
                            } else {
                                keff = Double.parseDouble(kmap.get("permu" + "_" + k + "_" + inj + "_" + (ti - 1)));
                            }
                        }
                        kmap.put("permu" + "_" + k + "_" + inj + "_" + ti, Double.toString(keff));
                        deffective.put("ti_" + ti + "k_" + k, keff);
                        kh = keff * h;
                        kh_der = kh * Math.log((re * Math.pow(r_div, (viscosity - 1))) / Math.pow(rw, viscosity));
                        sumkhder = sumkhder + kh_der;
                        khder.put("khder_" + k + ti, kh_der);
                        sumkh = sumkhder;
                    } else {
                        if (inj == 0) {
                            permMD = (ti == 0) ? Double.parseDouble(inputfilemap.get("PermMD" + k)) : Double.parseDouble(kmap.get("permu" + "_" + k + "_" + (inj - 1) + "_" + (diffti - 1)));
                        } else {
                            permMD = (ti == 0) ? Double.parseDouble(kmap.get("permu" + "_" + k + "_" + (inj - 1) + "_" + (diffti - 1))) : Double.parseDouble(kmap.get("permu" + "_" + k + "_" + inj + "_" + (ti - 1)));
                        }
                        kmap.put("permu" + "_" + k + "_" + inj + "_" + ti, Double.toString(permMD));
                        keff = 0.0;
                        khder.put("khder_" + k + ti, 0.0);
                    }

                }
                if (k == layers) {
                    mib++;
                }
            }

            /* calculation of Qi vitip vwh rwh */
            for (int k = 0; k <= (layers - 1); k++) {
                kh_der = khder.get("khder_" + k + ti);
                if (kh_der > 0) {
                    double tao = kh_der / (sumkh - kh_der);
                    q = totalpumprate * tao / (1 + tao);
                    q_k_ti.put("q_k_ti" + k + "_" + ti, q);
                    rwhlayer.put("rwh" + k + ti, r_div);
                } else {
                    q_k_ti.put("q_k_ti" + k + "_" + ti, 0.0);
                    rwhlayer.put("rwh" + k + ti, 0.0);
                }
            }
        }
        Ca_max = Double.parseDouble(Collections.max(camax));
        /*keffective calculation*/
        for (int ti = 0; ti < durations; ti++) {
            /* calculation of sumkhder */
            for (int k = 0; k <= (layers - 1); k++) {

                double h = Double.parseDouble(inputfilemap.get("ToDefthFT" + k)) - Double.parseDouble(inputfilemap.get("FromDefthFT" + k));
                double Poro = Double.parseDouble(inputfilemap.get("Poro" + k));

                double Diverter_volume_litres = 0;

                if (inj == 0) {
                    permMD = (ti == 0) ? Double.parseDouble(inputfilemap.get("PermMD" + k)) : Double.parseDouble(kmap.get("permu" + "_" + k + "_" + (inj - 1) + "_" + (diffti - 1)));
                    if (mib == 0) {
                        operm = permMD;
                    }
                } else {
                    permMD = (ti == 0) ? Double.parseDouble(kmap.get("permu" + "_" + k + "_" + (inj - 1) + "_" + (diffti - 1))) : Double.parseDouble(kmap.get("permu" + "_" + k + "_" + inj + "_" + (ti - 1)));
                    if (mib == 0) {
                        operm = permMD;
                    }
                }
                if (ti == 0) {
                    if (permMD > 0) {
                        kh = permMD * h;
                        sumkh = sumkh + kh;
                        khder.put("khder_" + k + ti, kh);
                    } else {
                        khder.put("khder_" + k + ti, 0.0);
                    }
                    rwhlayer.put("rwh" + k + (ti - 1), rw);
                    kmap.put("permu" + "_" + k + "_" + inj + "_" + ti, Double.toString(permMD));
                    double skin = 0.0;
                    skin = (inj == 0) ? pre_stim : Double.parseDouble(kmap.get("skin" + "_" + k + "_" + (inj - 1) + "_" + (diffti - 1)));
                    kmap.put("skin" + "_" + k + "_" + inj + "_" + ti, Double.toString(skin));
                } else {
                    if (keffective.size() == (layers)) {
                        permMD = keffective.get("k" + k);
                    } else {
                        permMD = (inj > 0) ? Double.parseDouble(kmap.get("permu" + "_" + k + "_" + (inj - 1) + "_" + (diffti - 1))) : Double.parseDouble(inputfilemap.get("PermMD" + k));
                    }
                    if (permMD > 0) {
                        double phi = (inj > 0) ? Double.parseDouble(kmap.get("porou" + "_" + k + "_" + (inj - 1) + "_" + (diffti - 1))) : Double.parseDouble(inputfilemap.get("Poro" + k));
                        //**Ves viscosity change**//

                        if (chkstage == 0) {
                            viscosity = viscosityVES + (MaxViscosity * Math.exp(-Math.pow((Ca_concentration - Ca_max) / W_two, 2)));
                        } else {
                            for (int i = 0; i < ti; i++) {

                                Diverter_volume_litres += q_k_ti.get("q_k_ti" + k + "_" + (ti - 1));

                            }

                            Diverter_volume_litres = (Diverter_volume_litres * 159);

                            double test_rwh = Double.parseDouble(Penetration.get("Penet" + k + "_" + (inj - 1) + "_" + (diffti - 1)));
                            double MineralVolume_litres = 3.14 * (Math.pow(test_rwh, 2) - Math.pow(rw, 2)) * h * (1 - phi) * 28.31;

                            double VolumeXMole_Mineral = MineralVolume_litres * Moles_mineral;
                            Ca_concentration = VolumeXMole_Mineral / (Diverter_volume_litres + MineralVolume_litres);

                            double viscosity3 = Math.exp(-0.5 * Math.pow(((sdva - sdva) / 0.0173), 2));
                            double viscosity4 = Math.exp(-Math.pow((Ca_concentration - Ca_max) / 1.1, 2));
                            double viscosity5 = (ErrorFunction.erf(vph - 1) + 1) / 2.0;
                            viscosity = (oviscosity + (MaxViscosity * viscosity3 * viscosity4 * viscosity5));
                            //viscosity = viscosityVES + (MaxViscosity * Math.exp(-Math.pow((Ca_concentration - Ca_max) / W_two, 2)));
                        }

                        double testqqqt = q_k_ti.get("q_k_ti" + k + "_" + (ti - 1));
                        r_div = Math.sqrt(((testqqqt * 5.614) / (3.14 * h * Poro)) + Math.pow(rw, 2));

                        if (ti == 1) {
                            if (inj == 0) {
                                permMDT = Double.parseDouble(inputfilemap.get("PermMD" + k)) / viscosity;
                            } else {
                                permMDT = Double.parseDouble(kmap.get("permu" + "_" + k + "_" + (inj - 1) + "_" + (diffti - 1))) / viscosity;
                            }

                            if (permMDT <= 0) {
                                permMDT = permMD = Double.parseDouble(inputfilemap.get("PermMD" + k));
                            }

                        } else {
                            permMDT = deffective.get("ti_" + (ti - 1) + "k_" + k) / viscosity;
                        }

                        keff = Math.log(re / rw) / ((Math.log(r_div / rw) / permMDT) + (Math.log(re / r_div) / permMD));
                        if (keff > operm) {
                            if (mib == 0) {
                                keff = Double.parseDouble(kmap.get("permu" + "_" + k + "_" + (inj - 1) + "_" + (diffti - 1)));
                            } else {
                                keff = Double.parseDouble(kmap.get("permu" + "_" + k + "_" + inj + "_" + (ti - 1)));
                            }
                        }
                        double skin = (permMD / keff) - 1 * Math.log(r_div / rw);
                        double newskin = 0.0;
                        if (inj == 0) {
                            newskin = skin + Double.parseDouble(kmap.get("skin" + "_" + k + "_" + inj + "_" + (ti - 1)));
                        } else {
                            newskin = skin + Double.parseDouble(kmap.get("skin" + "_" + k + "_" + inj + "_" + (ti - 1)));
                        }
                        kmap.put("skin" + "_" + k + "_" + inj + "_" + ti, Double.toString(newskin));
                        kmap.put("permu" + "_" + k + "_" + inj + "_" + ti, Double.toString(keff));
                        deffective.put("ti_" + ti + "k_" + k, keff);
                        kh = keff * h;
                        kh_der = kh * Math.log((re * Math.pow(r_div, (viscosity - 1))) / Math.pow(rw, viscosity));
                        sumkhder = sumkhder + kh_der;
                        khder.put("khder_" + k + ti, kh_der);
                        sumkh = sumkhder;
                    } else {
                        kmap.put("skin" + "_" + k + "_" + inj + "_" + ti, kmap.get("skin" + "_" + k + "_" + inj + "_" + (ti - 1)));
                        if (inj == 0) {
                            permMD = (ti == 0) ? Double.parseDouble(inputfilemap.get("PermMD" + k)) : Double.parseDouble(kmap.get("permu" + "_" + k + "_" + (inj - 1) + "_" + (diffti - 1)));
                        } else {
                            permMD = (ti == 0) ? Double.parseDouble(kmap.get("permu" + "_" + k + "_" + (inj - 1) + "_" + (diffti - 1))) : Double.parseDouble(kmap.get("permu" + "_" + k + "_" + inj + "_" + (ti - 1)));
                        }
                        kmap.put("permu" + "_" + k + "_" + inj + "_" + ti, Double.toString(permMD));
                        keff = 0.0;
                        khder.put("khder_" + k + ti, 0.0);
                    }
                    if (ti == (durations - 1)) {
                        keffective.put("k" + k, keff);
                    }

                }
                if (k == layers) {
                    mib++;
                }
            }

            /* calculation of Qi vitip vwh rwh */
            for (int k = 0; k <= (layers - 1); k++) {
                kh_der = khder.get("khder_" + k + ti);
                if (kh_der > 0) {
                    double tao = kh_der / (sumkh - kh_der);
                    q = totalpumprate * tao / (1 + tao);
                    q_k_ti.put("q_k_ti" + k + "_" + ti, q);
                    rwhlayer.put("rwh" + k + ti, r_div);
                } else {
                    q_k_ti.put("q_k_ti" + k + "_" + ti, 0.0);
                    rwhlayer.put("rwh" + k + ti, 0.0);
                }
            }
        }

    }
}
