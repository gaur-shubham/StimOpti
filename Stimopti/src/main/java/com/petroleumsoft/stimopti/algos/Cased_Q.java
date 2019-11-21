package com.petroleumsoft.stimopti.algos;


import java.text.DecimalFormat;
import java.util.Map;
import java.util.TreeMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author wasee
 */
public class Cased_Q {
   public  void casedDelpBH(Map<String, String> inputfilemap, String cases, String dtype, Map<String, String> QLTmap) throws Exception {
        DecimalFormat df = new DecimalFormat("0.00000");
        int layers = Integer.parseInt(inputfilemap.get("NoofLayers"));
        double rw = Double.parseDouble(inputfilemap.get("WELLBORE RADIUS (FT)"));
        double re = Double.parseDouble(inputfilemap.get("DRAINAGE RADIUS (FT)"));
        String acidtype = inputfilemap.get("ACID TYPE");
        int stages = Integer.parseInt(inputfilemap.get("NOofStages"));
        double dp_dl = 0.0;
        double Perm = 0.0;
        int Rlayers2 = 0;
        double zonePressure;
        double h;
        double pw;
        double qlt = 0.0;
        double ks = 0.0;
        double pumprate = 0;
        double a = 0;
        double kH = 0;
        double L_h = 0.0;
        double Viscosity = 0.0;
        double Pconu = 0.0;
        int part = 0;
        double hh = Double.parseDouble(inputfilemap.get("DISTANCE OF RESERVOIR BOTTOM TO HORIZONTAL WELL (FT)"));
        double cr = Double.parseDouble(inputfilemap.get("LEAK-OFF CO-EFFICIENT (FT/MIN^1/2)")) * 0.039;

        double pre_skin = 0.0;

        String WellType = inputfilemap.get("WELL TYPE");
        if (WellType.equals("HORIZONTAL")) {
            L_h = Double.parseDouble(inputfilemap.get("LENGTH OF HORIZONTAL REACH OF WELL (FT)"));
            a = (L_h / 2) * Math.sqrt(0.5 + Math.sqrt((0.25 + Math.pow(re / (L_h / 2), 4))));
            //  kH = Double.parseDouble(inputfilemap.get("HORIZONTAL PERMEABILITY (MD)"));
        }
        Map<String, String> kmap = new TreeMap<String, String>();
        Map<String, String> Qtube = new TreeMap<String, String>();
        Map<String, String> KHP = new TreeMap<String, String>();
        Map<String, String> Q1_Q2 = new TreeMap<String, String>();
        Map<String, String> pwmap = new TreeMap<String, String>();
        Map<String, String> Penetration = new TreeMap<String, String>();
        double diffti = 0.0;

        for (int j = 0; j <= (stages - 1); j++) {
            String stage = inputfilemap.get("Stage" + j);
            System.out.println(stage);
            double difftithis_ = (j == 0) ? Double.parseDouble(inputfilemap.get("Duration" + j)) : Double.parseDouble(inputfilemap.get("Duration" + (j - 1)));
            int difftithis = (int) difftithis_;
            diffti = Double.parseDouble(inputfilemap.get("Duration" + j));

            if (!dtype.equalsIgnoreCase("FOAMED")) {
                if (stage.equalsIgnoreCase("DIVERTER")) {
                    WithDiverter22.Diverter(inputfilemap, kmap, j, Penetration, dtype, QLTmap);
                }
            }
            ///*********Step 1 ***** percent volume*****///
            for (int d = 0; d < diffti; d++) {

                if (cases.equalsIgnoreCase("Base") || cases.equalsIgnoreCase("Shearthinning")) {
                    Viscosity = Double.parseDouble(inputfilemap.get("Viscosity (cP)"));
                    pumprate = Double.parseDouble(inputfilemap.get("PumpRateBMP" + j));
                    Pconu = Double.parseDouble(inputfilemap.get("Concentration (%)"));
                }


                double Qt = 0.0;
                Qt = pumprate;
                double Qtt = pumprate;
                double pore_radius = Double.parseDouble(inputfilemap.get("PORE RADIUS (micro-m)"));
                double injsg = Double.parseDouble(inputfilemap.get("INJECTION FLUID SPECIFIC GRAVITY"));
                double injpp = Double.parseDouble(inputfilemap.get("BHPPSI" + j));
                double dt = Double.parseDouble(inputfilemap.get("PRODUCTION TUBING DIAMETER (INCH)"));
                double drw = Double.parseDouble(inputfilemap.get("DAMAGE RADIUS (FT)"));
                double inclination = Double.parseDouble(inputfilemap.get("WELL INCLINATION (0-90)"));
                double kh_kv = Double.parseDouble(inputfilemap.get("KH/KV RATIO"));
                double dc = Double.parseDouble(inputfilemap.get("CASING INNER DIAMETER (INCH)"));
                String injType = inputfilemap.get("INJECTION TYPE");
                double D = injType.equals("BULLHEAD") ? (dt) : (dt - Double.parseDouble(inputfilemap.get("COILED TUBING OUTER DIAMETER (INCH)")));
                double e = Double.parseDouble(inputfilemap.get("ABSOLUTE ROUGHNESS (INCH)"));
                double sumkhp = 0.0;
                double khp;
                double Qc = 0.0;
                double s_theta = 0.0;
                double gama = 0.0;
                double dirf = 0.0;
                double f = 0.0;

                //***friction factor only**//
                double nre = (1.48 * (pumprate * 1440) * injsg * 62.427) / ((dc - dt) * Viscosity);
                if (nre > 2000) {
                    double f1 = 0.026 * Math.pow((e), 0.225) + 0.133 * (e);
                    double f2 = 22 * Math.pow((e), 0.44);
                    double f3 = 1.62 * Math.pow((e), 0.134);
                    f = f1 + (f2 * Math.pow(nre, (-f3)));
                } else if (nre < 2000) { //***** tRANSITION NOT CONSIDERED**///
                    f = 64 / nre;
                }

                for (int iu = 0; iu <= (layers - 1); iu++) {
                    zonePressure = Double.parseDouble(inputfilemap.get("ZonePressPSI" + iu));
                    if (j == 0) {
                        Perm = (d == 0) ? Double.parseDouble(inputfilemap.get("PermMD" + iu)) : Double.parseDouble(kmap.get("permu" + "_" + iu + "_" + j + "_" + (d - 1)));
                    } else {
                        Perm = (d == 0) ? Double.parseDouble(kmap.get("permu" + "_" + iu + "_" + (j - 1) + "_" + (difftithis - 1))) : Double.parseDouble(kmap.get("permu" + "_" + iu + "_" + j + "_" + (d - 1)));
                    }
                    if (Perm > 0) {
                        h = Double.parseDouble(inputfilemap.get("ToDefthFT" + iu)) - Double.parseDouble(inputfilemap.get("FromDefthFT" + iu));
                        dp_dl = ((1 / (703.2 * 3.2)) * (injsg * 1000) * f * (Math.pow(0.0008321 * Qt, 2)) / (9.8 * Math.pow((D * 2 * 0.3048), 5)));
                        pw = (injpp - (dp_dl * h));// + (0.433 * injsg * tvd);
                        pwmap.put("k_" + iu, Double.toString(pw - zonePressure));
                        sumkhp += (Perm * h * (pw - zonePressure));
                    }
                }
                int Rlayers = 0;
                for (int k = 0; k <= (layers - 1); k++) {
                    pre_skin = Double.parseDouble(inputfilemap.get("pre-stim" + k));
                    zonePressure = Double.parseDouble(inputfilemap.get("ZonePressPSI" + k));
                    if (j == 0) {
                        Perm = (d == 0) ? Double.parseDouble(inputfilemap.get("PermMD" + k)) : Double.parseDouble(kmap.get("permu" + "_" + k + "_" + j + "_" + (d - 1)));
                    } else {
                        Perm = (d == 0) ? Double.parseDouble(kmap.get("permu" + "_" + k + "_" + (j - 1) + "_" + (difftithis - 1))) : Double.parseDouble(kmap.get("permu" + "_" + k + "_" + j + "_" + (d - 1)));
                    }
                    kH = (Perm * 10);
                    h = Double.parseDouble(inputfilemap.get("ToDefthFT" + k)) - Double.parseDouble(inputfilemap.get("FromDefthFT" + k));
                    dp_dl = ((1 / (703.2 * 3.2)) * (injsg * 1000) * f * (Math.pow((0.0008321 * Qt), 2))) / (9.8 * Math.pow((D / 3.28084), 5));
                    pw = injpp - (dp_dl * h);// + (0.433 * injsg * tvd);
                    khp = Perm * h * (pw - zonePressure);
                    double q1 = Perm > 0 ? Qt * khp / (sumkhp) : 0.0;
                    double q2 = 0.0;
                    if (WellType.equalsIgnoreCase("SLANTED")) {
                        gama = Math.pow(Math.pow(Math.cos(Math.toRadians(inclination)), 2) + kh_kv * Math.pow(Math.sin(Math.toRadians(inclination)), 2), (1 / 2));
                        s_theta = Math.log(((4 * rw * Math.cos(Math.toRadians(inclination))) / (h * gama)) * Math.sqrt(kh_kv)) + (Math.cos(Math.toRadians(inclination)) / gama) * Math.log((h / (2 * rw * (1 + Math.pow(gama, -1)))) * Math.sqrt((kh_kv * gama) / (Math.cos(Math.toRadians(inclination)))));
                        q2 = (0.007080 * khp) / (1440 * Viscosity * Math.log(re / rw) - 0.75 + pre_skin + s_theta);
                    } else {
                        q2 = Perm > 0 ? (WellType.equals("HORIZONTAL") ? (0.007080 * khp) / (1440 * Viscosity * ((Math.log(((a + Math.sqrt((Math.pow(a, 2) - Math.pow((L_h / 2), 2)))) / (L_h / 2))) + (((Math.sqrt(kH / Perm) * hh) / L_h) * (Math.log((Math.sqrt(kH / Perm) * hh) / (rw * (Math.sqrt(kH / Perm) + 1))) + pre_skin))))) : (0.007080 * khp) / (1440 * Viscosity * Math.log(re / rw) - 0.75 + pre_skin)) : 0.0;// check later for the constants
                    }
                    KHP.put("Delp_" + k, df.format(pw - zonePressure));
                    Qc = Qtt - (q1 + q2);
                    dirf = Qc / Qt;
                    if (Qc > 0) {
                        if (dirf >= 0.99 && dirf <= 1.01) {
                            Qt = Qc;
                            Qtube.put("Qt_" + k + " " + j + "_" + d, df.format(Qt));
                            Q1_Q2.put("Q1_Q2" + k, df.format(q1 + q2));
                            KHP.put("KHP_" + k, df.format(khp));
                            Qtt = Qt;
                            Rlayers++;
                        } else {
                            k = k - 1;
                            Qt = Qc;
                        }

                    } else {
                        Q1_Q2.put("Q1_Q2" + k, df.format(Qtt));
                        Qt = 0.0;
                        Qtt = Qt;
                        Qtube.put("Qt_" + k + " " + j + "_" + d, df.format(0.0));
                        KHP.put("KHP_" + k, df.format(0.0));

                    }
                }
                //****New sumkhp for q negative*****//
                sumkhp = 0.0;
                for (int iu = 0; iu <= (Rlayers - 1); iu++) {
                    zonePressure = Double.parseDouble(inputfilemap.get("ZonePressPSI" + iu));
                    if (j == 0) {
                        Perm = (d == 0) ? Double.parseDouble(inputfilemap.get("PermMD" + iu)) : Double.parseDouble(kmap.get("permu" + "_" + iu + "_" + j + "_" + (d - 1)));
                    } else {
                        Perm = (d == 0) ? Double.parseDouble(kmap.get("permu" + "_" + iu + "_" + (j - 1) + "_" + (difftithis - 1))) : Double.parseDouble(kmap.get("permu" + "_" + iu + "_" + j + "_" + (d - 1)));
                    }
                    if (Perm > 0) {
                        h = Double.parseDouble(inputfilemap.get("ToDefthFT" + iu)) - Double.parseDouble(inputfilemap.get("FromDefthFT" + iu));
                        dp_dl = ((1 / (703.2 * 3.2)) * (injsg * 1000) * f * (Math.pow(0.0008321 * Qt, 2)) / (9.8 * Math.pow((D * 2 * 0.3048), 5)));
                        pw = (injpp - (dp_dl * h));// + (0.433 * injsg * tvd);
                        sumkhp += (Perm * h * (pw - zonePressure));
                    }
                }
                if (dtype.equalsIgnoreCase("FOAMED")) {
                    if (stage.equalsIgnoreCase("DIVERTER")) {
                        WithDiverter22.Foam(inputfilemap, kmap, Qtube, Q1_Q2, KHP, Rlayers, j, d, sumkhp, Rlayers2, part);
                    }
                }

                for (int k = 0; k <= (layers - 1); k++) {
                    double rwh = 0.0;
                    double rwwc = 0.0;
                    double Poro = 0.0;
                    pre_skin = Double.parseDouble(inputfilemap.get("pre-stim" + k));
                    zonePressure = Double.parseDouble(inputfilemap.get("ZonePressPSI" + k));
                    if (j == 0) {
                        Perm = (d == 0) ? Double.parseDouble(inputfilemap.get("PermMD" + k)) : Double.parseDouble(kmap.get("permu" + "_" + k + "_" + j + "_" + (d - 1)));
                        double kd = Perm / ((pre_skin / (Math.log(drw / rwwc))) + 1);
                        double pd = ((Perm - kd) / Perm) * 100;
                        Poro = Double.parseDouble(inputfilemap.get("Poro" + k));
                        if (d == 0) {
                            Poro = Poro - (Poro * (pd / 100));
                        }
                    } else {
                        Perm = (d == 0) ? Double.parseDouble(kmap.get("permu" + "_" + k + "_" + (j - 1) + "_" + (difftithis - 1))) : Double.parseDouble(kmap.get("permu" + "_" + k + "_" + j + "_" + (d - 1)));
                        Poro = (d == 0) ? Double.parseDouble(kmap.get("porou" + "_" + k + "_" + (j - 1) + "_" + (difftithis - 1))) : Double.parseDouble(kmap.get("porou" + "_" + k + "_" + j + "_" + (d - 1)));
                    }
                    if (Perm > 0) {
                        h = Double.parseDouble(inputfilemap.get("ToDefthFT" + k)) - Double.parseDouble(inputfilemap.get("FromDefthFT" + k));
                        double q3 = (Double.parseDouble(Qtube.get("Qt_" + (Rlayers - 1) + " " + j + "_" + d))) * (Double.parseDouble(KHP.get("KHP_" + k)) / sumkhp);
                        qlt = (Double.parseDouble(Q1_Q2.get("Q1_Q2" + k))) + q3;
                        if (j != 0) {
                            rwwc = (d == 0) ? Double.parseDouble(Penetration.get("Penet" + k + "_" + (j - 1) + "_" + (difftithis - 1))) : Double.parseDouble(Penetration.get("Penet" + k + "_" + j + "_" + (d - 1)));
                        }
                        /*Fluid Loss Calculation*/
                        // double lossdelp = (f_bhp - zonePressure) * 6894.76;
                        double beta1 = (Pconu * 100.1) / (7300);
                        double r2_p = (2 * 3.14 * beta1 * cr * Math.pow(1, (1 / 2))) / (1 - Poro);
                        double p2 = Math.pow((pore_radius * 0.000001), 2);
                        double r = (-(-r2_p) + Math.sqrt(Math.pow(r2_p, 2) - 4 * 1 * (-p2))) / 2;
                        double qloss = ((2 * 3.14 * r * cr) / (Math.pow(1, 0.5))) / 0.00265;
                        /**/
                        qlt = qlt + qloss;
                        if (qlt > 0) {
                            QLTmap.put("QLTW " + k + " " + j + "_" + d, df.format(qlt));
                            if (stage.equalsIgnoreCase("ACID")) {
                                if (j == 0) {
                                    qlt = (d == 0) ? qlt : qlt + Double.parseDouble(QLTmap.get("QLT " + k + " " + j + "_" + (d - 1)));
                                } else {
                                    double qlt1 = 0.0;
                                    int ps = (j - 1);
                                    for (int kk = ps; kk >= 0; kk--) {
                                        String pckeck = inputfilemap.get("Stage" + kk);
                                        if (pckeck.equalsIgnoreCase("ACID")) {
                                            break;
                                        }
                                        if (kk == 0) {
                                            break;
                                        }
                                        ps--;
                                    }
                                    String pckeck = inputfilemap.get("Stage" + ps);
                                    if (pckeck.equalsIgnoreCase("ACID")) {
                                        double difftithisps_ = Double.parseDouble(inputfilemap.get("Duration" + ps));
                                        int difftithisps = (int) difftithisps_;
                                        qlt1 = Double.parseDouble(QLTmap.get("QLT " + k + " " + ps + "_" + (difftithisps - 1)));
                                        if (qlt1 == 0) {
                                            int j1 = j;
                                            int d1 = 0;
                                            if (d == 0) {
                                                j1 = ps;
                                            } else {
                                                d1 = d;
                                            }
                                            for (int s11 = j1; s11 >= 0; s11--) {
                                                double difftithis1_ = Double.parseDouble(inputfilemap.get("Duration" + s11));
                                                int difftithis1 = (int) difftithis1_;
                                                if (d1 > 0) {
                                                    difftithis1 = d1;
                                                }
                                                for (int d11 = difftithis1; d11 > 0; d11--) {
                                                    if (d11 == 1) {
                                                        d1 = 0;
                                                    }
                                                    for (int l11 = k; l11 >= k; l11--) {
                                                        //   System.out.println(s11+"_"+d11+"_"+l11);
                                                        qlt1 = Double.parseDouble(QLTmap.get("QLT " + l11 + " " + s11 + "_" + (d11 - 1)));
                                                        difftithis1--;
                                                        if (qlt1 > 0) {
                                                            break;
                                                        }
                                                    }
                                                    if (qlt1 > 0) {
                                                        break;
                                                    }
                                                }
                                                if (qlt1 > 0) {
                                                    break;
                                                }
                                            }

                                        }
                                        qlt = (d == 0) ? qlt + qlt1 : qlt + Double.parseDouble(QLTmap.get("QLT " + k + " " + j + "_" + (d - 1)));
                                    } else {

                                        qlt = (d == 0) ? qlt + 0.0 : qlt + Double.parseDouble(QLTmap.get("QLT " + k + " " + j + "_" + (d - 1)));

                                    }
                                }
                                if (acidtype.equals("EMULSIFIED")) {
                                    rwh = PenetrationAcidCarbonate.emulsified_Acid(inputfilemap, j, k, Poro, qlt, Pconu, Viscosity, Penetration, kmap, d);
                                } else {
                                    rwh = PenetrationAcidCarbonate.penetration(inputfilemap, k, j, qlt, Pconu, Penetration, kmap, d);
                                }
                            } else if (stage.equalsIgnoreCase("DIVERTER") || stage.equalsIgnoreCase("OVERFLUSH")) {
                                if (j == 0) {
                                    rwh = 0.0;
                                } else {
                                    rwh = (d == 0) ? Double.parseDouble(Penetration.get("Penet" + k + "_" + (j - 1) + "_" + (difftithis - 1))) : Double.parseDouble(Penetration.get("Penet" + k + "_" + j + "_" + (d - 1)));
                                }
                            }
                            QLTmap.put("QLT " + k + " " + j + "_" + d, df.format(qlt));
                            Penetration.put("Penet" + k + "_" + j + "_" + d, df.format(rwh));
                            if (stage.equalsIgnoreCase("ACID")) {
                                ks = (stage.equalsIgnoreCase("PREFLUSH")) ? 0.0 : StimulatedPermeability.kstim(inputfilemap, qlt, k, j, Perm, rwh, rwwc, KHP, Viscosity, kmap, d);
                                kmap.put("permu" + "_" + k + "_" + j + "_" + d, df.format(ks));
                            } else if (stage.equalsIgnoreCase("DIVERTER")) {
                            } else if (stage.equalsIgnoreCase("PREFLUSH")) {
                                ks = Double.parseDouble(inputfilemap.get("PermMD" + k));
                                kmap.put("permu" + "_" + k + "_" + j + "_" + d, df.format(ks));
                                kmap.put("skin" + "_" + k + "_" + j + "_" + d, Double.toString(pre_skin));
                                Penetration.put("Penet" + k + "_" + j + "_" + d, df.format(0.0));
                            } else if (stage.equalsIgnoreCase("OVERFLUSH")) {
                                ks = Double.parseDouble(kmap.get("permu" + "_" + k + "_" + (j - 1) + "_" + (difftithis - 1)));
                                kmap.put("permu" + "_" + k + "_" + j + "_" + d, df.format(ks));
                                kmap.put("skin" + "_" + k + "_" + j + "_" + d, kmap.get("skin" + "_" + k + "_" + (j - 1) + "_" + (difftithis - 1)));
                            }

                        } else {
                            QLTmap.put("QLT " + k + " " + j + "_" + d, df.format(0));
                            QLTmap.put("QLTW " + k + " " + j + "_" + d, df.format(0));
                            if (j == 0) {
                                if (!stage.equalsIgnoreCase("DIVERTER")) {
                                    ks = (d == 0) ? Double.parseDouble(inputfilemap.get("PermMD" + k)) : Double.parseDouble(kmap.get("permu" + "_" + k + "_" + j + "_" + (d - 1)));
                                    kmap.put("permu" + "_" + k + "_" + j + "_" + d, df.format(ks));
                                    if (d == 0) {
                                        kmap.put("skin" + "_" + k + "_" + j + "_" + d, Double.toString(0));
                                    } else {
                                        kmap.put("skin" + "_" + k + "_" + j + "_" + d, Double.toString(0));
                                    }
                                }
                                rwh = 0;
                                Penetration.put("Penet" + k + "_" + j + "_" + d, df.format(0));
                            } else {
                                if (!stage.equalsIgnoreCase("DIVERTER")) {
                                    ks = (d == 0) ? Double.parseDouble(kmap.get("permu" + "_" + k + "_" + (j - 1) + "_" + (difftithis - 1))) : Double.parseDouble(kmap.get("permu" + "_" + k + "_" + j + "_" + (d - 1)));
                                    kmap.put("permu" + "_" + k + "_" + j + "_" + d, df.format(ks));
                                    kmap.put("skin" + "_" + k + "_" + j + "_" + d, kmap.get("skin" + "_" + k + "_" + (j - 1) + "_" + (difftithis - 1)));
                                }
                                rwh = (d == 0) ? Double.parseDouble(Penetration.get("Penet" + k + "_" + (j - 1) + "_" + (difftithis - 1))) : Double.parseDouble(Penetration.get("Penet" + k + "_" + j + "_" + (d - 1)));
                                Penetration.put("Penet" + k + "_" + j + "_" + d, Double.toString(rwh));

                            }
                        }

                        kmap.put("porou" + "_" + k + "_" + j + "_" + d, df.format(Poro));
                    } else {
                        QLTmap.put("QLT " + k + " " + j + "_" + d, df.format(0));
                        QLTmap.put("QLTW " + k + " " + j + "_" + d, df.format(0));
                        if (j == 0) {
                            if (!stage.equalsIgnoreCase("DIVERTER")) {
                                if (d == 0) {
                                    kmap.put("skin" + "_" + k + "_" + j + "_" + d, Double.toString(0));
                                } else {
                                    kmap.put("skin" + "_" + k + "_" + j + "_" + d, kmap.get("skin" + "_" + k + "_" + j + "_" + (d - 1)));
                                }
                                ks = (d == 0) ? Double.parseDouble(inputfilemap.get("PermMD" + k)) : Double.parseDouble(kmap.get("permu" + "_" + k + "_" + j + "_" + (d - 1)));
                                kmap.put("permu" + "_" + k + "_" + j + "_" + d, df.format(ks));
                            }
                            Penetration.put("Penet" + k + "_" + j + "_" + d, df.format(0));
                        } else {
                            if (!stage.equalsIgnoreCase("DIVERTER")) {
                                if (d == 0) {
                                    kmap.put("skin" + "_" + k + "_" + j + "_" + d, kmap.get("skin" + "_" + k + "_" + (j - 1) + "_" + (difftithis - 1)));
                                } else {
                                    kmap.put("skin" + "_" + k + "_" + j + "_" + d, kmap.get("skin" + "_" + k + "_" + j + "_" + (d - 1)));
                                }
                                ks = (d == 0) ? Double.parseDouble(kmap.get("permu" + "_" + k + "_" + (j - 1) + "_" + (difftithis - 1))) : Double.parseDouble(kmap.get("permu" + "_" + k + "_" + j + "_" + (d - 1)));
                                kmap.put("permu" + "_" + k + "_" + j + "_" + d, df.format(ks));
                            }

                            rwh = (d == 0) ? Double.parseDouble(Penetration.get("Penet" + k + "_" + (j - 1) + "_" + (difftithis - 1))) : Double.parseDouble(Penetration.get("Penet" + k + "_" + j + "_" + (d - 1)));
                            Penetration.put("Penet" + k + "_" + j + "_" + d, Double.toString(rwh));

                        }
                        Poro = j == 0 ? Double.parseDouble(inputfilemap.get("Poro" + k)) : ((d == 0) ? Double.parseDouble(kmap.get("porou" + "_" + k + "_" + (j - 1) + "_" + (difftithis - 1))) : Double.parseDouble(kmap.get("porou" + "_" + k + "_" + j + "_" + (d - 1))));
                        kmap.put("porou" + "_" + k + "_" + j + "_" + d, df.format(Poro));
                    }
                }

                System.gc();
            }

        }
        Qtube.clear();
        KHP.clear();
        Q1_Q2.clear();
        pwmap.clear();
        Penetration.clear();
        kmap.clear();
        System.gc();
    }
 
    
}
