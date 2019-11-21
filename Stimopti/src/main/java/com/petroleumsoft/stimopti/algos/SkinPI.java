package com.petroleumsoft.stimopti.algos;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author waseem
 */
public class SkinPI {

    public void skinPi(Map<String, String> inputfilemap, Map<String, String> penetrationlengthplot, Map<String, String> kMap, Map<String, String> productivityindex, Map<String, String> skincalculationplot, Map<String, String> foldmap) {
        DecimalFormat df = new DecimalFormat("##.#####");
        int layers = Integer.parseInt(inputfilemap.get("Layers"));
        int stages = Integer.parseInt(inputfilemap.get("Stages"));
        double rwwc = Double.parseDouble(inputfilemap.get("WELLBORE RADIUS (FT)"));
        String comp_type = inputfilemap.get("COMPLETION TYPE");
        double skin = 0;
        double ks = 0;
        double rwh = 0;
        String Phase = "";
        String Phase2 = "";
        String Phase3 = "";
        int kk = 0;
        int kk1 = 0;
        for (kk = 0;; kk++) {
            try {
                double perfS = Double.parseDouble(inputfilemap.get("perfSTARTFT" + kk));
            } catch (NullPointerException n) {
                break;
            }
        }
        if (!comp_type.equalsIgnoreCase("OPEN HOLE")) {
            for (kk1 = 0; kk1 < (layers - 1); kk1++) {
                double depth = Double.parseDouble(inputfilemap.get("FromDefthFT" + kk1));
                double perfS = Double.parseDouble(inputfilemap.get("perfSTARTFT" + 0));
                if (depth == perfS) {
                    break;
                }
            }
        }
        /* PRODUCTIVIVTY INPUTS START*/
        double pwf = 2500.0;
        // List<String> hproduction = new ArrayList();
        // Map<String, String> PIinjlayer = new LinkedHashMap();
        double meu_o = Double.parseDouble(inputfilemap.get("RESERVOIR OIL VISCOSITY (CP)"));
        String FlowType = inputfilemap.get("FLOW TYPE");
        double prestim = 0.0;
        double Bo_gas = 0.0;
        double meu_gas = 0.0;
        double Bo_water = 0.0;
        double meu_water = 0.0;
        double kr_w = 0.0;
        double kr_g = 0.0;
        double kh_kv = 0.0;
        double spgr_gas = 0.0;
        double L = 0.0;

        if (inputfilemap.containsKey("Phase2")) {
            Phase2 = inputfilemap.get("PHASE2");
            Bo_gas = Double.parseDouble(inputfilemap.get("GAS FORMATION VOLUME FACTOR (BBL/STB)"));
            meu_gas = Double.parseDouble(inputfilemap.get("RESERVOIR GAS VISCOSITY (CP)"));
            kr_g = Double.parseDouble(inputfilemap.get("RELATIVE PERMEABILITY OF GAS"));
            spgr_gas = Double.parseDouble(inputfilemap.get("RESERVOIR GAS DENSITY (KG/M3)")) / 62.427;
        }
        if (inputfilemap.containsKey("Phase3")) {
            Phase3 = inputfilemap.get("PHASE3");

            Bo_water = Double.parseDouble(inputfilemap.get("WATER FORMATION VOLUME FACTOR (BBL/STB)"));
            meu_water = Double.parseDouble(inputfilemap.get("RESERVOIR WATER VISCOSITY (CP)"));
            kr_w = Double.parseDouble(inputfilemap.get("RELATIVE PERMEABILITY OF WATER"));

        }
        if (inputfilemap.containsValue("Slanted")) {
            kh_kv = Double.parseDouble(inputfilemap.get("KH/KV RATIO"));
        }
        if (inputfilemap.containsValue("Horizontal")) {
            L = Double.parseDouble(inputfilemap.get("LENGTH OF HORIZONTAL REACH OF WELL (FT)"));
        }
        double Ct = Double.parseDouble(inputfilemap.get("OIL TOTAL COMPRESSIBILITY (1/PSI)"));
        double meu_oil = Double.parseDouble(inputfilemap.get("RESERVOIR OIL VISCOSITY (CP)"));
        double inclination = Double.parseDouble(inputfilemap.get("WELL INCLINATION (0-90)"));
        double kr_o = Double.parseDouble(inputfilemap.get("RELATIVE PERMEABILITY OF OIL"));
        double m_slope = -0.5;//Double.parseDouble(inputfilemap.get("SLOPE OF CONSTANT RATE FLOW TEST, M"));
        String WellType = inputfilemap.get("WELL TYPE");
        double Bo = Double.parseDouble(inputfilemap.get("OIL FORMATION VOLUME FACTOR (BBL/STB)"));
        double rewc = Double.parseDouble(inputfilemap.get("DRAINAGE RADIUS (FT)"));

        double T_f = Double.parseDouble(inputfilemap.get("FORMATION TEMPERATURE (F)"));
        double f_bhp = Double.parseDouble(inputfilemap.get("FLOWING BOTTOM HOLE PRESSURE"));
        double drw = Double.parseDouble(inputfilemap.get("DAMAGE RADIUS (FT)"));
        double dper = 0.0;

        double perf_h = 0.0;
        if (comp_type.equalsIgnoreCase("CASED HOLE")) {
            perf_h = Double.parseDouble(inputfilemap.get("perfENDFT" + (kk - 1))) - Double.parseDouble(inputfilemap.get("perfENDFT0"));
        } else {
            perf_h = Double.parseDouble(inputfilemap.get("ToDepth" + (layers - 1))) - Double.parseDouble(inputfilemap.get("FromDepth0"));

        }
        Phase = inputfilemap.get("PHASE1");

        String Phase_type = "";
        /*   if (Phase.equals("OIL") && Phase2.equals("GAS") && Phase3.equals("water")) {
            Phase_type = "MULTIPHASE FLOW";

        } else if (Phase.equals("OIL") && Phase2.equals("GAS")) {
            Phase_type = "MULTIPHASE FLOW";
        } else if (Phase.equals("OIL") && Phase2.equals("WATER")) {
            Phase_type = "MULTIPHASE FLOW";
        } else if (Phase.equals("GAS") && Phase2.equals("WATER")) {
            Phase_type = "MULTIPHASE FLOW";
        } else {
            Phase_type = "Single Phase Flow";
        }*/
        Phase_type = "Single Phase Flow";

        //*******************GALAT HAI GASFLOW RATE***************//
        double gasflowrate = 1;//Double.parseDouble(inputfilemap.get("GAS FLOW RATE (MMSCF/DAY)"));
        /* PRODUCTIVIVTY INPUTS END*/
        List<Double> permlist = new ArrayList<Double>();
        for (int l = 0; l <= (layers - 1); l++) {
            List<Double> complist = new ArrayList<Double>();
            for (int s = 0; s <= (stages - 1); s++) {
                double duration = Double.parseDouble(inputfilemap.get("Duration" + s));
                for (int d = 0; d <= (duration - 1); d++) {
                    complist.add(Double.parseDouble(kMap.get("permu" + "_" + l + "_" + s + "_" + d)));
                }
            }
            double maxv = Collections.max(complist);
            permlist.add(maxv);
        }
        double s_theta = 0.0;
        for (int l = 0; l <= (layers - 1); l++) {
            prestim = Double.parseDouble(inputfilemap.get("Pre-Skin" + l));
            double DepthF = Double.parseDouble(inputfilemap.get("FromDepth" + l));
            double DepthT = Double.parseDouble(inputfilemap.get("ToDepth" + l));
            double ko = 0.0;
            if (!comp_type.equalsIgnoreCase("OPEN HOLE")) {
                ko = Double.parseDouble(inputfilemap.get("Perm" + kk1));
            } else {
                ko = Double.parseDouble(inputfilemap.get("Perm" + l));
            }

            double h = DepthT - DepthF;
            if (WellType.equalsIgnoreCase("SLANTED")) {
                double gama = Math.pow(Math.pow(Math.cos(Math.toRadians(inclination)), 2) + kh_kv * Math.pow(Math.sin(Math.toRadians(inclination)), 2), (1 / 2));
                s_theta = Math.log(((4 * rwwc * Math.cos(Math.toRadians(inclination))) / (h * gama)) * Math.sqrt(kh_kv)) + (Math.cos(Math.toRadians(inclination)) / gama) * Math.log((h / (2 * rwwc * (1 + Math.pow(gama, -1)))) * Math.sqrt((kh_kv * gama) / (Math.cos(Math.toRadians(inclination)))));

            }
            String DepthFrom = inputfilemap.get("FromDepth" + l);
            rwh = Double.parseDouble(penetrationlengthplot.get(inputfilemap.get("FromDepth" + l))) + rwwc;
            double kd1 = ko / ((prestim / (Math.log(drw / rwwc))) + 1);
            dper = ((ko - kd1) / ko) * 100;
            if (rwh > drw) {
                skin = (WellType.equalsIgnoreCase("SLANTED")) ? (-Math.log((rwh) / rwwc) + s_theta) : (-Math.log((rwh) / rwwc));
            } else if (rwwc < rwh && rwh < drw) {
                double kd = ko - ((dper * ko) / 100);
                skin = ((ko / kd) * Math.log((drw) / rwh)) - Math.log((drw) / rwwc);
//                skin_damage = ((ko/kd)-1)*Math.log((drw-rwh + rwwc)/rwwc);
//               
//                skin = (WellType.equalsIgnoreCase("SLANTED")) ? (-Math.log((rwh ) / rwwc) + s_theta)+skin_damage: (-Math.log((rwh) / rwwc))+skin_damage; 
            } else if (rwh <= rwwc) {
                skin = prestim;
            }

            skincalculationplot.put(DepthFrom, df.format(skin));
            double Perm = 0.0;
            Perm = permlist.get(l);
            if (rwh > drw) {
                Perm = permlist.get(l);
            } else {
                double kd = ko - ((dper * ko) / 100);
                if (Perm > 0) {
                    Perm = Math.log(rewc / rwwc) / ((Math.log((rewc / drw)) / ko) + (Math.log((drw / rwh)) / kd) + (Math.log((rwh / rwwc)) / permlist.get(l)));
                }
            }
            double phi = Double.parseDouble(inputfilemap.get("Poro" + l));

            double ZonePress = Double.parseDouble(inputfilemap.get("ZonalPressure" + l));

            double ks_gas = ks;
            double seconds = 24.0;

            //Below input used for horizontal wells only
            double J = 0;
            if (rwh > 0) {
                double effective_radius_acid = rwwc * Math.exp(-skin);
                double effective_radius_prestim = rwwc * Math.exp(-prestim);
                double fold_increase = (Math.log(rewc / effective_radius_prestim) - 0.75) / (Math.log(rewc / effective_radius_acid) - 0.75);
                foldmap.put(Double.toString(DepthF), df.format(fold_increase));

                if (Phase_type.equalsIgnoreCase("Single Phase Flow")) {

                    if (WellType.equalsIgnoreCase("Horizontal")) {
                        double kv = Double.parseDouble(inputfilemap.get("Perm" + l));
                        double kH = (Perm / 10);
                        //double reH = Double.parseDouble(inputfilemap.get("AVERAGE HORIZONTAL PERMEABILITY (MD)"));
                        double a = (L / 2) * Math.sqrt(0.5 + Math.sqrt((0.25 + Math.pow(rewc / (L / 2), 4))));
                        if (kv <= 0) {
                            J = 0;
                            productivityindex.put(DepthFrom, df.format(0));
                        } else {
                            if (Phase.equalsIgnoreCase("OIL")) {
                                double I_ani = Math.sqrt(kH / kv);
                                double skinH = ((Perm / permlist.get(l)) - 1) * Math.log((1 / (I_ani + 1)) * Math.sqrt((4 / 3) * (((Math.pow(a, 2)) / Math.pow(rewc, 2)) + (a / rewc) + 1)));
                                J = (Perm * h) / (141.2 * Bo * meu_oil * (Math.log(((a + Math.sqrt((Math.pow(a, 2) - Math.pow((L / 2), 2)))) / (L / 2))) + (((I_ani * h) / L) * (Math.log((I_ani * h) / (rwwc * (I_ani + 1))) + skinH))));
                            }

                            if (Phase2.equalsIgnoreCase("GAS")) {
                                double meu_gas_avg = 0.0;
                                double z_avg_gas = Double.parseDouble(inputfilemap.get("GAS FORMATION VOLUME FACTOR (BBL/STB)"));
                                meu_gas_avg = meu_gas;
                                J = (7.08 * Math.pow(10, -6) * kH * h) / (z_avg_gas * meu_gas_avg * (Math.log(rewc / rwwc) - 0.75 + skin));

                            }
                            productivityindex.put(DepthFrom, df.format(J));
                        }

                    }
                    if (WellType.equalsIgnoreCase("Vertical") || WellType.equalsIgnoreCase("SLANTED")) {
                        if (Perm <= 0) {
                            J = 0;
                            productivityindex.put(DepthFrom, df.format(0));
                        } else if (Phase.equalsIgnoreCase("OIL")) {

                            if (FlowType.equalsIgnoreCase("Radial Pseudo Steady state Flow")) {
                                J = (Perm * h) / ((141.2 * Bo * meu_o) * (Math.log(rewc / rwwc) - (0.75) + skin));
                            }

                            productivityindex.put(DepthFrom, df.format(J));

                        } else if (Phase2.equalsIgnoreCase("Gas")) {

                            double meu_gas_avg = 0;
                            double z_avg_gas = Double.parseDouble(inputfilemap.get("GAS FORMATION VOLUME FACTOR (BBL/STB)"));
                            double D_gas = 0;
                            double skin_effective = 0;
                            meu_gas_avg = meu_gas;
                            D_gas = (6 * Math.pow(10, -5) * spgr_gas * Math.pow(ks_gas, -0.1) * h) / (meu_gas * rwwc * Math.pow(perf_h, 2));
                            skin_effective = skin + D_gas * gasflowrate;
                            if (FlowType.equalsIgnoreCase("Radial Transient Flow")) {
                                J = (((ZonePress + pwf) * (Perm * h)) / (1637 * (T_f + 459.67) * z_avg_gas * meu_gas_avg * Bo_gas)) * ((Math.log10((Perm * seconds) / (phi * meu_gas_avg * Ct * Math.pow(rwwc, 2))) - (-3.23 + (0.869 * skin_effective))));
                            }
                            if (FlowType.equalsIgnoreCase("Radial Pseudo Steady state Flow")) {
                                J = (7.08 * Math.pow(10, -6) * Perm * h) / (z_avg_gas * meu_gas_avg * (Math.log(rewc / rwwc) - 0.75 + skin));
                            }
                            productivityindex.put(DepthFrom, df.format(J));
                        }
                    }

                } else if (Phase_type.equalsIgnoreCase("MULTIPHASE FLOW")) {
                    double J_multi = 0;
                    if (Perm <= 0) {
                        J_multi = 0;
                        productivityindex.put(DepthFrom, df.format(J_multi));
                    } else {
                        double q_oil = 0;
                        double q_gas = 0;
                        double q_water = 0;
                        double qrt_multi = 0;
                        double lambda_multi = 0;

                        double Rs = Double.parseDouble(inputfilemap.get("GOR, GAS OIL RATIO (SCF/STB)"));

                        if (FlowType.equalsIgnoreCase("Radial Pseudo Steady state Flow")) {
                            if (Phase.equalsIgnoreCase("OIL")) {
                                q_oil = (Perm * kr_o * h * (ZonePress - f_bhp)) / ((141.2 * Bo * meu_oil) * (Math.log(rewc / rwwc) - 0.75 + skin));
                            }

                            if (Phase2.equalsIgnoreCase("GAS")) {
                                q_gas = (Perm * kr_g * h * (ZonePress - f_bhp)) / ((141.2 * Bo_gas * meu_gas) * (Math.log(rewc / rwwc) - 0.75 + skin));
                            }
                            if (Phase3.equalsIgnoreCase("WATER")) {
                                q_water = (Perm * kr_w * h * (ZonePress - f_bhp)) / ((141.2 * Bo_water * meu_water) * (Math.log(rewc / rwwc) - 0.75 + skin));
                            }

                        }

                        qrt_multi = (q_oil * Bo) + (q_water * Bo_water) + (((q_gas * (0.1777 / 1000)) - ((q_oil * Rs) / 1000)) * Bo_gas);
                        lambda_multi = (-162.6 * qrt_multi) / (m_slope * h);
                        double J1 = -(lambda_multi * h) / 162.6;
                        double J2 = (Math.log10((1688 * phi * Ct * (Math.pow(rwwc, 2))) / (lambda_multi * 30 * 24)) - (0.868 * skin));
                        J_multi = J1 / J2;
                        productivityindex.put(DepthFrom, df.format(J_multi));

                    }
                }
            } else {
                productivityindex.put(DepthFrom, df.format(0));
                foldmap.put(DepthFrom, df.format(0));
            }
        }

    }

}
