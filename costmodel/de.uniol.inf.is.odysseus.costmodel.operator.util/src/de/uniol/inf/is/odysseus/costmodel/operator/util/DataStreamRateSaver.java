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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.OdysseusDefaults;

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

	private static Logger _logger = null;

	protected static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(DataStreamRateSaver.class);
		}
		return _logger;
	}

	private static DataStreamRateSaver instance = null;
	private static final String FILENAME = "ac_datarates.conf";

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
		String filename = OdysseusDefaults.getHomeDir() + FILENAME;
		getLogger().debug("Loading datarates from " + filename);

		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));

			String line = br.readLine();
			while (line != null) {

				String[] parts = line.split("\\=");

				Double d = new Double(parts[1]);
				datarates.put(parts[0], d);
				getLogger().debug("Datarate of " + parts[0] + ":" + d);

				line = br.readLine();
			}
		} catch (FileNotFoundException ex) {
			File file = new File(filename);
			try {
				file.createNewFile();
				getLogger().debug("New cfg-File created: " + filename);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
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
		getLogger().debug("Setting datarate of " + streamName + " to " + datarate);
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
		String filename = OdysseusDefaults.getHomeDir() + FILENAME;
		getLogger().debug("Saving datarates in " + filename);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(filename));

			for (String str : datarates.keySet()) {
				Double rate = datarates.get(str);
				bw.write(str + "=" + rate + "\n");
				bw.flush();

				getLogger().debug("Writing " + str + "=" + rate);
			}

			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
