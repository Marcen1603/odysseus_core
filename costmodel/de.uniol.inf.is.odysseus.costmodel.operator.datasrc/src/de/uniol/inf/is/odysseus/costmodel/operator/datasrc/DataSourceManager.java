package de.uniol.inf.is.odysseus.costmodel.operator.datasrc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.OdysseusDefaults;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.AttributeObserver;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.DataSourceObserverSink;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFSchema;

/**
 * Verwaltet die Beobachtung der Datenquellen. Dabei handelt es sich um eine
 * Singleton-Klasse. Sie liefert zu gegebenen Attribute die Histogramme.
 * 
 * @author Timo Michelsen
 * 
 */
public class DataSourceManager {

	private static Logger _logger = null;

	protected static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(DataSourceManager.class);
		}
		return _logger;
	}

	// contains additional info
	// of the used sources in odysseus
	// (internal use only!)
	private class SourceInfo {
		public int queriesUsed = 1;
		public DataSourceObserverSink<?> observer;

		@SuppressWarnings({ "unchecked", "rawtypes" })
		public SourceInfo(ISource<?> source) {
			this.observer = new DataSourceObserverSink(source);
		}
	}

	private static DataSourceManager instance = null;
	private static final String FILENAME = "ac_attributes.conf";

	private Map<ISource<?>, SourceInfo> sources = new HashMap<ISource<?>, SourceInfo>();
	private Map<SDFAttribute, AttributeObserver> attributes = new HashMap<SDFAttribute, AttributeObserver>();
	private List<SDFAttribute> waitingAttributes = new ArrayList<SDFAttribute>();
	private Map<String, Collection<Double>> cachedAttributes = new HashMap<String, Collection<Double>>();

	private DataSourceManager() {

	}

	/**
	 * Liefert die einzige Instanz dieser Klasse.
	 * 
	 * @return Einzige Instanz der Klasse
	 */
	public static DataSourceManager getInstance() {
		if (instance == null)
			instance = new DataSourceManager();
		return instance;
	}

	/**
	 * Fügt eine neue Datenquelle hinzu, die beobachtet werden soll
	 * 
	 * @param src
	 *            Neue zu beobachtende Datenquelle
	 */
	public void addSource(ISource<?> src) {
		if (!sources.containsKey(src)) {
			SourceInfo info = new SourceInfo(src);
			sources.put(src, info);

			getLogger().debug("New source added: " + src);

			if (!waitingAttributes.isEmpty()) {
				SDFSchema attributes = src.getOutputSchema();
				List<SDFAttribute> toRemove = new ArrayList<SDFAttribute>();
				for (SDFAttribute waitingAttribute : waitingAttributes) {
					if (attributes.contains(waitingAttribute)) {
						// create observer for this attribute, since
						// it was asked in the past
						createAttributeObserver(waitingAttribute);
						toRemove.add(waitingAttribute);
					}
				}

				// clear list of waiting attributes
				for (SDFAttribute a : toRemove) {
					waitingAttributes.remove(a);
				}
			}

			// cached attribute with temporary attributeobservers
			// to connect to this new source?
			for (SDFAttribute attribute : src.getOutputSchema()) {
				if (attributes.containsKey(attribute)) {
					AttributeObserver observer = attributes.get(attribute);
					if (!observer.hasSink())
						observer.setSink(info.observer);
				}
			}

		} else {
			SourceInfo info = sources.get(src);
			info.queriesUsed++;

			getLogger().debug("Source " + src + " already added. Uses: " + info.queriesUsed);
		}
	}

	/**
	 * Entfernt eine Datenquelle von der Beobachtung
	 * 
	 * @param src
	 *            Zu entfernende Datenquelle
	 */
	public void removeSource(ISource<?> src) {
		if (sources.containsKey(src)) {
			SourceInfo srcInfo = sources.get(src);
			srcInfo.queriesUsed--;

			if (srcInfo.queriesUsed == 0) {
				// disconnect from source
				// --> do not observe this source anymore!
				srcInfo.observer.disconnect();

				// source no longer used
				sources.remove(src);
				getLogger().debug("Source " + src + " removed");
			}

			getLogger().debug("Source " + src + " uses : " + srcInfo.queriesUsed);

		} else {
			// sollte nicht vorkommen
			getLogger().warn("Query with source " + src + "(which was not known here) removed");
		}
	}

	/**
	 * Liefert zum gegebenen Attribut ein Histogramm.
	 * 
	 * @param attribute
	 *            Attribut, dessen Histogramm benötigt wird
	 * @return Histogramm des Attribut. Liefert <code>null</code>, falls noch
	 *         kein Histogramm erstellt werden konnte
	 */
	public IHistogram getHistogram(SDFAttribute attribute) {

		if (attributes.containsKey(attribute)) {
			AttributeObserver observer = attributes.get(attribute);
			return observer.getHistogram();
		} else if (cachedAttributes.containsKey(attribute.toString())) {

			getLogger().debug("Cached values of " + attribute + " found");

			Collection<Double> values = cachedAttributes.get(attribute.toString());

			AttributeObserver observer = new AttributeObserver(attribute);
			attributes.put(attribute, observer);
			getLogger().debug("Created temporary attributeObserver for " + attribute);

			for (Double d : values)
				observer.streamElementRecieved(null, d, 0);

			return observer.getHistogram();

		} else {
			getLogger().debug("No histogram for attribute " + attribute + " exists");

			createAttributeObserver(attribute);
		}

		return null;
	}

	/**
	 * Liefert die Menge aktuell beobachteter Datenquellen
	 * 
	 * @return Menge der Datenquellen, die aktuell beobachtet werden.
	 */
	public Set<ISource<?>> getSources() {
		return sources.keySet();
	}

	private void createAttributeObserver(SDFAttribute attribute) {

		// only numerical attributes!
		if (attribute.getDatatype().isNumeric()) {

			DataSourceObserverSink<?> sink = findSink(attribute);
			if (sink != null) {
				AttributeObserver observer = attributes.get(attribute);
				if (observer == null) {
					observer = new AttributeObserver(attribute);

					observer.setSink(sink);

					attributes.put(attribute, observer);
					getLogger().debug("Added AttributeObserver for attribute " + attribute);
				} else {
					observer.setSink(sink);
					getLogger().debug("Activated temporary AttributeObserver for attribute " + attribute);
				}

			} else {
				getLogger().warn("Source for attribute " + attribute + " not found. Cannot create observer now.");
				waitingAttributes.add(attribute);
			}
		} else {
			getLogger().warn("Attribute " + attribute + " is not numerical");
		}
	}

	private DataSourceObserverSink<?> findSink(SDFAttribute attribute) {
		for (SourceInfo sourceInfo : sources.values()) {
			SDFSchema attributeList = sourceInfo.observer.getOutputSchema();
			if (attributeList.contains(attribute))
				return sourceInfo.observer;
		}

		return null;
	}

	/**
	 * Speichert die aktuellen Histogramme aller Histogramme in einer
	 * Konfigurationsdatei.
	 */
	public void save() {
		String filename = OdysseusDefaults.getHomeDir() + FILENAME;
		System.out.println("Writing file " + filename);
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(filename));

			for (SDFAttribute attribute : attributes.keySet()) {
				System.out.print("Writing values of " + attribute);

				AttributeObserver observer = attributes.get(attribute);
				Collection<Double> values = observer.getValues();

				System.out.println(" Count: " + values.size());

				bw.write(attribute.toString() + "\n");
				bw.write(String.valueOf(values.size()) + "\n");
				for (Double v : values) {
					bw.write(String.valueOf(v) + "\n");
				}
				bw.flush();

			}

			for (String attrName : cachedAttributes.keySet()) {

				boolean found = false;
				for (SDFAttribute attr : attributes.keySet()) {
					if (attr.toString().equals(attrName)) {
						found = true;
						break;
					}
				}
				if (found)
					continue;

				System.out.print("Writing cached values of " + attrName);

				Collection<Double> values = cachedAttributes.get(attrName);

				System.out.println(" Count: " + values.size());

				bw.write(attrName + "\n");
				bw.write(String.valueOf(values.size()) + "\n");
				for (Double v : values) {
					bw.write(String.valueOf(v) + "\n");
				}
				bw.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bw != null)
				try {
					bw.flush();
					bw.close();
					System.out.println("Finished");
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	/**
	 * Läd alle Histogramme aus der Konfigurationsdatei.
	 */
	public void load() {
		String filename = OdysseusDefaults.getHomeDir() + FILENAME;
		System.out.println("Reading file " + filename);

		BufferedReader bw = null;
		try {
			bw = new BufferedReader(new FileReader(filename));

			while (true) {
				String attribute = bw.readLine();
				if (attribute == null)
					return;
				System.out.print("Reading values of " + attribute);

				String count = bw.readLine();
				System.out.println(" Count: " + count);
				if (count == null)
					return;
				int size = Integer.valueOf(count);

				List<Double> values = new ArrayList<Double>(size);
				for (int i = 0; i < size; i++) {
					String valueString = bw.readLine();
					if (valueString != null) {
						Double d = Double.valueOf(valueString);
						values.add(d);
					} else {
						return;
					}
				}

				cachedAttributes.put(attribute, values);
			}

		} catch (FileNotFoundException ex) {
			File f = new File(filename);
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("File " + filename + " created");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bw != null)
				try {
					bw.close();
					System.out.println("Finished");
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

	}

}
