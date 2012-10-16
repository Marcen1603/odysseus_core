/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.costmodel.operator.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;

/**
 * Singleton-Klasse, welcher die Aufgabe hat, die Verarbeitungszeiten der
 * physischen Operatoren persistent auf der Festplatte zu speichern, beim
 * Starten zu laden und anderen Klassen zur Verfügung zu stellen.
 * 
 * @author Timo Michelsen
 * 
 */
public class CPURateSaver {

	private static CPURateSaver instance = null;
	private static final String FILENAME = "ac_cpurates.conf";

	private Map<String, Double> cpuRates = new HashMap<String, Double>();

	private CPURateSaver() {
	}

	/**
	 * Liefert die einzige Instanz der Klasse {@link CPURateSaver}.
	 * 
	 * @return Einzige Instanz
	 */
	public static CPURateSaver getInstance() {
		if (instance == null)
			instance = new CPURateSaver();
		return instance;
	}

	/**
	 * Läd alle Verarbeitungszeiten aus der Konfigurationsdatei. Existiert die
	 * Daten nicht, wird sie erstellt.
	 */
	public void load() {
		String filename = OdysseusConfiguration.getHomeDir() + FILENAME;
		// System.out.println("Loading CPURates from " + filename);

		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));

			String line = br.readLine();
			while (line != null) {

				String[] parts = line.split("\\=");

				Double d = new Double(parts[1]);
				cpuRates.put(parts[0], d);
				// System.out.println("CPURate of " + parts[0] + ":" + d);

				line = br.readLine();
			}
			
			br.close();
		} catch (FileNotFoundException ex) {
			File file = new File(filename);
			try {
				file.createNewFile();
				// System.out.println("New cfg-File created: " + filename);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Setzt die Verarbeitungszeit für den gegebenen Operatornamen auf den
	 * gegebenen Wert. Beim nächsten Speichern wird dieser persistent
	 * festgehalten. Existierte zuvor kein Wert für diesen Operator, wird ein
	 * neuer Eintrag angelegt.
	 * 
	 * @param opName
	 *            Name/Typ des Operators, dessen Verarbeitungszeit gesetzt
	 *            werden soll.
	 * @param cpurate
	 *            Neue Verarbeitungszeit.
	 */
	public void set(String opName, double cpurate) {
		cpuRates.put(opName, new Double(cpurate));

		// System.out.println("Setting cpurate of " + streamName + " to " +
		// cpurate);
	}

	/**
	 * Liefert die gespeicherte Verarbeitungszeit für den gegebenen
	 * Operatornamen. Es sollte sichergestellt werden, dass zuvor
	 * <code>load()</code> aufgerufen wurde, sodass die Werte aus der
	 * Konfigurationsdatei verfügbar sind. Ist zu einem Operatornamen kein Wert
	 * gespeichert, wir der empirische Wert 0.00005 zurückgegeben.
	 * 
	 * @param opName
	 *            Operatorname/Typ, dessen Verarbeitungszeit zurückgegeben
	 *            werden soll
	 * 
	 * @return Gespeicherte Verarbeitungszeit des gegebenen Operators
	 */
	public double get(String opName) {
		if (!cpuRates.containsKey(opName))
			return 0.00005; // empiric

		return cpuRates.get(opName);
	}

	/**
	 * Speichert alle hinterlegten Verarbeitungszeiten in der
	 * Konfigurationsdatei.
	 */
	public void save() {
		String filename = OdysseusConfiguration.getHomeDir() + FILENAME;
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(filename));

			for (String str : cpuRates.keySet()) {
				Double rate = cpuRates.get(str);
				bw.write(str + "=" + rate + "\n");
				bw.flush();
			}

			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
