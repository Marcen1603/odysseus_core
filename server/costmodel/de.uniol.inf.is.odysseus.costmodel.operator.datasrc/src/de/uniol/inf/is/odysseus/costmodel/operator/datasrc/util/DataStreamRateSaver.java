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
package de.uniol.inf.is.odysseus.costmodel.operator.datasrc.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;

/**
 * Singleton-Klasse, welcher die Aufgabe besitzt, zu verschiedenen Datenquellen
 * die Datenrate zu speichern, zu laden und anderen Klassen zur Verfügung zu
 * stellen. Die ZUordnung ist zwischen Name der Datenquelle (bspw. nexmark:bid2)
 * und der Datenrate in Tupel pro Sekunde (bspw. 5)
 * 
 * @author Timo Michelsen
 * 
 */
public class DataStreamRateSaver {

	private static final Logger LOG = LoggerFactory.getLogger(DataStreamRateSaver.class);
	private static final String FILENAME = "ac_datarates.conf";

	private static DataStreamRateSaver instance = null;

	private Map<String, Double> datarates = new HashMap<String, Double>();

	private DataStreamRateSaver() {
	}

	/**
	 * Liefert die einzige Instanz dieser Klasse zurück.
	 * 
	 * @return Einzige Instanz der Klasse {@link DataStreamRateSaver}
	 */
	public static DataStreamRateSaver getInstance() {
		if (instance == null)
			instance = new DataStreamRateSaver();
		return instance;
	}

	/**
	 * Läd alle Datenraten der verschiedenen Datenquellen aus der
	 * Konfigurationsdatei. Falls die Datei nicht existiert, wird sie neu
	 * angelegt.
	 */
	public void load() {
		String filename = OdysseusConfiguration.getHomeDir() + FILENAME;
		LOG.debug("Loading datarates from " + filename);

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filename));

			String line = br.readLine();
			while (line != null) {

				String[] parts = line.split("\\=");

				Double d = new Double(parts[1]);
				datarates.put(parts[0], d);
				LOG.debug("Datarate of " + parts[0] + ":" + d);

				line = br.readLine();
			} 
		} catch (FileNotFoundException ex) {
			File file = new File(filename);
			try {
				file.createNewFile();
				LOG.debug("New cfg-File created: " + filename);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			LOG.error("Could not load data stream rates", e);
		} finally {
			tryClose(br);
		}
	}

	/**
	 * Setzt die Datenrate der gegebenen Datenquelle auf den gegebenen Wert.
	 * Existierte die Datenquelle bisher nicht in der Konfigurationsdatei, wird
	 * ein neuer Eintrag angelegt.
	 * 
	 * @param streamName
	 *            Name der Datenquelle
	 * @param datarate
	 *            Neue Datenrate der Datenquelle
	 */
	public void set(String streamName, double datarate) {
		datarates.put(streamName, new Double(datarate));
		LOG.debug("Setting datarate of " + streamName + " to " + datarate);
	}

	/**
	 * Liefert die zuletzt gespeicherte Datenrate der gegeben Datenquelle.
	 * Existiert zur gegebenen Datenquelle kein Eintrag, wird 1 zurückgegeben.
	 * 
	 * @param streamName
	 *            Name der Datenquelle, dessen Datenrate zurückgegeben werden
	 *            soll.
	 * 
	 * @return Datenrate der gegebenen Datenquelle, oder 1, falls die
	 *         Datenquelle nicht bekannt ist.
	 */
	public double get(String streamName) {
		if (!datarates.containsKey(streamName))
			return 1.0;

		return datarates.get(streamName);
	}

	/**
	 * Speichert alle Datenraten und die korrespondierenden Datenquellennamen
	 * in der Konfigurationsdatei, sodass sie beim nächsten Start der Applikation
	 * geladen werden können.
	 */
	public void save() {
		String filename = OdysseusConfiguration.getHomeDir() + FILENAME;
		LOG.debug("Saving datarates in {}", filename);
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(filename));

			for (String str : datarates.keySet()) {
				Double rate = datarates.get(str);
				bw.write(str + "=" + rate + "\n");
				bw.flush();

				LOG.debug("Writing {} = {}", str, rate);
			}

			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			tryClose(bw);
		}
	}

	private static void tryClose(BufferedWriter bw) {
		try {
			if( bw != null ) {
				bw.close();
			}
		} catch (IOException ex) {
			LOG.debug("Could not close writer", ex);
		}
	}

	private static void tryClose(BufferedReader br) {
		try {
			if( br != null ) {
				br.close();
			}
		} catch (IOException ex) {
			LOG.debug("Could not close reader", ex);
		}
	}
}
