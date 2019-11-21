package com.petroleumsoft.stimopti.algos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.util.ResourceUtils;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author waseem
 */
public class CoiledTubing {

	public void calculatecarbonateCT(Map<String, String> penetrationlengthplot, Map<String, String> volumepercentage,
			Map<String, String> productivityindex, Map<String, String> skincalculationplot, Map<String, String> foldmap)
			throws Exception {
		try {
			Map<String, String> injmap1  = new LinkedHashMap<String, String>();
			Map<String, String> inputfilemap = new LinkedHashMap<String, String>();

			/* Reading Input File */
			File file = ResourceUtils.getFile("classpath:config/Acid_Carbonate_Input.txt");
			// File file = new File("Acid_Carbonate_Input.txt");
			BufferedReader br = null;
			String line = "";
			int dc1 = 0;
			double total_mins = 0;
			br = new BufferedReader(new FileReader(file));
			while ((line = br.readLine()) != null) {
				if (line.startsWith("/***")) {
					continue;
				} else {
					int index = line.indexOf('=');
					String key = line.substring(0, index);
					String value = line.substring(index + 1);
					try {
						if (key.equalsIgnoreCase("Duration" + dc1)) {
							total_mins += Double.parseDouble(value);
							dc1++;
						}

					} catch (NullPointerException npe) {
					}
					injmap1.put(key, value.trim());
					inputfilemap.put(key, value.trim());

				}
			}
			br.close();
			/* Input File Reading Process Completed */

			/* Algorithms Coding Starts from Here */
			int layers = 0;

			layers = Integer.parseInt(inputfilemap.get("Layers"));
			int stages = Integer.parseInt(injmap1.get("Stages"));


			String cases = "Base";
			double volumed = 0.0;
			double durationd = 0.0;
			double injdepthi = 0.0;
			double injdepthl = 0.0;
			double Viscosity = 0, Pconu = 0, hh = 0.0, kh_kv = 0.0, dc = 0.0;
			int c = 0;
			List<Integer> as = new ArrayList<Integer>();
			for (int i = 0; i < stages; i++) {
				String direction = injmap1.get("Direction" + i);
				String tempstage = injmap1.get("Stage" + i);
				if (tempstage.equalsIgnoreCase("ACID") || tempstage.equalsIgnoreCase("DIVERTER")
						|| tempstage.equalsIgnoreCase("PREFLUSH") || tempstage.equalsIgnoreCase("OVERFLUSH")) {
					if (direction.equalsIgnoreCase("RIH")) {
						injdepthi = Double.parseDouble(injmap1.get("InjDepth" + i));
						durationd = Double.parseDouble(injmap1.get("Duration" + i));
						double dur = durationd / 2;
						injdepthl = Double.parseDouble(inputfilemap.get("FromDepth" + (layers - 1)));
						double layersd = Math.round((injdepthl - injdepthi) / dur);
						for (int ii = 0; ii <= (layers - 1); ii = ii + (int) layersd) {
							if (injdepthi < injdepthl) {
								volumed = Double.parseDouble(injmap1.get("Volume" + i)) / dur;
								inputfilemap.put("Volume" + c, Double.toString(volumed));
								inputfilemap.put("Stage" + c, injmap1.get("Stage" + i));
								inputfilemap.put("Pumprate" + c, injmap1.get("Pumprate" + i));
								inputfilemap.put("Bhp" + c, injmap1.get("Bhp" + i));
								inputfilemap.put("Injp" + c, injmap1.get("Injp" + i));
								inputfilemap.put("InjDepth" + c, Double.toString(Math.round(injdepthi)));
								inputfilemap.put("Duration" + c, Double.toString(2.0));
								injdepthi += layersd;
								c++;
							}
						}
						if (tempstage.equalsIgnoreCase("ACID")) {
							as.add(c);
						}

					} else if (direction.equalsIgnoreCase("POOH")) {
						injdepthi = Double.parseDouble(injmap1.get("InjDepth" + i));
						durationd = Double.parseDouble(injmap1.get("Duration" + i));
						double dur = durationd / 2;
						injdepthl = Double.parseDouble(inputfilemap.get("FromDepth0"));
						double layersd = Math.round((injdepthi - injdepthl) / dur);
						for (int ii = (layers - 1); ii >= 0; ii = ii - (int) layersd) {
							if (injdepthi > injdepthl) {
								volumed = Double.parseDouble(injmap1.get("Volume" + i)) / dur;
								inputfilemap.put("Volume" + c, Double.toString(volumed));
								inputfilemap.put("Stage" + c, injmap1.get("Stage" + i));
								inputfilemap.put("Pumprate" + c, injmap1.get("Pumprate" + i));
								inputfilemap.put("Bhp" + c, injmap1.get("Bhp" + i));
								inputfilemap.put("Injp" + c, injmap1.get("Injp" + i));
								inputfilemap.put("InjDepth" + c, Double.toString(Math.round(injdepthi)));
								inputfilemap.put("Duration" + c, Double.toString(2.0));
								injdepthi -= layersd;
								c++;
							}
						}
						if (tempstage.equalsIgnoreCase("ACID")) {
							as.add(c);
						}

					} else {
						inputfilemap.put("Volume" + c, injmap1.get("Volume" + i));
						inputfilemap.put("Stage" + c, injmap1.get("Stage" + i));
						inputfilemap.put("Pumprate" + c, injmap1.get("Pumprate" + i));
						inputfilemap.put("Bhp" + c, injmap1.get("Bhp" + i));
						inputfilemap.put("Injp" + c, injmap1.get("Injp" + i));
						inputfilemap.put("InjDepth" + c, injmap1.get("InjDepth" + i));
						inputfilemap.put("Duration" + c, injmap1.get("Duration" + i));
						c++;
						if (tempstage.equalsIgnoreCase("ACID")) {
							as.add(c);
						}
					}
				}

			}
			System.out.println("No of stages :   "+c);
			inputfilemap.put("Stages", Integer.toString(c));

			int tomi = ((int) total_mins * layers + stages);
			HashMap<String, String> injmap = new HashMap<>(tomi, 0.95f);
			Map<String, String> QLTmap = new TreeMap<String, String>();
			String dtype = inputfilemap.get("DIVERTER TYPE");
			if (inputfilemap.get("COMPLETION TYPE").equalsIgnoreCase("CASED HOLE")) {
				CasedQCt cq = new CasedQCt();
				cq.casedDelpCT(inputfilemap, cases, dtype, QLTmap);
			}
			String acidtype = inputfilemap.get("ACID TYPE");
			double pumprate = 0;
			DecimalFormat df = new DecimalFormat("0.00000");

			double rw = Double.parseDouble(inputfilemap.get("WELLBORE RADIUS (FT)"));
			double re = Double.parseDouble(inputfilemap.get("DRAINAGE RADIUS (FT)"));
			double drw = Double.parseDouble(inputfilemap.get("DAMAGE RADIUS (FT)"));

			double cr = Double.parseDouble(inputfilemap.get("LEAK-OFF CO-EFFICIENT (FT/MIN^1/2)")) * 0.039;
			double dp_dl = 0.0;
			double Perm = 0.0;
			double zonePressure;
			double h;
			double pw;
			double qlt = 0.0;
			double ks = 0.0;
			double a = 0;
			double kH = 0;
			double L_h = 0.0;
			double delpcas = 0.0;

			double pre_skin = 0.0;
			String WellType = inputfilemap.get("WELL TYPE");
			if (WellType.equals("HORIZONTAL")) {
				L_h = Double.parseDouble(inputfilemap.get("LENGTH OF HORIZONTAL REACH OF WELL (FT)"));
				a = (L_h / 2) * Math.sqrt(0.5 + Math.sqrt((0.25 + Math.pow(re / (L_h / 2), 4))));
				// kH = 1.0;
			}

			if (inputfilemap.containsValue("Horizonatl") || inputfilemap.containsValue("Slanted")) {
				hh = Double.parseDouble(inputfilemap.get("DISTANCE OF RESERVOIR BOTTOM TO HORIZONTAL WELL (FT)"));
				kh_kv = Double.parseDouble(inputfilemap.get("KH/KV RATIO"));

			}
			if (inputfilemap.containsValue("Cased Hole")) {
				dc = Double.parseDouble(inputfilemap.get("CASING INNER DIAMETER (INCH)"));
			}
			/*
			 * Map<String, String> Qtube = new TreeMap <String, String>(); Map<String,
			 * String> KHP = new TreeMap <String, String>(); Map<String, String> Q1_Q2 = new
			 * TreeMap<String, String>(); Map<String, String> pwmap = new TreeMap<String,
			 * String>(); Map<String, String> Penetration = new TreeMap<String, String>();
			 * Map<String, String> delpcasmap1 = new TreeMap<String, String>(); Map<String,
			 * String> qltp = new TreeMap<String, String>(); Map<String, String> kmap = new
			 * LinkedHashMap<>(); Map<String, String> QLT = new LinkedHashMap<>();
			 *
			 * 
			 */

			double injdepth = 0;
			double f = 0.0;
			double diffti = 0.0;
			stages = Integer.parseInt(inputfilemap.get("Stages"));
			// System.out.println(dtype + " ");
			for (int j = 0; j <= (stages - 1); j++) {
				String stage = inputfilemap.get("Stage" + j);
				System.out.println("Stage : " + j);
				double difftithis_ = (j == 0) ? Double.parseDouble(inputfilemap.get("Duration" + j))
						: Double.parseDouble(inputfilemap.get("Duration" + (j - 1)));
				int difftithis = (int) difftithis_;
				diffti = Double.parseDouble(inputfilemap.get("Duration" + j));
				if (!dtype.equalsIgnoreCase("FOAMED")) {
					if (stage.equalsIgnoreCase("DIVERTER")) {
						WithDiverter22.Diverter(inputfilemap, injmap, j, injmap, dtype, injmap);
					}
				}
				/// *******************************Step 1 ************************************
				/// percent volume*****
				// System.out.println("Stage" + j);
				for (int d = 0; d < diffti; d++) {
					int Rlayers1 = 1;
					int Rlayers2 = 0;

					if (cases.equalsIgnoreCase("Base") || cases.equalsIgnoreCase("Shearthinning")) {
						Viscosity = Double.parseDouble(inputfilemap.get("Viscosity (cP)"));
						pumprate = Double.parseDouble(inputfilemap.get("Pumprate" + j));
						Pconu = Double.parseDouble(inputfilemap.get("Concentration (%)"));
					}
					injdepth = Double.parseDouble(inputfilemap.get("InjDepth" + j));
					double H = Double.parseDouble(inputfilemap.get("FromDepth" + (layers - 1)))
							- Double.parseDouble(inputfilemap.get("FromDepth" + 0));
					double HH = injdepth - Double.parseDouble(inputfilemap.get("FromDepth" + 0));
					double Qt1 = (HH * pumprate) / H;
					double Qtt1 = (HH * pumprate) / H;
					double Qt2 = pumprate - Qt1;
					double Qtt2 = pumprate - Qtt1;
					double injsg = Double.parseDouble(inputfilemap.get("INJECTION FLUID SPECIFIC GRAVITY"));
					double injpp = Double.parseDouble(inputfilemap.get("Bhp" + j));// dhayan de yahan pe hydrostatic add
																					// hoga
					double dt = Double.parseDouble(inputfilemap.get("PRODUCTION TUBING DIAMETER (INCH)"));

					String injType = inputfilemap.get("INJECTION TYPE");

					double inclination = Double.parseDouble(inputfilemap.get("WELL INCLINATION (0-90)"));
					double pore_radius = Double.parseDouble(inputfilemap.get("PORE RADIUS (MICRO-M)"));
					double acid_den = Double.parseDouble(inputfilemap.get("Specific Gravity")) * 1000;
					double D = injType.equals("BULLHEAD") ? (dc - dt)
							: (Double.parseDouble(inputfilemap.get("COILED TUBING OUTER DIAMETER (INCH)")));
					double e = Double.parseDouble(inputfilemap.get("ABSOLUTE ROUGHNESS (INCH)"));
					double sumkhp1 = 0.0;
					double sumkhp2 = 0.0;
					double sumkhp = 0.0;
					double khp;
					double Qc1 = 0.0;
					double Qc2 = 0.0;
					double dirf = 0.0;
					double gama = 0.0;
					double s_theta = 0.0;
					int part = 0;

					for (int l = 0; l <= layers - 1; l++) {
						double depth = Double.parseDouble(inputfilemap.get("FromDepth" + l));
						if (depth >= injdepth) {
							break;
						} else {
							part++;
						}
					}
					double delp_cased1 = 0.0;
					if (inputfilemap.get("COMPLETION TYPE").equalsIgnoreCase("CASED HOLE")) {
						for (int k1 = 0; k1 <= (layers - 1); k1++) {
							double Q_cas = Double.parseDouble(QLTmap.get("QLTW " + k1 + " " + j + "_" + d));
							if (Q_cas > 0) {
								double n = Double.parseDouble(inputfilemap.get("NUMBER OF PERFORATIONS"));
								double perf_radius = (Double
										.parseDouble(inputfilemap.get("PERFORATION DIAMETER (INCH)")) * 0.0254) / 2;
								double delp_perf1 = (Math.pow((Q_cas * 0.00265), 2) * acid_den)
										/ (2 * Math.pow(0.9, 2) * Math.pow(3.14, 2) * Math.pow(perf_radius, 4));
								delp_cased1 = delp_perf1 * 0.000145038 * n;
								injmap.put("delpcas" + "_" + k1 + "_" + j + "_" + d, Double.toString(delp_cased1));

							} else {
								injmap.put("delpcas" + "_" + k1 + "_" + j + "_" + d, Double.toString(0));

							}
						}
					}
					// ***friction factor only**//
					double nre = (1.48 * (pumprate * 1440) * injsg * 62.427) / ((dc - dt) * Viscosity);
					if (nre > 2000) {
						double f1 = 0.026 * Math.pow((e), 0.225) + 0.133 * (e);
						double f2 = 22 * Math.pow((e), 0.44);
						double f3 = 1.62 * Math.pow((e), 0.134);
						f = f1 + (f2 * Math.pow(nre, (-f3)));
					} else if (nre < 2000) { // ***** tRANSITION NOT CONSIDERED**///
						f = 64 / nre;
					}
					// *********sumKhp*******//
					// P1//
					int iu = 0;
					if (part == 0) {
						part = -1;
					}

					// System.out.println("***********TIME"+d+"*************");
					for (iu = (part); iu >= 0; iu--) {
						if (inputfilemap.get("COMPLETION TYPE").equalsIgnoreCase("CASED HOLE")) {
							delpcas = Double.parseDouble(injmap.get("delpcas" + "_" + iu + "_" + j + "_" + d));
						}
						zonePressure = Double.parseDouble(inputfilemap.get("ZonalPressure" + iu));
						if (j == 0) {
							Perm = (d == 0) ? Double.parseDouble(inputfilemap.get("Perm" + iu))
									: Double.parseDouble(injmap.get("permu" + "_" + iu + "_" + j + "_" + (d - 1)));

						} else {
							Perm = (d == 0)
									? Double.parseDouble(
											injmap.get("permu" + "_" + iu + "_" + (j - 1) + "_" + (difftithis - 1)))
									: Double.parseDouble(injmap.get("permu" + "_" + iu + "_" + j + "_" + (d - 1)));
						}
						if (Perm > 0) {
							h = Double.parseDouble(inputfilemap.get("ToDepth" + iu))
									- Double.parseDouble(inputfilemap.get("FromDepth" + iu));
							dp_dl = (1 / (703.2 * 3.2)) * (injsg * 1000) * f
									* (Math.pow(0.0008321 * Qt1, 2) / (9.8 * Math.pow((D * 2 * 0.3048), 5)));
							pw = inputfilemap.get("COMPLETION TYPE").equalsIgnoreCase("CASED HOLE")
									? injpp - delpcas - (dp_dl * h)
									: injpp - (dp_dl * h);
							injmap.put("k_" + iu, Double.toString(pw - zonePressure));
							sumkhp1 += (Perm * h * (pw - zonePressure));
						}
					}

					// P2//
					for (iu = (part + 1); iu <= (layers - 1); iu++) {
						if (inputfilemap.get("COMPLETION TYPE").equalsIgnoreCase("CASED HOLE")) {
							delpcas = Double.parseDouble(injmap.get("delpcas" + "_" + iu + "_" + j + "_" + d));
						}
						zonePressure = Double.parseDouble(inputfilemap.get("ZonalPressure" + iu));
						if (j == 0) {
							Perm = (d == 0) ? Double.parseDouble(inputfilemap.get("Perm" + iu))
									: Double.parseDouble(injmap.get("permu" + "_" + iu + "_" + j + "_" + (d - 1)));
						} else {
							Perm = (d == 0)
									? Double.parseDouble(
											injmap.get("permu" + "_" + iu + "_" + (j - 1) + "_" + (difftithis - 1)))
									: Double.parseDouble(injmap.get("permu" + "_" + iu + "_" + j + "_" + (d - 1)));
						}
						if (Perm > 0) {
							h = Double.parseDouble(inputfilemap.get("ToDepth" + iu))
									- Double.parseDouble(inputfilemap.get("FromDepth" + iu));
							dp_dl = (1 / (703.2 * 3.2)) * (injsg * 1000) * f
									* (Math.pow(0.0008321 * Qt2, 2) / (9.8 * Math.pow((D * 2 * 0.3048), 5)));
							pw = inputfilemap.get("COMPLETION TYPE").equalsIgnoreCase("CASED HOLE")
									? injpp - delpcas - (dp_dl * h)
									: injpp - (dp_dl * h);
							injmap.put("k_" + iu, Double.toString(pw - zonePressure));
							sumkhp2 += (Perm * h * (pw - zonePressure));
						}
					}

					// sumkhp = sumkhp1 + sumkhp2;
					// ****** q1 q2********//
					// P1//
					for (int k = (part); k >= 0; k--) {
						pre_skin = Double.parseDouble(inputfilemap.get("Pre-Skin" + k));

						zonePressure = Double.parseDouble(inputfilemap.get("ZonalPressure" + k));
						if (j == 0) {
							Perm = (d == 0) ? Double.parseDouble(inputfilemap.get("Perm" + k))
									: Double.parseDouble(injmap.get("permu" + "_" + k + "_" + j + "_" + (d - 1)));
						} else {
							Perm = (d == 0)
									? Double.parseDouble(
											injmap.get("permu" + "_" + k + "_" + (j - 1) + "_" + (difftithis - 1)))
									: Double.parseDouble(injmap.get("permu" + "_" + k + "_" + j + "_" + (d - 1)));
						}
						h = Double.parseDouble(inputfilemap.get("ToDepth" + k))
								- Double.parseDouble(inputfilemap.get("FromDepth" + k));
						dp_dl = (1 / (703.2 * 3.2)) * (injsg * 1000) * f * (Math.pow((0.0008321 * Qt1), 2))
								/ (9.8 * Math.pow(D * 2 * 0.3048, 5));
						// dp_dl = ((0.0624 / 144) * 970 * 1.35e-11 * f * Math.pow(Qt1 * 8085, 2)) /
						// (Math.pow(2 * rw * 12, 5));
						pw = injpp - (dp_dl * h);
						khp = Perm * h * (pw - zonePressure);
						kH = (Perm * 10);
						double q1 = Perm > 0 ? Qt1 * khp / (sumkhp1) : 0.0;
						double q2 = 0.0;
						if (WellType.equalsIgnoreCase("SLANTED")) {
							gama = Math.pow(Math.pow(Math.cos(Math.toRadians(inclination)), 2)
									+ kh_kv * Math.pow(Math.sin(Math.toRadians(inclination)), 2), (1 / 2));
							s_theta = Math.log(
									((4 * rw * Math.cos(Math.toRadians(inclination))) / (h * gama)) * Math.sqrt(kh_kv))
									+ (Math.cos(Math.toRadians(inclination)) / gama)
											* Math.log((h / (2 * rw * (1 + Math.pow(gama, -1)))) * Math
													.sqrt((kh_kv * gama) / (Math.cos(Math.toRadians(inclination)))));
							q2 = (0.007080 * khp) / (1440 * Viscosity * Math.log(re / rw) - 0.75 + pre_skin + s_theta);
						} else {
							q2 = Perm > 0
									? (WellType.equals("HORIZONTAL")
											? (0.007080 * khp) / (1440 * Viscosity
													* ((Math.log(
															((a + Math.sqrt((Math.pow(a, 2) - Math.pow((L_h / 2), 2))))
																	/ (L_h / 2)))
															+ (((Math.sqrt(kH / Perm) * hh) / L_h) * (Math
																	.log((Math.sqrt(kH / Perm) * hh)
																			/ (rw * (Math.sqrt(kH / Perm) + 1)))
																	+ pre_skin)))))
											: (0.007080 * khp)
													/ (1440 * Viscosity * Math.log(re / rw) - 0.75 + pre_skin))
									: 0.0;// check later for the constants

						}

						Qc1 = Qtt1 - (q1 + q2);

						dirf = Qt1 - Qc1;
						injmap.put("Delp_" + k, df.format(pw - zonePressure));

						if (Qc1 > 0) {
							if (dirf <= 0.1) {
								Qt1 = Qc1;

								injmap.put("Qt_" + k + " " + j + "_" + d, df.format(Qt1));
								injmap.put("Q1_Q2" + k, df.format(q1 + q2));
								injmap.put("KHP_" + k, df.format(khp));
								Qtt1 = Qt1;
								Rlayers1 = k + 1;
							} else {
								k = k + 1;
								Qt1 = Qc1;
							}
						} else {

							injmap.put("Q1_Q2" + k, df.format(Qtt1));
							Qt1 = 0.0;
							Qtt1 = Qt1;
							injmap.put("Qt_" + k + " " + j + "_" + d, df.format(0.0));
							injmap.put("KHP_" + k, df.format(0.0));
						}

					}

					Rlayers2 = part + 1;
					// P2//

					for (int k = (part + 1); k <= (layers - 1); k++) {
						pre_skin = Double.parseDouble(inputfilemap.get("Pre-Skin" + k));

						zonePressure = Double.parseDouble(inputfilemap.get("ZonalPressure" + k));
						if (j == 0) {
							Perm = (d == 0) ? Double.parseDouble(inputfilemap.get("Perm" + k))
									: Double.parseDouble(injmap.get("permu" + "_" + k + "_" + j + "_" + (d - 1)));
						} else {
							Perm = (d == 0)
									? Double.parseDouble(
											injmap.get("permu" + "_" + k + "_" + (j - 1) + "_" + (difftithis - 1)))
									: Double.parseDouble(injmap.get("permu" + "_" + k + "_" + j + "_" + (d - 1)));
						}

						h = Double.parseDouble(inputfilemap.get("ToDepth" + k))
								- Double.parseDouble(inputfilemap.get("FromDepth" + k));
						dp_dl = (1 / (703.2 * 3.2)) * (injsg * 1000) * f * (Math.pow((0.0008321 * Qt2), 2))
								/ (9.8 * Math.pow((D / 3.28084), 5));
						pw = injpp - (dp_dl * h);
						kH = (Perm * 10);
						khp = Perm * h * (pw - zonePressure);
						// double q1 = Qt1 * khp / (sumkhp1)
						double q1 = Perm > 0 ? Qt2 * khp / (sumkhp2) : 0.0;
						double q2 = 0.0;
						if (WellType.equalsIgnoreCase("SLANTED")) {
							gama = Math.pow(Math.pow(Math.cos(Math.toRadians(inclination)), 2)
									+ kh_kv * Math.pow(Math.sin(Math.toRadians(inclination)), 2), (1 / 2));
							s_theta = Math.log(
									((4 * rw * Math.cos(Math.toRadians(inclination))) / (h * gama)) * Math.sqrt(kh_kv))
									+ (Math.cos(Math.toRadians(inclination)) / gama)
											* Math.log((h / (2 * rw * (1 + Math.pow(gama, -1)))) * Math
													.sqrt((kh_kv * gama) / (Math.cos(Math.toRadians(inclination)))));
							q2 = (0.007080 * khp) / (1440 * Viscosity * Math.log(re / rw) - 0.75 + pre_skin + s_theta);
						} else {
							q2 = Perm > 0
									? (WellType.equals("HORIZONTAL")
											? (0.007080 * khp) / (1440 * Viscosity
													* ((Math.log(
															((a + Math.sqrt((Math.pow(a, 2) - Math.pow((L_h / 2), 2))))
																	/ (L_h / 2)))
															+ (((Math.sqrt(kH / Perm) * hh) / L_h) * (Math
																	.log((Math.sqrt(kH / Perm) * hh)
																			/ (rw * (Math.sqrt(kH / Perm) + 1)))
																	+ pre_skin)))))
											: (0.007080 * khp)
													/ (1440 * Viscosity * Math.log(re / rw) - 0.75 + pre_skin))
									: 0.0;// check later for the constants
						}
						Qc2 = Qtt2 - (q1 + q2);
						dirf = Qt2 - Qc2;
						if (Qc2 > 0) {
							if (dirf <= 0.1) {
								Qt2 = Qc2;
								injmap.put("Qt_" + k + " " + j + "_" + d, df.format(Qt2));
								injmap.put("Q1_Q2" + k, df.format(q1 + q2));
								injmap.put("KHP_" + k, df.format(khp));
								injmap.put("Delp_" + k, df.format(pw - zonePressure));
								Qtt2 = Qt2;
								Rlayers2++;
							} else {
								k = k - 1;
								Qt2 = Qc2;
							}
						} else {
							injmap.put("Q1_Q2" + k, df.format(Qtt2));
							Qt2 = 0.0;
							Qtt2 = Qt2;
							injmap.put("Qt_" + k + " " + j, df.format(0.0));

							injmap.put("KHP_" + k, df.format(0.0));
							injmap.put("Delp_" + k, df.format(pw - zonePressure));
						}
					}

					// *********New sumKhp for q negative*******//
					// P1//
					sumkhp = 0.0;
					sumkhp1 = 0.0;
					sumkhp2 = 0.0;

					for (iu = (part); iu >= (Rlayers1 - 1); iu--) {
						if (inputfilemap.get("COMPLETION TYPE").equalsIgnoreCase("CASED HOLE")) {
							delpcas = Double.parseDouble(injmap.get("delpcas" + "_" + iu + "_" + j + "_" + d));
						}
						zonePressure = Double.parseDouble(inputfilemap.get("ZonalPressure" + iu));
						if (j == 0) {
							Perm = (d == 0) ? Double.parseDouble(inputfilemap.get("Perm" + iu))
									: Double.parseDouble(injmap.get("permu" + "_" + iu + "_" + j + "_" + (d - 1)));
						} else {
							Perm = (d == 0)
									? Double.parseDouble(
											injmap.get("permu" + "_" + iu + "_" + (j - 1) + "_" + (difftithis - 1)))
									: Double.parseDouble(injmap.get("permu" + "_" + iu + "_" + j + "_" + (d - 1)));
						}
						if (Perm > 0) {
							h = Double.parseDouble(inputfilemap.get("ToDepth" + iu))
									- Double.parseDouble(inputfilemap.get("FromDepth" + iu));
							dp_dl = (1 / (703.2 * 3.2)) * (injsg * 1000) * f
									* (Math.pow(0.0008321 * Qt1, 2) / (9.8 * Math.pow((D / 3.28084), 5)));
							pw = inputfilemap.get("COMPLETION TYPE").equalsIgnoreCase("CASED HOLE")
									? injpp - delpcas - (dp_dl * h)
									: injpp - (dp_dl * h);

							sumkhp1 += (Perm * h * (pw - zonePressure));
						}
					}

					// P2//
					for (iu = (part + 1); iu <= (Rlayers2 - 1); iu++) {
						if (inputfilemap.get("COMPLETION TYPE").equalsIgnoreCase("CASED HOLE")) {
							delpcas = Double.parseDouble(injmap.get("delpcas" + "_" + iu + "_" + j + "_" + d));
						}
						zonePressure = Double.parseDouble(inputfilemap.get("ZonalPressure" + iu));
						if (j == 0) {
							Perm = (d == 0) ? Double.parseDouble(inputfilemap.get("Perm" + iu))
									: Double.parseDouble(injmap.get("permu" + "_" + iu + "_" + j + "_" + (d - 1)));
						} else {
							Perm = (d == 0)
									? Double.parseDouble(
											injmap.get("permu" + "_" + iu + "_" + (j - 1) + "_" + (difftithis - 1)))
									: Double.parseDouble(injmap.get("permu" + "_" + iu + "_" + j + "_" + (d - 1)));
						}
						if (Perm > 0) {
							h = Double.parseDouble(inputfilemap.get("ToDepth" + iu))
									- Double.parseDouble(inputfilemap.get("FromDepth" + iu));
							dp_dl = (1 / (703.2 * 3.2)) * (injsg * 1000) * f
									* (Math.pow(0.0008321 * Qt2, 2) / (9.8 * Math.pow((D / 3.28084), 5)));
							pw = inputfilemap.get("COMPLETION TYPE").equalsIgnoreCase("CASED HOLE")
									? injpp - delpcas - (dp_dl * h)
									: injpp - (dp_dl * h);

							sumkhp2 += (Perm * h * (pw - zonePressure));
						}
					}

					sumkhp = sumkhp1 + sumkhp2;
					if (dtype.equalsIgnoreCase("FOAMED")) {
						if (stage.equalsIgnoreCase("DIVERTER")) {
							WithDiverter22.Foam(inputfilemap, injmap, injmap, injmap, injmap, Rlayers1, j, d, sumkhp,
									Rlayers2, part);
						}
					}
					for (int k = 0; k <= (layers - 1); k++) {
						double rwh = 0.0;
						double rwwc = 0.0;
						double q3 = 0.0;
						double Poro = 0.0;
						pre_skin = Double.parseDouble(inputfilemap.get("Pre-Skin" + k));
						if (j == 0) {
							Perm = (d == 0) ? Double.parseDouble(inputfilemap.get("Perm" + k))
									: Double.parseDouble(injmap.get("permu" + "_" + k + "_" + j + "_" + (d - 1)));
							double kd = Perm / ((pre_skin / (Math.log(drw / rwwc))) + 1);
							double pd = ((Perm - kd) / Perm) * 100;
							Poro = Double.parseDouble(inputfilemap.get("Poro" + k));
							if (d == 0) {
								Poro = Poro - (Poro * (pd / 100));
							}
						} else {
							Perm = (d == 0)
									? Double.parseDouble(
											injmap.get("permu" + "_" + k + "_" + (j - 1) + "_" + (difftithis - 1)))
									: Double.parseDouble(injmap.get("permu" + "_" + k + "_" + j + "_" + (d - 1)));
							Poro = Double.parseDouble(inputfilemap.get("Poro" + k));
						}
						if (Perm > 0) {
							h = Double.parseDouble(inputfilemap.get("ToDepth" + k))
									- Double.parseDouble(inputfilemap.get("FromDepth" + k));

							if (part == layers - 1) {
								q3 = (Double.parseDouble(injmap.get("Qt_" + (Rlayers1 - 1) + " " + j + "_" + d)))
										* (Double.parseDouble(injmap.get("KHP_" + k))) / sumkhp;

							} else if (part == -1) {
								q3 = (Double.parseDouble(injmap.get("Qt_" + (Rlayers2 - 1) + " " + j + "_" + d)))
										* (Double.parseDouble(injmap.get("KHP_" + k))) / sumkhp;
							} else {
								q3 = (Double.parseDouble(injmap.get("Qt_" + (Rlayers1 - 1) + " " + j + "_" + d))
										+ Double.parseDouble(injmap.get("Qt_" + (Rlayers2 - 1) + " " + j + "_" + d)))
										* (Double.parseDouble(injmap.get("KHP_" + k))) / sumkhp;
							}
							qlt = (Double.parseDouble(injmap.get("Q1_Q2" + k))) + q3;
							if (j != 0) {
								rwwc = (d == 0)
										? Double.parseDouble(
												injmap.get("Penet" + k + "_" + (j - 1) + "_" + (difftithis - 1)))
										: Double.parseDouble(injmap.get("Penet" + k + "_" + j + "_" + (d - 1)));
							}
							/* Fluid Loss Calculation */
							// double lossdelp = (f_bhp - zonePressure) * 6894.76;
							double beta1 = (Pconu * 100.1) / (7300);
							double r2_p = (2 * 3.14 * beta1 * cr * Math.pow(1, (0.5))) / (1 - Poro);
							double p2 = Math.pow((pore_radius * 0.000001), 2);
							double r = (-(-r2_p) + Math.sqrt(Math.pow(r2_p, 2) - 4 * 1 * (-p2))) / 2;
							double qloss = ((2 * 3.14 * r * cr) / (Math.pow(1, 0.5))) / 0.00265;
							/**/
							qlt = qlt - qloss;

							if (qlt > 0) {
								injmap.put("QLT " + k + " " + j + "_" + d, Double.toString(qlt));
								if (cases.equalsIgnoreCase("Shearthinning")) {
									// rwh =
									// Double.parseDouble(ShearthinngPenetration.get(inputfilemap.get("FromDepth" +
									// j)));
								} else if (stage.equalsIgnoreCase("ACID")) {
									if (j == 0) {
										qlt = (d == 0) ? qlt
												: qlt + Double
														.parseDouble(injmap.get("QLT " + k + " " + j + "_" + (d - 1)));
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
											double difftithisps_ = Double
													.parseDouble(inputfilemap.get("Duration" + ps));
											int difftithisps = (int) difftithisps_;
											qlt1 = Double.parseDouble(
													injmap.get("QLT " + k + " " + ps + "_" + (difftithisps - 1)));
											if (qlt1 == 0) {
												int j1 = j;
												int d1 = 0;
												if (d == 0) {
													j1 = ps;
												} else {
													d1 = d;
												}
												for (int s11 = j1; s11 >= 0; s11--) {
													double difftithis1_ = Double
															.parseDouble(inputfilemap.get("Duration" + s11));
													int difftithis1 = (int) difftithis1_;
													if (d1 > 0) {
														difftithis1 = d1;
													}
													for (int d11 = difftithis1; d11 > 0; d11--) {
														if (d11 == 1) {
															d1 = 0;
														}
														for (int l11 = k; l11 >= k; l11--) {
															qlt1 = Double.parseDouble(injmap
																	.get("QLT " + l11 + " " + s11 + "_" + (d11 - 1)));
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
											qlt = (d == 0) ? qlt + qlt1
													: qlt + Double.parseDouble(
															injmap.get("QLT " + k + " " + j + "_" + (d - 1)));
										} else {

											qlt = (d == 0) ? qlt + 0.0
													: qlt + Double.parseDouble(
															injmap.get("QLT " + k + " " + j + "_" + (d - 1)));

										}
									}
									if (acidtype.equalsIgnoreCase("EMULSIFIED ACID")) {
										rwh = PenetrationAcidCarbonate.emulsified_Acid(inputfilemap, j, k, Poro, qlt,
												Pconu, Viscosity, injmap, injmap, d);
									} else {
										rwh = PenetrationAcidCarbonate.penetration(inputfilemap, k, j, qlt, Pconu,
												injmap, injmap, d);
									}
								} else if (stage.equalsIgnoreCase("DIVERTER") || stage.equalsIgnoreCase("OVERFLUSH")) {
									if (j == 0) {
										rwh = 0.0;
									} else {
										rwh = (d == 0)
												? Double.parseDouble(injmap
														.get("Penet" + k + "_" + (j - 1) + "_" + (difftithis - 1)))
												: Double.parseDouble(injmap.get("Penet" + k + "_" + j + "_" + (d - 1)));
									}
								}
								injmap.put("QLT " + k + " " + j + "_" + d, Double.toString(qlt));
								injmap.put("Penet" + k + "_" + j + "_" + d, df.format(rwh));
								if (stage.equalsIgnoreCase("ACID")) {
									double qlt1 = 0.0;
									qlt1 = (d == 0) ? qlt
											: qlt + Double
													.parseDouble(injmap.get("QLT " + k + " " + j + "_" + (d - 1)));
									ks = (stage.equalsIgnoreCase("PREFLUSH"))
											? Double.parseDouble(inputfilemap.get("Perm" + k))
											: StimulatedPermeability.kstim(inputfilemap, qlt1, k, j, Perm, rwh, rwwc,
													injmap, Viscosity, injmap, d);
									injmap.put("permu" + "_" + k + "_" + j + "_" + d, df.format(ks));
								} else if (stage.equalsIgnoreCase("DIVERTER")) {

								} else if (stage.equalsIgnoreCase("PREFLUSH")) {
									ks = Double.parseDouble(inputfilemap.get("Perm" + k));
									injmap.put("permu" + "_" + k + "_" + j + "_" + d, df.format(ks));
									injmap.put("skin" + "_" + k + "_" + j + "_" + d, Double.toString(pre_skin));
									injmap.put("Penet" + k + "_" + j + "_" + d, df.format(0.0));
								} else if (stage.equalsIgnoreCase("OVERFLUSH")) {
									ks = Double.parseDouble(
											injmap.get("permu" + "_" + k + "_" + (j - 1) + "_" + (difftithis - 1)));
									injmap.put("permu" + "_" + k + "_" + j + "_" + d, df.format(ks));
									injmap.put("skin" + "_" + k + "_" + j + "_" + d,
											injmap.get("skin" + "_" + k + "_" + (j - 1) + "_" + (difftithis - 1)));
								}
							} else {
								injmap.put("QLT " + k + " " + j + "_" + d, df.format(0));
								if (j == 0) {
									rwh = 0;
									injmap.put("Penet" + k + "_" + j + "_" + d, df.format(0));
									if (!stage.equalsIgnoreCase("DIVERTER")) {
										if (d == 0) {
											injmap.put("skin" + "_" + k + "_" + j + "_" + d, Double.toString(pre_skin));
										} else {
											injmap.put("skin" + "_" + k + "_" + j + "_" + d,
													injmap.get("skin" + "_" + k + "_" + j + "_" + (d - 1)));
										}
										ks = (d == 0) ? Double.parseDouble(inputfilemap.get("Perm" + k))
												: Double.parseDouble(
														injmap.get("permu" + "_" + k + "_" + j + "_" + (d - 1)));
										injmap.put("permu" + "_" + k + "_" + j + "_" + d, df.format(ks));
									}

								} else {
									if (!stage.equalsIgnoreCase("DIVERTER")) {
										ks = (d == 0)
												? Double.parseDouble(injmap.get(
														"permu" + "_" + k + "_" + (j - 1) + "_" + (difftithis - 1)))
												: Double.parseDouble(
														injmap.get("permu" + "_" + k + "_" + j + "_" + (d - 1)));
										injmap.put("permu" + "_" + k + "_" + j + "_" + d, df.format(ks));
										if (d == 0) {
											injmap.put("skin" + "_" + k + "_" + j + "_" + d, injmap
													.get("skin" + "_" + k + "_" + (j - 1) + "_" + (difftithis - 1)));
										} else {
											injmap.put("skin" + "_" + k + "_" + j + "_" + d,
													injmap.get("skin" + "_" + k + "_" + j + "_" + (d - 1)));
										}

									}
									rwh = (d == 0)
											? Double.parseDouble(
													injmap.get("Penet" + k + "_" + (j - 1) + "_" + (difftithis - 1)))
											: Double.parseDouble(injmap.get("Penet" + k + "_" + j + "_" + (d - 1)));
									injmap.put("Penet" + k + "_" + j + "_" + d, Double.toString(rwh));

								}
							}

							Poro = Double.parseDouble(inputfilemap.get("Poro" + k));
							injmap.put("porou" + "_" + k + "_" + j + "_" + d, df.format(Poro));
						} else {
							injmap.put("QLT " + k + " " + j + "_" + d, df.format(0));
							if (j == 0) {
								if (!stage.equalsIgnoreCase("DIVERTER")) {
									ks = (d == 0) ? Double.parseDouble(inputfilemap.get("Perm" + k))
											: Double.parseDouble(
													injmap.get("permu" + "_" + k + "_" + j + "_" + (d - 1)));
									injmap.put("permu" + "_" + k + "_" + j + "_" + d, df.format(ks));
									if (d == 0) {
										injmap.put("skin" + "_" + k + "_" + j + "_" + d, Double.toString(pre_skin));
									} else {
										injmap.put("skin" + "_" + k + "_" + j + "_" + d,
												injmap.get("skin" + "_" + k + "_" + j + "_" + (d - 1)));
									}
								}
								injmap.put("Penet" + k + "_" + j + "_" + d, df.format(0));

							} else {
								if (!stage.equalsIgnoreCase("DIVERTER")) {
									ks = (d == 0)
											? Double.parseDouble(injmap
													.get("permu" + "_" + k + "_" + (j - 1) + "_" + (difftithis - 1)))
											: Double.parseDouble(
													injmap.get("permu" + "_" + k + "_" + j + "_" + (d - 1)));
									injmap.put("permu" + "_" + k + "_" + j + "_" + d, df.format(ks));
									if (d == 0) {
										injmap.put("skin" + "_" + k + "_" + j + "_" + d,
												injmap.get("skin" + "_" + k + "_" + (j - 1) + "_" + (difftithis - 1)));
									} else {
										injmap.put("skin" + "_" + k + "_" + j + "_" + d,
												injmap.get("skin" + "_" + k + "_" + j + "_" + (d - 1)));
									}
								}
								rwh = (d == 0)
										? Double.parseDouble(
												injmap.get("Penet" + k + "_" + (j - 1) + "_" + (difftithis - 1)))
										: Double.parseDouble(injmap.get("Penet" + k + "_" + j + "_" + (d - 1)));
								injmap.put("Penet" + k + "_" + j + "_" + d, Double.toString(rwh));
							}
							Poro = j == 0 ? Double.parseDouble(inputfilemap.get("Poro" + k))
									: ((d == 0)
											? Double.parseDouble(injmap
													.get("porou" + "_" + k + "_" + (j - 1) + "_" + (difftithis - 1)))
											: Double.parseDouble(
													injmap.get("porou" + "_" + k + "_" + j + "_" + (d - 1))));
							injmap.put("porou" + "_" + k + "_" + j + "_" + d, df.format(Poro));

						}
						
						
					}

				}
				  if(j>1) {
					  int delj=j-1;
					  double deltime=Double.parseDouble(inputfilemap.get("Duration"+delj));
					  for(int dtim=0;dtim<((int)deltime);dtim++) {
						  for(int dellayers=0;dellayers<layers;dellayers++) {
							  injmap.remove("permu" + "_" + dellayers + "_" + delj + "_" + dellayers);  
							  injmap.remove("porou" + "_" + dellayers + "_" + delj + "_" + dellayers);  
							  
						  }						  
					  }
					 					  	  
				  }
			}

			Map<String, String> qltMap = new TreeMap<String, String>();
			Map<String, String> penetLengMap = new TreeMap<String, String>();

			double qlttotal = 0;
			stages = Integer.parseInt(inputfilemap.get("Stages"));
			int wstages = Integer.parseInt(injmap.get("Stages"));
			int asc = 0;
			for (int js = 0; js < wstages; js++) {
				String stage1 = injmap.get("Stage" + js);
				if (stage1.equals("ACID")) {
					int lastv = as.get(asc);
					as.remove(asc);
					as.add(asc, lastv - 1);
					for (int j = 0; j <= (stages - 1); j++) {
						double penet = 0;
						String stage = inputfilemap.get("Stage" + j);
						if (stage.equals("ACID")) {
							double durations = Double.parseDouble(inputfilemap.get("Duration" + j));
							for (int l = 0; l <= (layers - 1); l++) {
								for (int d = 0; d < durations; d++) {
									penet = Double.parseDouble(injmap.get("Penet" + l + "_" + j + "_" + d));
								}
								if (as.get(asc) == j) {
									penetrationlengthplot.put("penet" + "_" + js + "_" + l, df.format(penet));
								}
							}
						}
					}
					asc++;
				}

			}

			for (int l = 0; l <= (layers - 1); l++) {
				double penet = 0;
				double qltsum = 0;
				for (int j = 0; j <= (stages - 1); j++) {
					String stage = inputfilemap.get("Stage" + j);
					double durations = Double.parseDouble(inputfilemap.get("Duration" + j));
					if (stage.equals("ACID")) {
						for (int d = 0; d < durations; d++) {
							qltsum += Double.parseDouble(injmap.get("QLT " + l + " " + j + "_" + d));
							qlttotal += Double.parseDouble(injmap.get("QLT " + l + " " + j + "_" + d));
							penet = Double.parseDouble(injmap.get("Penet" + l + "_" + j + "_" + d));
						}
					}
				}
				qltMap.put("qlt" + "_" + l, df.format(qltsum));
				penetLengMap.put(inputfilemap.get("FromDepth" + l), df.format(penet));
			}
			int ll = 0;
			for (int l = 0; l <= (layers - 1); l++) {
				double percentVol = 0;
				double ql = Double.parseDouble(qltMap.get("qlt" + "_" + l));
				percentVol = (ql * 100) / qlttotal;
				volumepercentage.put(inputfilemap.get("FromDepth" + ll), df.format(percentVol));
				ll++;
			}
			System.gc();
			/* Skin and PI calculation */
			SkinPI sp = new SkinPI();
			sp.skinPi(inputfilemap, penetLengMap, injmap, productivityindex, skincalculationplot, foldmap);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
