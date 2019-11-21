package com.petroleumsoft.stimopti.algos;
import java.text.DecimalFormat;
import java.util.Map;

public class PenetrationAcidCarbonate {

    public static double penetration(Map<String, String> inputfilemap, int k, int inj, double q, double concentration, Map<String, String> Penetration, Map<String, String> kmap, int d) throws Exception {
        DecimalFormat df = new DecimalFormat("#.####");
        double rwwc = 0.0;
        double phi = 0;
        if (inj == 0) {
            rwwc = (d == 0) ? Double.parseDouble(inputfilemap.get("WELLBORE RADIUS (FT)")) : Double.parseDouble(Penetration.get("Penet" + k + "_" + inj + "_" + (d - 1)));
        } else {
            double difftithis_ = Double.parseDouble(inputfilemap.get("Duration" + (inj - 1)));
            int difftithis = (int) difftithis_;
            rwwc = (d == 0) ? Double.parseDouble(Penetration.get("Penet" + k + "_" + (inj - 1) + "_" + (difftithis - 1))) : Double.parseDouble(Penetration.get("Penet" + k + "_" + inj + "_" + (d - 1)));
        }
        if (rwwc == 0.0) {
            rwwc = Double.parseDouble(inputfilemap.get("WELLBORE RADIUS (FT)"));
        }

        double aciddensity = Double.parseDouble(inputfilemap.get("Specific Gravity")) * 1000;
        aciddensity = aciddensity / 1000;
        double rock_density = Double.parseDouble(inputfilemap.get("ROCK DENSITY (G/CM3)"));
        //double Pvbt_opt = Double.parseDouble(inputfilemap.get("OPTIMUM PORE VOLUME TO BREAKTHROUGH"));
        //double vi_opt = Double.parseDouble(inputfilemap.get("OPTIMUM INTERSTITIAL VELOCITY (CM/MIN)"));
        double diffti_ = (inj == 0) ? Double.parseDouble(inputfilemap.get("Duration" + inj)) : Double.parseDouble(inputfilemap.get("Duration" + (inj - 1)));
        int diffti = (int) diffti_;
        double Nac = 0;
        double beta = 0;
        //***** Layer Properties****//
        String Keylith = String.valueOf(k);
        String KeyDepthF = "FromDepth" + Keylith;
        String KeyDepthT = "ToDepth" + Keylith;
        double DepthF = Double.parseDouble(inputfilemap.get(KeyDepthF));
        double DepthT = Double.parseDouble(inputfilemap.get(KeyDepthT));
        double h = DepthT - DepthF;
        String poro = "Poro" + Keylith;
        phi = inj == 0 ? Double.parseDouble(inputfilemap.get(poro)) : Double.parseDouble(kmap.get("porou" + "_" + k + "_" + (inj - 1) + "_" + (diffti - 1)));
       
        double acid_conc = Double.parseDouble(inputfilemap.get("Concentration (%)"));
        double dcoef = Double.parseDouble(inputfilemap.get("Diffusion Co-Efficient (Ft2/Min)"));
        String coreflood = inputfilemap.get("COREFLOOD");
        double rwh = 0;
       
        if (coreflood.equalsIgnoreCase("NOT AVAILABLE")) {
            double qh = ((q * 0.0026) / (h * 0.304));
            beta = ((acid_conc / 100) * 1 * 100.1) / (2 * 36.5);
            Nac = (phi * beta * aciddensity) / ((1 - phi) * (rock_density));
            double vh = ((q * 0.0026) * (1 * 60)) / (h * 0.304);
            if (inj == 0) {
                rwh = (d == 0) ? (((Math.pow((vh * 1.5E-5 * Nac) / (3.14 * phi * Math.pow((dcoef * 0.00154), 0.66) * (Math.pow(qh, 0.33))), 0.625))) * 3.28) : (((Math.pow((vh * 1.5E-5 * Nac) / (3.14 * phi * Math.pow((dcoef * 0.00154), 0.66) * (Math.pow(qh, 0.33))), 0.625))) * 3.28);
            } else {
                rwh = (((Math.pow((vh * 1.5E-5 * Nac) / (3.14 * phi * Math.pow((dcoef * 0.00154), 0.66) * (Math.pow(qh, 0.33))), 0.625))) * 3.28);
            }

        } 
        beta = beta * aciddensity / rock_density;
        inputfilemap.put("beta_", df.format(beta));
        return rwh;
    }

    public static double emulsified_Acid(Map<String, String> inputfilemap, int inj, int k, double phi, double q, double Pconu, double Viscosity, Map<String, String> Penetration, Map<String, String> kmap, int d) {

        DecimalFormat df = new DecimalFormat("##.###");
        double diffti_ = (inj == 0) ? Double.parseDouble(inputfilemap.get("Duration" + inj)) : Double.parseDouble(inputfilemap.get("Duration" + (inj - 1)));
        int diffti1 = (int) diffti_;
        double h = (Double.parseDouble(inputfilemap.get("ToDefthFT" + k)) - Double.parseDouble(inputfilemap.get("FromDefthFT" + k)));
        Pconu = ((Pconu / 100) * (Double.parseDouble(inputfilemap.get("Specific Gravity")) * 1000)) / 36.5;
        double betaF = 0.5; //Stoichiometric coefficient of calcite
        double MWf = 100.1;// Molecular weight of Calcite only
        double V0f = Double.parseDouble(inputfilemap.get("CALCITE CONCENTRATION (%)"));
        double denf = Double.parseDouble(inputfilemap.get("ROCK DENSITY (G/CM3)"));
        double TempK = ((Double.parseDouble(inputfilemap.get("FORMATION TEMPERATURE (F)")) - 32) * 0.55555) + 273.15;
        double cal_conc = Double.parseDouble(inputfilemap.get("CALCITE CONCENTRATION (%)"));
        double re = Double.parseDouble(inputfilemap.get("DRAINAGE RADIUS (FT)"));
        double ef = 0.0;
        double Efs = 0.0;
        double rwh = 0.0;
        double beta = 0;
        double aciddensity =Double.parseDouble(inputfilemap.get("Specific Gravity")) * 1000;
        aciddensity = aciddensity / 1000;
        double rock_density = Double.parseDouble(inputfilemap.get("ROCK DENSITY (G/CM3)"));
        double calcite_comp = Double.parseDouble(inputfilemap.get("CALCITE CONCENTRATION (%)"));
        double concentration = Double.parseDouble(inputfilemap.get("Concentration (%)"));
        double R_f = 0.0;
        double rwwc = 0.0;

        if (inj == 0) {
            rwwc = (d == 0) ? Double.parseDouble(inputfilemap.get("WELLBORE RADIUS (FT)")) : Double.parseDouble(Penetration.get("Penet" + k + "_" + inj + "_" + (d - 1)));
        } else {
            double difftithis_ = Double.parseDouble(inputfilemap.get("Duration" + (inj - 1)));
            int difftithis = (int) difftithis_;
            rwwc = (d == 0) ? Double.parseDouble(Penetration.get("Penet" + k + "_" + (inj - 1) + "_" + (difftithis - 1))) : Double.parseDouble(Penetration.get("Penet" + k + "_" + inj + "_" + (d - 1)));
        }
        if (rwwc == 0.0) {
            rwwc = Double.parseDouble(inputfilemap.get("WELLBORE RADIUS (FT)"));
        }


        R_f = Double.parseDouble(inputfilemap.get("RETARDATION FACTOR"));
        phi = inj == 0 ? Double.parseDouble(inputfilemap.get("Poro" + k)) : Double.parseDouble(kmap.get("porou" + "_" + k + "_" + (inj - 1) + "_" + (diffti1 - 1)));
        double SS = ((2 * 3.14 * re * 0.3048 * h * 0.3048) + (2 * 3.14 * Math.pow(re * 0.3048, 2))) / (3.14 * Math.pow(re * 0.3048, 2) * h * 0.3048 * (1 - phi) * cal_conc * 0.01); //(0.187)/ (Math.pow((rwwc * 0.3048), 2) * (h * 0.3048));
        Efs = 7.291E7 * Math.exp(-7.55E3 / TempK) / R_f;
        double Acf = (phi * betaF * Pconu * MWf) / ((1 - phi) * (V0f / 100) * (denf * 1000));
        double DaAcid = (((1 - phi) * (calcite_comp / 100) * Efs * SS * 0.8) * 3.14 * Math.pow((rwwc * 0.3048), 2) * (h * 0.3048)) / (q * 0.00265);
        double theta = (q * 0.00265 * 1 * 60) / (3.14 * Math.pow((rwwc * 0.3048), 2) * (h * 0.3048) * phi);
        double efi1 = 1.0;

        do {
            ef = efi1;
            efi1 = ef - ((((Math.exp(DaAcid * ef) - 1) / (Acf * DaAcid)) + ef - theta) / ((Math.exp(DaAcid * ef)) / Acf)) + 1;
            if (Double.toString(efi1).equals("NaN")) {
                efi1 = ef = 0;
                break;
            }
        } while (!df.format(efi1).equals(df.format(ef)));
        if (inj == 0) {
            rwh = (((rwwc * 0.3048) * Math.pow(ef + 1, 0.5)) / 0.3048);
        } else {
            rwh = (((rwwc * 0.3048) * Math.pow(ef + 1, 0.5)) / 0.3048);
        }
        if (rwh == rwwc) {
            rwh = 0.0;
        }
        beta = (concentration * 0.01371232877);
        beta = beta * aciddensity / rock_density;
        inputfilemap.put("beta_", df.format(beta));
        return rwh;
    }

}


