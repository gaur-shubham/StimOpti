package com.petroleumsoft.stimopti.algos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
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
 * @author wasee
 */
public class Bullhead {
	public void bullhead(Map<String, String> penetLengMap, Map<String, String> volumepercentage,
			Map<String, String> productivityindex, Map<String, String> skincalculationplot) {
		try {
			Map<String, String> inputfilemap = new LinkedHashMap<String, String>();
			/* Reading Input File */
			File file = ResourceUtils.getFile("classpath:config/Acid_Carbonate_Input.txt");
			// File file = new File("Acid_Carbonate_Input.txt");
			BufferedReader br = null;
			String line = "";
			br = new BufferedReader(new FileReader(file));
			while ((line = br.readLine()) != null) {
				if (line.startsWith("/***")) {
					continue;
				} else {
					int index = line.indexOf('=');
					String key = line.substring(0, index);
					String value = line.substring(index + 1);
					inputfilemap.put(key, value.trim());
				}
			}
			br.close();
			/* Input File Reading Process Completed */

			/* Algorithms Coding Starts from Here */

			String cases = "Base";
			String dtype = inputfilemap.get("DIVERTER TYPE");
			/* Cased Hole */
			Map<String, String> QLTmap = new LinkedHashMap<String, String>();
			if (inputfilemap.get("COMPLETION TYPE").equalsIgnoreCase("CASED HOLE")) {
				Cased_Q csq = new Cased_Q();
				csq.casedDelpBH(inputfilemap, cases, dtype, QLTmap);
			}
			/* End */
			double Viscosity = 0.0;
			int layers = Integer.parseInt(inputfilemap.get("Layers"));
			DecimalFormat df = new DecimalFormat("0.0000");
			double rw = Double.parseDouble(inputfilemap.get("WELLBORE RADIUS (FT)"));
			double re = Double.parseDouble(inputfilemap.get("DRAINAGE RADIUS (FT)"));
			double acid_den = Double.parseDouble(inputfilemap.get("Specific Gravity")) * 1000;
			int stages = Integer.parseInt(inputfilemap.get("Stages"));
			double dp_dl = 0.0;
			double Perm = 0.0;
			double zonePressure;
			double h;
			double pw;
			double qlt = 0.0;
			double ks = 0.0;
			int part = 0;
			double pumprate = 0;
			double a = 0;
			int Rlayers2 = 0;
			double kH = 0;
			double L_h = 0.0;
			// double hh = Double.parseDouble(inputfilemap.get("DISTANCE OF RESERVOIR BOTTOM
			// TO HORIZONTAL WELL (FT)"));
			double hh = 1.0;
			double drw = Double.parseDouble(inputfilemap.get("DAMAGE RADIUS (FT)"));
			double Pconu = 0.0;

			double cr = Double.parseDouble(inputfilemap.get("LEAK-OFF CO-EFFICIENT (FT/MIN^1/2)")) * 0.039;
			double pre_skin = 0.0;
			String WellType = inputfilemap.get("WELL TYPE");
			if (WellType.equals("HORIZONTAL")) {
				L_h = Double.parseDouble(inputfilemap.get("LENGTH OF HORIZONTAL REACH OF WELL (FT)"));
				a = (L_h / 2) * Math.sqrt(0.5 + Math.sqrt((0.25 + Math.pow(re / (L_h / 2), 4))));

			}

			Map<String, String> Qtube = new TreeMap<String, String>();
			Map<String, String> kmap = new TreeMap<String, String>();
			Map<String, String> QLT = new TreeMap<String, String>();
			Map<String, String> KHP = new TreeMap<String, String>();
			Map<String, String> Q1_Q2 = new TreeMap<String, String>();
			Map<String, String> Penetration = new TreeMap<String, String>();
			Map<String, String> qltp = new TreeMap<String, String>();
			Map<String, String> pwmap = new TreeMap<String, String>();
			Map<String, String> delpcasmap1 = new TreeMap<String, String>();
			double diffti = 0.0;
			for (int j = 0; j <= (stages - 1); j++) {
				String stage = inputfilemap.get("Stage" + j);
				System.out.println(stage);
				double difftithis_ = (j == 0) ? Double.parseDouble(inputfilemap.get("Duration" + j))
						: Double.parseDouble(inputfilemap.get("Duration" + (j - 1)));
				int difftithis = (int) difftithis_;
				diffti = Double.parseDouble(inputfilemap.get("Duration" + j));
				if (!dtype.equalsIgnoreCase("FOAMED")) {
					if (stage.equalsIgnoreCase("DIVERTER")) {
						WithDiverter22.Diverter(inputfilemap, kmap, j, Penetration, dtype, QLT);
					}
				}

				/// *********Step 1 ***** percent volume*****///
				for (int d = 0; d < diffti; d++) {
					// System.out.println("");
					if (cases.equalsIgnoreCase("Base")) {
						Viscosity = Double.parseDouble(inputfilemap.get("Viscosity (cP)"));
						pumprate = Double.parseDouble(inputfilemap.get("Pumprate" + j));
						Pconu = Double.parseDouble(inputfilemap.get("Concentration (%)"));
					}


					double Qt = 0.0;
					Qt = pumprate;
					double Qtt = pumprate;
					String acidtype = inputfilemap.get("ACID TYPE");
					double pore_radius = Double.parseDouble(inputfilemap.get("PORE RADIUS (MICRO-M)"));
					double injsg = Double.parseDouble(inputfilemap.get("INJECTION FLUID SPECIFIC GRAVITY"));
					double inclination = Double.parseDouble(inputfilemap.get("WELL INCLINATION (0-90)"));
					// double kh_kv = Double.parseDouble(inputfilemap.get("KH/KV RATIO"));
					double kh_kv = 1.0;
					double injpp = Double.parseDouble(inputfilemap.get("Bhp" + j));
					double dt = Double.parseDouble(inputfilemap.get("PRODUCTION TUBING DIAMETER (INCH)"));
					// double dc = Double.parseDouble(inputfilemap.get("CASING INNER DIAMETER
					// (INCH)"));
					double dc = 0.0;
					double D = dt;
					double e = Double.parseDouble(inputfilemap.get("ABSOLUTE ROUGHNESS (INCH)"));
					double sumkhp = 0.0;
					double khp;
					double Qc = 0.0;
					double s_theta = 0.0;
					double gama = 0.0;
					double dirf = 0.0;
					double f = 0.0;

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
								delpcasmap1.put("delpcas" + "_" + k1 + "_" + j + "_" + d, Double.toString(delp_cased1));

							} else {
								delpcasmap1.put("delpcas" + "_" + k1 + "_" + j + "_" + d, Double.toString(0));

							}
						}
					}

					double delpcas = 0.0;
					for (int iu = 0; iu <= (layers - 1); iu++) {
						if (inputfilemap.get("COMPLETION TYPE").equalsIgnoreCase("CASED HOLE")) {
							delpcas = Double.parseDouble(delpcasmap1.get("delpcas" + "_" + iu + "_" + j + "_" + d));
						}
						zonePressure = Double.parseDouble(inputfilemap.get("ZonalPressure" + iu));
						if (j == 0) {
							Perm = (d == 0) ? Double.parseDouble(inputfilemap.get("Perm" + iu))
									: Double.parseDouble(kmap.get("permu" + "_" + iu + "_" + j + "_" + (d - 1)));
						} else {
							Perm = (d == 0)
									? Double.parseDouble(
											kmap.get("permu" + "_" + iu + "_" + (j - 1) + "_" + (difftithis - 1)))
									: Double.parseDouble(kmap.get("permu" + "_" + iu + "_" + j + "_" + (d - 1)));
						}
						if (Perm > 0) {
							h = Double.parseDouble(inputfilemap.get("ToDepth" + iu))
									- Double.parseDouble(inputfilemap.get("FromDepth" + iu));
							dp_dl = ((1 / (703.2 * 3.2)) * (injsg * 1000) * f * (Math.pow(0.0008321 * Qt, 2))
									/ (9.8 * Math.pow((D * 2 * 0.3048), 5)));
							pw = inputfilemap.get("COMPLETION TYPE").equalsIgnoreCase("CASED HOLE")
									? injpp - delpcas - (dp_dl * h)
									: injpp - (dp_dl * h);
							pwmap.put("k_" + iu, Double.toString(pw - zonePressure));
							sumkhp += (Perm * h * (pw - zonePressure));
						}
					}
					int Rlayers = 0;

					for (int k = 0; k <= (layers - 1); k++) {
						pre_skin = Double.parseDouble(inputfilemap.get("Pre-Skin" + k));
						zonePressure = Double.parseDouble(inputfilemap.get("ZonalPressure" + k));
						if (j == 0) {
							Perm = (d == 0) ? Double.parseDouble(inputfilemap.get("Perm" + k))
									: Double.parseDouble(kmap.get("permu" + "_" + k + "_" + j + "_" + (d - 1)));
						} else {
							Perm = (d == 0)
									? Double.parseDouble(
											kmap.get("permu" + "_" + k + "_" + (j - 1) + "_" + (difftithis - 1)))
									: Double.parseDouble(kmap.get("permu" + "_" + k + "_" + j + "_" + (d - 1)));
						}

						kH = (Perm * 10);
						h = Double.parseDouble(inputfilemap.get("ToDepth" + k))
								- Double.parseDouble(inputfilemap.get("FromDepth" + k));
						dp_dl = ((1 / (703.2 * 3.2)) * (injsg * 1000) * f * (Math.pow((0.0008321 * Qt), 2)))
								/ (9.8 * Math.pow((D / 3.28084), 5));
						pw = injpp - (dp_dl * h);// + (0.433 * injsg * tvd);
						khp = Perm * h * (pw - zonePressure);
						double q1 = Perm > 0 ? Qt * khp / (sumkhp) : 0.0;
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
					// ****New sumkhp for q negative*****//
					sumkhp = 0.0;

					for (int iu = 0; iu <= (Rlayers - 1); iu++) {
						if (inputfilemap.get("COMPLETION TYPE").equalsIgnoreCase("CASED HOLE")) {
							delpcas = Double.parseDouble(delpcasmap1.get("delpcas" + "_" + iu + "_" + j + "_" + d));
						}
						zonePressure = Double.parseDouble(inputfilemap.get("ZonalPressure" + iu));
						if (j == 0) {
							Perm = (d == 0) ? Double.parseDouble(inputfilemap.get("Perm" + iu))
									: Double.parseDouble(kmap.get("permu" + "_" + iu + "_" + j + "_" + (d - 1)));
						} else {
							Perm = (d == 0)
									? Double.parseDouble(
											kmap.get("permu" + "_" + iu + "_" + (j - 1) + "_" + (difftithis - 1)))
									: Double.parseDouble(kmap.get("permu" + "_" + iu + "_" + j + "_" + (d - 1)));
						}
						if (Perm > 0) {
							h = Double.parseDouble(inputfilemap.get("ToDepth" + iu))
									- Double.parseDouble(inputfilemap.get("FromDepth" + iu));
							dp_dl = ((1 / (703.2 * 3.2)) * (injsg * 1000) * f * (Math.pow(0.0008321 * Qt, 2))
									/ (9.8 * Math.pow((D * 2 * 0.3048), 5)));
							pw = inputfilemap.get("COMPLETION TYPE").equalsIgnoreCase("CASED HOLE")
									? injpp - delpcas - (dp_dl * h)
									: injpp - (dp_dl * h);
							sumkhp += (Perm * h * (pw - zonePressure));
						}
					}
					if (dtype.equalsIgnoreCase("FOAMED")) {
						if (stage.equalsIgnoreCase("DIVERTER")) {
							WithDiverter22.Foam(inputfilemap, kmap, Qtube, Q1_Q2, KHP, Rlayers, j, d, sumkhp, Rlayers2,
									part);
						}
					}
					for (int k = 0; k <= (layers - 1); k++) {

						double rwh = 0.0;
						double rwwc = 0.0;
						double Poro = 0.0;
						pre_skin = Double.parseDouble(inputfilemap.get("Pre-Skin" + k));

						if (j == 0) {
							Perm = (d == 0) ? Double.parseDouble(inputfilemap.get("Perm" + k))
									: Double.parseDouble(kmap.get("permu" + "_" + k + "_" + j + "_" + (d - 1)));
							double kd = Perm / ((pre_skin / (Math.log(drw / rwwc))) + 1);
							double pd = ((Perm - kd) / Perm) * 100;
							Poro = Double.parseDouble(inputfilemap.get("Poro" + k));
							if (d == 0) {
								Poro = Poro - (Poro * (pd / 100));
							}
						} else {
							Perm = (d == 0)
									? Double.parseDouble(
											kmap.get("permu" + "_" + k + "_" + (j - 1) + "_" + (difftithis - 1)))
									: Double.parseDouble(kmap.get("permu" + "_" + k + "_" + j + "_" + (d - 1)));
							// Poro = (d == 0) ? Double.parseDouble(kmap.get("porou" + "_" + k + "_" + (j -
							// 1) + "_" + (difftithis - 1))) : Double.parseDouble(kmap.get("porou" + "_" + k
							// + "_" + j + "_" + (d - 1)));
							Poro = Double.parseDouble(inputfilemap.get("Poro" + k));
						}

						if (Perm > 0) {
							if (Rlayers == 0) {
								Rlayers = 1;
							}
							double q3 = (Double.parseDouble(Qtube.get("Qt_" + (Rlayers - 1) + " " + j + "_" + d)))
									* (Double.parseDouble(KHP.get("KHP_" + k)) / sumkhp);
							qlt = (Double.parseDouble(Q1_Q2.get("Q1_Q2" + k))) + q3;

							if (j != 0) {
								rwwc = (d == 0)
										? Double.parseDouble(
												Penetration.get("Penet" + k + "_" + (j - 1) + "_" + (difftithis - 1)))
										: Double.parseDouble(Penetration.get("Penet" + k + "_" + j + "_" + (d - 1)));
							}
							/* Fluid Loss Calculation */
							// double lossdelp = (f_bhp - zonePressure) * 6894.76;
							double beta1 = (Pconu * 100.1) / (7300);
							double r2_p = (2 * 3.14 * beta1 * cr * Math.pow(1, (1 / 2))) / (1 - Poro);
							double p2 = Math.pow((pore_radius * 0.000001), 2);
							double r = (-(-r2_p) + Math.sqrt(Math.pow(r2_p, 2) - 4 * 1 * (-p2))) / 2;
							double qloss = ((2 * 3.14 * r * cr) / (Math.pow(1, 0.5))) / 0.00265;
							/**/

							qlt = qlt + qloss;
							if (qlt > 0) {
								QLT.put("QLTW " + k + " " + j + "_" + d, df.format(qlt));
								QLT.put("QLT " + k + " " + j + "_" + d, df.format(qlt));
								if (stage.equalsIgnoreCase("ACID")) {
									if (j == 0) {
										qlt = (d == 0) ? qlt
												: qlt + Double
														.parseDouble(qltp.get("QLT " + k + " " + j + "_" + (d - 1)));
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
													qltp.get("QLT " + k + " " + ps + "_" + (difftithisps - 1)));
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
															qlt1 = Double.parseDouble(qltp
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
															qltp.get("QLT " + k + " " + j + "_" + (d - 1)));
										} else {
											qlt = (d == 0) ? qlt + 0.0
													: qlt + Double.parseDouble(
															qltp.get("QLT " + k + " " + j + "_" + (d - 1)));
										}
									}
									if (acidtype.equals("EMULSIFIED")) {
										rwh = PenetrationAcidCarbonate.emulsified_Acid(inputfilemap, j, k, Poro, qlt,
												Pconu, Viscosity, Penetration, kmap, d);
									} else {
										rwh = PenetrationAcidCarbonate.penetration(inputfilemap, k, j, qlt, Pconu,
												Penetration, kmap, d);
									}

								} else if (stage.equalsIgnoreCase("OVERFLUSH")) {
									if (j == 0) {
										rwh = 0.0;
									} else {
										rwh = (d == 0)
												? Double.parseDouble(Penetration
														.get("Penet" + k + "_" + (j - 1) + "_" + (difftithis - 1)))
												: Double.parseDouble(
														Penetration.get("Penet" + k + "_" + j + "_" + (d - 1)));
									}
								}

								qltp.put("QLT " + k + " " + j + "_" + d, df.format(qlt));
								if (stage.equalsIgnoreCase("ACID")) {
									ks = StimulatedPermeability.kstim(inputfilemap, qlt, k, j, Perm, rwh, rwwc, KHP,
											Viscosity, kmap, d);
									kmap.put("permu" + "_" + k + "_" + j + "_" + d, df.format(ks));
								} else if (stage.equalsIgnoreCase("DIVERTER")) {
									if (j == 0) {
										rwh = 0.0;
									} else {
										rwh = (d == 0)
												? Double.parseDouble(Penetration
														.get("Penet" + k + "_" + (j - 1) + "_" + (difftithis - 1)))
												: Double.parseDouble(
														Penetration.get("Penet" + k + "_" + j + "_" + (d - 1)));
									}
								} else if (stage.equalsIgnoreCase("PREFLUSH")) {
									rwh = 0.0;
									ks = Double.parseDouble(inputfilemap.get("Perm" + k));
									kmap.put("permu" + "_" + k + "_" + j + "_" + d, df.format(ks));
									kmap.put("skin" + "_" + k + "_" + j + "_" + d, Double.toString(pre_skin));
									kmap.put("skinw" + "_" + k + "_" + j + "_" + d, Double.toString(pre_skin));
								} else if (stage.equalsIgnoreCase("OVERFLUSH")) {
									ks = Double.parseDouble(
											kmap.get("permu" + "_" + k + "_" + (j - 1) + "_" + (difftithis - 1)));
									kmap.put("permu" + "_" + k + "_" + j + "_" + d, df.format(ks));
									kmap.put("skin" + "_" + k + "_" + j + "_" + d,
											kmap.get("skin" + "_" + k + "_" + (j - 1) + "_" + (difftithis - 1)));
									kmap.put("skinw" + "_" + k + "_" + j + "_" + d,
											kmap.get("skinw" + "_" + k + "_" + (j - 1) + "_" + (difftithis - 1)));
								}

								Penetration.put("Penet" + k + "_" + j + "_" + d, df.format(rwh));
							} else {

								QLT.put("QLT " + k + " " + j + "_" + d, df.format(0));
								qltp.put("QLT " + k + " " + j + "_" + d, df.format(0));
								QLT.put("QLTW " + k + " " + j + "_" + d, df.format(0));
								if (j == 0) {
									if (!stage.equalsIgnoreCase("DIVERTER")) {
										ks = (d == 0) ? Double.parseDouble(inputfilemap.get("PermMD" + k))
												: Double.parseDouble(
														kmap.get("permu" + "_" + k + "_" + j + "_" + (d - 1)));
										kmap.put("permu" + "_" + k + "_" + j + "_" + d, df.format(ks));
										if (d == 0) {
											kmap.put("skin" + "_" + k + "_" + j + "_" + d, Double.toString(pre_skin));
											kmap.put("skinw" + "_" + k + "_" + j + "_" + d, Double.toString(0));
										} else {
											kmap.put("skin" + "_" + k + "_" + j + "_" + d,
													kmap.get("skin" + "_" + k + "_" + j + "_" + (d - 1)));
											kmap.put("skinw" + "_" + k + "_" + j + "_" + d, Double.toString(0));
										}
									}
									rwh = 0;
									Penetration.put("Penet" + k + "_" + j + "_" + d, df.format(0));

								} else {
									if (!stage.equalsIgnoreCase("DIVERTER")) {
										ks = (d == 0)
												? Double.parseDouble(kmap.get(
														"permu" + "_" + k + "_" + (j - 1) + "_" + (difftithis - 1)))
												: Double.parseDouble(
														kmap.get("permu" + "_" + k + "_" + j + "_" + (d - 1)));
										kmap.put("permu" + "_" + k + "_" + j + "_" + d, df.format(ks));
										kmap.put("skin" + "_" + k + "_" + j + "_" + d,
												kmap.get("skin" + "_" + k + "_" + (j - 1) + "_" + (difftithis - 1)));
										kmap.put("skinw" + "_" + k + "_" + j + "_" + d, Double.toString(0));
									}
									rwh = (d == 0)
											? Double.parseDouble(Penetration
													.get("Penet" + k + "_" + (j - 1) + "_" + (difftithis - 1)))
											: Double.parseDouble(
													Penetration.get("Penet" + k + "_" + j + "_" + (d - 1)));
									Penetration.put("Penet" + k + "_" + j + "_" + d, Double.toString(rwh));

								}
							}
							kmap.put("porou" + "_" + k + "_" + j + "_" + d, df.format(Poro));
						} else {

							QLT.put("QLT " + k + " " + j + "_" + d, df.format(0));
							QLT.put("QLTW " + k + " " + j + "_" + d, df.format(0));
							qltp.put("QLT " + k + " " + j + "_" + d, df.format(0));
							if (j == 0) {
								if (!stage.equalsIgnoreCase("DIVERTER")) {
									if (d == 0) {
										kmap.put("skin" + "_" + k + "_" + j + "_" + d, Double.toString(pre_skin));
									} else {
										kmap.put("skin" + "_" + k + "_" + j + "_" + d,
												kmap.get("skin" + "_" + k + "_" + j + "_" + (d - 1)));
									}
									ks = (d == 0) ? Double.parseDouble(inputfilemap.get("PermMD" + k))
											: Double.parseDouble(kmap.get("permu" + "_" + k + "_" + j + "_" + (d - 1)));
									kmap.put("permu" + "_" + k + "_" + j + "_" + d, df.format(ks));
									kmap.put("skinw" + "_" + k + "_" + j + "_" + d, Double.toString(0));
								}
								Penetration.put("Penet" + k + "_" + j + "_" + d, df.format(0));
							} else {
								if (!stage.equalsIgnoreCase("DIVERTER")) {
									kmap.put("skinw" + "_" + k + "_" + j + "_" + d, Double.toString(0));
									if (d == 0) {
										kmap.put("skin" + "_" + k + "_" + j + "_" + d,
												kmap.get("skin" + "_" + k + "_" + (j - 1) + "_" + (difftithis - 1)));
									} else {
										kmap.put("skin" + "_" + k + "_" + j + "_" + d,
												kmap.get("skin" + "_" + k + "_" + j + "_" + (d - 1)));
									}
									ks = (d == 0)
											? Double.parseDouble(kmap
													.get("permu" + "_" + k + "_" + (j - 1) + "_" + (difftithis - 1)))
											: Double.parseDouble(kmap.get("permu" + "_" + k + "_" + j + "_" + (d - 1)));
									kmap.put("permu" + "_" + k + "_" + j + "_" + d, df.format(ks));
								}
								rwh = (d == 0)
										? Double.parseDouble(
												Penetration.get("Penet" + k + "_" + (j - 1) + "_" + (difftithis - 1)))
										: Double.parseDouble(Penetration.get("Penet" + k + "_" + j + "_" + (d - 1)));
								Penetration.put("Penet" + k + "_" + j + "_" + d, Double.toString(rwh));
							}
							if (j == 0) {
								Poro = (d == 0) ? Double.parseDouble(inputfilemap.get("Poro" + k))
										: Double.parseDouble(kmap.get("porou" + "_" + k + "_" + j + "_" + (d - 1)));
							} else {
								Poro = (d == 0)
										? Double.parseDouble(
												kmap.get("porou" + "_" + k + "_" + (j - 1) + "_" + (difftithis - 1)))
										: Double.parseDouble(kmap.get("porou" + "_" + k + "_" + j + "_" + (d - 1)));
							}
							kmap.put("porou" + "_" + k + "_" + j + "_" + d, df.format(Poro));
						}

					}
					System.gc();
				}
			}

			Map<String, String> qltMap = new TreeMap<String, String>();
			Map<String, String> penetrationlengthplot = new TreeMap<String, String>();
			Map<String, String> foldmap = new TreeMap<String, String>();

			double qlttotal = 0;

			for (int j = 0; j <= (stages - 1); j++) {
				double penet = 0;
				for (int l = 0; l <= (layers - 1); l++) {
					String stage = inputfilemap.get("Stage" + j);
					double durations = Double.parseDouble(inputfilemap.get("Duration" + j));
					if (stage.equals("ACID")) {
						for (int d = 0; d < durations; d++) {
							penet = Double.parseDouble(Penetration.get("Penet" + l + "_" + j + "_" + d));
						}
						penetrationlengthplot.put("penet" + "_" + j + "_" + l, df.format(penet));
					}

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
							qltsum += Double.parseDouble(QLT.get("QLT " + l + " " + j + "_" + d));
							qlttotal += Double.parseDouble(QLT.get("QLT " + l + " " + j + "_" + d));
							penet = Double.parseDouble(Penetration.get("Penet" + l + "_" + j + "_" + d));
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
			Qtube.clear();
			KHP.clear();
			Q1_Q2.clear();
			Penetration.clear();
			qltp.clear();
			pwmap.clear();
			delpcasmap1.clear();
			System.gc();
			/* Calculating Skin PI */
			SkinPI sp = new SkinPI();
			sp.skinPi(inputfilemap, penetLengMap, kmap, productivityindex, skincalculationplot, foldmap);
			/* Skin PI Calculation Ends Here */
			/*
			 * Writing Output File BufferedWriter writer = null; writer = new
			 * BufferedWriter(new
			 * FileWriter("src/main/resources/config/Acid_Carbonate_output.txt")); //TODO
			 */

			System.out.println("Calculation Done For Pumping Type Bullheading .");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}
