package com.petroleumsoft.stimopti.algos;


import java.util.Map;

/**
 *
 * @author wsmrt
 */
public class StimulatedPermeability {

    public static double kstim(Map<String, String> inputfilemap, double qlt, int k, int inj, double Permu, double rwh, double rwwc, Map<String, String> KHP, double Viscosity, Map<String, String> Kmap, int d) throws Exception {
        double ks = 200;
        //inputs

        if (inj == 0 || rwwc == 0) {
            rwwc = Double.parseDouble(inputfilemap.get("WELLBORE RADIUS (FT)"));
        }
        double difftithis_ = (inj == 0) ? Double.parseDouble(inputfilemap.get("Duration" + inj)) : Double.parseDouble(inputfilemap.get("Duration" + (inj - 1)));
        int difftithis = (int) difftithis_;
        double layers = Double.parseDouble(inputfilemap.get("Layers"));
        double preavg1 = 0.0;
        for (int p1 = 0; p1 < layers; p1++) {
            preavg1 += Double.parseDouble(inputfilemap.get("Pre-Skin" + p1));
        }
        double pre_stim = preavg1 / layers;
        double wrad = Double.parseDouble(inputfilemap.get("WELLBORE RADIUS (FT)"));
        double Operm = Double.parseDouble(inputfilemap.get("Perm" + k));

        double re = Double.parseDouble(inputfilemap.get("DRAINAGE RADIUS (FT)"));
        double delp = Double.parseDouble(KHP.get("Delp_" + k));
        double h = Double.parseDouble(inputfilemap.get("ToDepth" + k)) - Double.parseDouble(inputfilemap.get("FromDepth" + k));
        double skin = 0.0;
        double kstim = 0.0;
        double ksf = 0.0;
        //IMP** sensi in viscosity is only in Diverter hence**//
        Viscosity = Double.parseDouble(inputfilemap.get("Viscosity (cP)"));
//        if (inj == 0) {
//            Operm = (d == 0) ? Double.parseDouble(inputfilemap.get("PermMD" + k)) : Double.parseDouble(Kmap.get("permu" + "_" + k + "_" + inj + "_" + (d - 1)));
//        } else {
//            Operm = (d == 0) ? Double.parseDouble(Kmap.get("permu" + "_" + k + "_" + (inj - 1) + "_" + (difftithis - 1))) : Double.parseDouble(Kmap.get("permu" + "_" + k + "_" + inj + "_" + (d - 1)));
//        }

        for (;;) {
            skin = ((Operm / ks) - 1) * (Math.log((rwh + wrad) / wrad));
            if (skin < 0) {
                skin = ((Operm / ks) - 1) * (Math.log(((rwh + wrad) / wrad)));
            } else {
                skin = -Math.log(((rwh + wrad) / wrad));
            }
            //kstim = (141.2 * qlt * 24 * 60 * Viscosity * (Math.log((re / wrad)) -0.75 + skin)) / (delp * h);
            kstim = (qlt * 1440 * Viscosity * (Math.log(re / wrad) + skin)) / (0.00708 * delp * h);
            double div = kstim / ks;
            if (div >= 0.99 && div <= 1.01) {
                break;
            } else {
                ks = kstim;
            }
        }
        Kmap.put("skinw" + "_" + k + "_" + inj + "_" + d, Double.toString(skin));
        double newskin = 0.0;
        if (inj == 0) {
            if (d == 0) {
                newskin = skin + pre_stim;
            } else {
                newskin = skin + Double.parseDouble(Kmap.get("skin" + "_" + k + "_" + inj + "_" + (d - 1)));
            }
        } else if (d == 0) {

            newskin = skin + Double.parseDouble(Kmap.get("skin" + "_" + k + "_" + (inj - 1) + "_" + (difftithis - 1)));
        } else {

            newskin = skin + Double.parseDouble(Kmap.get("skin" + "_" + k + "_" + inj + "_" + (d - 1)));
        }
        Kmap.put("skin" + "_" + k + "_" + inj + "_" + d, Double.toString(newskin));
        ksf = Math.log(re / wrad) / ((Math.log((rwh + wrad) / wrad) / ks) + (Math.log(re / (rwh + wrad)) / Operm));
        if (ksf < Operm) {
            ksf = Operm;
        }
        return ksf;
    }
}
