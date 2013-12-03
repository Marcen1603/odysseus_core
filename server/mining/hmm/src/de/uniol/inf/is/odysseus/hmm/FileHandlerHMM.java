package de.uniol.inf.is.odysseus.hmm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Handles any file related operations like saving or loading.
 * 
 * @author Michael Möbes
 * @author Christian Pieper
 * 
 */
public class FileHandlerHMM {

	/**
	 * Loads existing training data from file.
	 * 
	 * @param gesture
	 *            Gesture name to load.
	 */
	public static ArrayList<int[]> loadTrainingData(String path) {
		File f = new File(path);
		ArrayList<int[]> obsSequences = new ArrayList<int[]>();
		if (f.exists()) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(f));
				String line;
				while ((line = br.readLine()) != null) {
					String[] lineSplit = line.split(",");
					if (lineSplit.length != 0) {
						int[] tmp = new int[lineSplit.length];
						for (int i = 0; i < lineSplit.length; i++) {
							tmp[i] = Integer.parseInt(lineSplit[i].trim());
						}
						obsSequences.add(tmp);
					}
				}
				br.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return obsSequences;
	}

	/**
	 * Prints out training observations.
	 * 
	 * @param obsSequences
	 *            Training observations.
	 */
	public void printTrainingObservations(ArrayList<int[]> obsSequences) {
		for (int i = 0; i < obsSequences.size(); i++) {
			for (int j = 0; j < obsSequences.get(i).length; j++) {
				System.out.print(obsSequences.get(i)[j] + " ");
			}
			System.out.println();
		}
	}

	/**
	 * Deletes referenced file.
	 * 
	 * @param filePath
	 *            Path including filename and file extension
	 * @return True if deleted, false otherwise
	 */
	public static boolean deleteFile(String filePath) {
		File f = new File(filePath);
		return f.delete();
	}

	/**
	 * Appends new observation sequence to training data. Creates new file of
	 * not existent yet
	 * 
	 * @param observation
	 *            Observation sequence to append
	 * @param filePath
	 *            Path including filename and file extension
	 * @throws IOException
	 */
	public static void appendNewTrainingData(ArrayList<Integer> observation, String filePath) throws IOException {
		File f = new File(filePath);
		// Create if file doesn't exist yet.
		boolean existed = true;
		if (!f.exists()) {
			System.err.println(f.getAbsolutePath());
			f.createNewFile();
			existed = false;
		}
		
		// Append new observation sequence
		OutputStreamWriter writer = new OutputStreamWriter(
				new FileOutputStream(f, true), "UTF-8");
		BufferedWriter wr = new BufferedWriter(writer);
		if (existed) {
			wr.newLine();
		}
		
		for (int i = 0; i < observation.size(); i++) {
			if (i == observation.size() - 1) {
				wr.append(observation.get(i).toString());
			} else {
				wr.append(observation.get(i).toString() + ", ");
			}
		}
		wr.close();
	}

	/**
	 * Save HMM config to file
	 * 
	 * @param gesture
	 *            HMM config to save
	 * @param pathToConfigfiles
	 *            Path to files
	 */
	public static void saveHMMConfigToFile(Gesture gesture,
			String pathToConfigfiles) {
		// Delete old HMM config file if already existent
		String path = pathToConfigfiles + gesture.getName() + ".csv";
		FileHandlerHMM.deleteFile(path);

		// save to new file
		File f = new File(path);
		try {
			f.createNewFile();
			BufferedWriter wr = new BufferedWriter(new FileWriter(f));
			wr.append("# KEEP FILE STRUCTURE\n#\n");
			wr.append("# Metadata\n");
			wr.append(gesture.getNumStates() + ", " + HMM.observationLength	+ ", " + gesture.getNumMinObs() + ", "	+ gesture.getNumMaxObs() + "\n");
			// Append Matrix Pi
			wr.append("#\n# Matrix Pi\n");
			for (int i = 0; i < gesture.getPi().length; i++) {
				if (i == gesture.getPi().length - 1) {
					wr.append(gesture.getPi()[i] + "\n#\n");
				} else {
					wr.append(gesture.getPi()[i] + ", ");
				}
			}
			// Append Matrix A
			wr.append("# Matrix A\n");
			for (int i = 0; i < gesture.getA().length; i++) {
				for (int j = 0; j < gesture.getA()[i].length; j++) {
					if (j == gesture.getA()[i].length - 1) {
						wr.append(gesture.getA()[i][j] + "\n");
					} else {
						wr.append(gesture.getA()[i][j] + ", ");
					}
				}
			}
			wr.append("#\n");
			// Append Matrix B
			wr.append("# Matrix B\n");
			for (int i = 0; i < gesture.getB().length; i++) {
				for (int j = 0; j < gesture.getB()[i].length; j++) {
					if (j == gesture.getB()[i].length - 1) {
						wr.append(gesture.getB()[i][j] + "\n");
					} else {
						wr.append(gesture.getB()[i][j] + ", ");
					}
				}
			}
			wr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<Gesture> loadHMMConfigFromFile(String dirPath) {
		double[] pi = null;
		double[][] a = null;
		double[][] b = null;
		int numStates = 0;
		int numObs = 0;
		int numMinObs = 0;
		int numMaxObs = 0;
		ArrayList<Gesture> gesturelist = new ArrayList<Gesture>();

		String path = dirPath;
		File dir = new File(path);
		File[] fileList = dir.listFiles();
		for (File f : fileList) {
			if (!f.isDirectory()) {
				System.out.println(f.getName().substring(0,
						f.getName().length() - 4));
				try {
					BufferedReader br = new BufferedReader(new FileReader(f));
					String line;
					while ((line = br.readLine()) != null) {
						if (line.charAt(0) == '#') {
							// Read metadata
							if (line.contains("Metadata")) {
								line = br.readLine();
								String[] lineSplit = line.split(",");
								numStates = Integer.parseInt(lineSplit[0].trim());
								numObs = Integer.parseInt(lineSplit[1].trim());
								numMinObs = Integer.parseInt(lineSplit[2].trim());
								numMaxObs = Integer.parseInt(lineSplit[3].trim());
								
								// init arrays
								pi = new double[numStates];
								a = new double[numStates][numStates];
								b = new double[numStates][numObs];

								// Read Pi
							} else if (line.contains("Matrix Pi")) {
								line = br.readLine();
								String[] lineSplit = line.split(",");

								for (int i = 0; i < lineSplit.length; i++) {
									pi[i] = Double.parseDouble(lineSplit[i]);
								}

								// Read A
							} else if (line.contains("Matrix A")) {
								int i = 0;
								while ((line = br.readLine()) != null
										&& line.charAt(0) != '#') {
									String[] lineSplit = line.split(",");
									for (int j = 0; j < lineSplit.length; j++) {
										a[i][j] = Double.parseDouble(lineSplit[j]);
									}
									i++;
								}

								// Read B
							} else if (line.contains("Matrix B")) {
								int i = 0;
								while ((line = br.readLine()) != null
										&& line.charAt(0) != '#') {
									String[] lineSplit = line.split(",");
									for (int j = 0; j < lineSplit.length; j++) {
										b[i][j] = Double.parseDouble(lineSplit[j]);
									}
									i++;
								}
							}
						}
					}
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.err.println("BAD FILE");
				}

				String tmpName = f.getName().substring(0, f.getName().length() - 4);
				System.out.println("mach mal" + tmpName);
				Gesture newGesture = new Gesture(pi, a, b);
				newGesture.setName(tmpName);
				newGesture.setNumMinObs(numMinObs);
				newGesture.setNumMaxObs(numMaxObs);
				gesturelist.add(newGesture);
			}
			System.out.println("-------");
			// for (int i = 0; i < pi.length; i++) {
			// System.out.print(pi[i] + " ");
			// }
			// System.out.println("\n");
			// for (int i = 0; i < numStates; i++) {
			// for (double zahl : a[i]) {
			// System.out.print(zahl + " ");
			// }
			// System.out.println();
			// }
			// System.out.println("\n");
			// for (int i = 0; i < numStates; i++) {
			// for(double zahl : b[i]) {
			// System.out.print(zahl + " ");
			// }
			// System.out.println();
			// }

		}

		return gesturelist;
	}

}
