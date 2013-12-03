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

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.AttributeObserver;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.DataSourceObserverSink;

/**
 * Verwaltet die Beobachtung der Datenquellen. Dabei handelt es sich um eine
 * Singleton-Klasse. Sie liefert zu gegebenen Attribute die Histogramme.
 * 
 * @author Timo Michelsen
 * 
 */
public class DataSourceManager {

	// contains additional info
	// of the used sources in odysseus
	// (internal use only!)
	private class SourceInfo {
		public int queriesUsed = 1;
		public DataSourceObserverSink observer;

		public SourceInfo(ISource<? extends IStreamObject<?>> source) {
			this.observer = new DataSourceObserverSink(source);
		}
	}

	private static final Logger LOG = LoggerFactory.getLogger(DataSourceManager.class);

	private static DataSourceManager instance = null;
	private static final String FILENAME = "ac_attributes.conf";

	private final Map<ISource<?>, SourceInfo> sources = new HashMap<ISource<?>, SourceInfo>();
	private final Map<SDFAttribute, AttributeObserver> attributes = new HashMap<SDFAttribute, AttributeObserver>();
	private final List<SDFAttribute> waitingAttributes = new ArrayList<SDFAttribute>();
	private final Map<String, Collection<Double>> cachedAttributes = new HashMap<String, Collection<Double>>();

	private boolean useHistograms = true;

	private DataSourceManager() {
		useHistograms = OdysseusConfiguration.getBoolean("ac_operator_useHistograms", true);
	}

	/**
	 * F�gt eine neue Datenquelle hinzu, die beobachtet werden soll
	 * 
	 * @param src
	 *            Neue zu beobachtende Datenquelle
	 */
	public void addSource(ISource<? extends IStreamObject<?>> src) {
		if (!useHistograms) {
			return;
		}

		if (!sources.containsKey(src)) {
			final SourceInfo info = new SourceInfo(src);
			sources.put(src, info);

			LOG.debug("New source added: " + src);

			if (!waitingAttributes.isEmpty()) {
				final SDFSchema attributes = src.getOutputSchema();
				final List<SDFAttribute> toRemove = new ArrayList<SDFAttribute>();
				for (final SDFAttribute waitingAttribute : waitingAttributes) {
					if (attributes.contains(waitingAttribute)) {
						// create observer for this attribute, since
						// it was asked in the past
						createAttributeObserver(waitingAttribute);
						toRemove.add(waitingAttribute);
					}
				}

				// clear list of waiting attributes
				for (final SDFAttribute a : toRemove) {
					waitingAttributes.remove(a);
				}
			}

			// cached attribute with temporary attributeobservers
			// to connect to this new source?
			for (final SDFAttribute attribute : src.getOutputSchema()) {
				if (attributes.containsKey(attribute)) {
					final AttributeObserver observer = attributes.get(attribute);
					if (!observer.hasSink()) {
						observer.setSink(info.observer);
					}
				}
			}

		} else {
			final SourceInfo info = sources.get(src);
			info.queriesUsed++;

			LOG.debug("Source " + src + " already added. Uses: " + info.queriesUsed);
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
		if (!isUseHistograms()) {
			return null;
		}

		if (attributes.containsKey(attribute)) {
			final AttributeObserver observer = attributes.get(attribute);
			return observer.getHistogram();
		} else if (cachedAttributes.containsKey(attribute.toString())) {

			LOG.debug("Cached values of " + attribute + " found");

			final Collection<Double> values = cachedAttributes.get(attribute.toString());

			final AttributeObserver observer = new AttributeObserver(attribute);
			attributes.put(attribute, observer);
			LOG.debug("Created temporary attributeObserver for " + attribute);

			for (final Double d : values) {
				observer.cachedElementRecieved(d);
			}

			return observer.getHistogram();

		} else {
			LOG.debug("No histogram for attribute " + attribute + " exists");

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

	public boolean isUseHistograms() {
		return useHistograms;
	}

	/**
	 * Läd alle Histogramme aus der Konfigurationsdatei.
	 */
	public void load() {
		if (!isUseHistograms()) {
			return;
		}

		final String filename = OdysseusConfiguration.getHomeDir() + FILENAME;
		LOG.debug("Reading file " + filename);

		BufferedReader bw = null;
		try {
			bw = new BufferedReader(new FileReader(filename));

			while (true) {
				final String attribute = bw.readLine();
				if (attribute == null) {
					return;
				}
				LOG.debug("Reading values of " + attribute);

				final String count = bw.readLine();
				LOG.debug(" Count: " + count);
				if (count == null) {
					return;
				}
				final int size = Integer.valueOf(count);

				final List<Double> values = new ArrayList<Double>(size);
				for (int i = 0; i < size; i++) {
					final String valueString = bw.readLine();
					if (valueString != null) {
						final Double d = Double.valueOf(valueString);
						values.add(d);
					} else {
						return;
					}
				}

				cachedAttributes.put(attribute, values);
			}

		} catch (final FileNotFoundException ex) {
			final File f = new File(filename);
			try {
				f.createNewFile();
			} catch (final IOException e) {
				LOG.error("Could not create new file {}", filename, e);
			}
			LOG.debug("File " + filename + " created");
		} catch (final IOException e) {
			LOG.error("Could not load file {}", filename, e);
		} finally {
			if (bw != null) {
				try {
					bw.close();
					LOG.debug("Finished");
				} catch (final IOException e) {
				}
			}
		}

	}

	/**
	 * Entfernt eine Datenquelle von der Beobachtung
	 * 
	 * @param src
	 *            Zu entfernende Datenquelle
	 */
	public void removeSource(ISource<?> src) {
		if (!isUseHistograms()) {
			return;
		}

		if (sources.containsKey(src)) {
			final SourceInfo srcInfo = sources.get(src);
			srcInfo.queriesUsed--;

			if (srcInfo.queriesUsed == 0) {
				// disconnect from source
				// --> do not observe this source anymore!
				srcInfo.observer.disconnect();

				// source no longer used
				sources.remove(src);
				LOG.debug("Source " + src + " removed");
			}

			LOG.debug("Source " + src + " uses : " + srcInfo.queriesUsed);

		} else {
			// sollte nicht vorkommen
			LOG.warn("Query with source " + src + "(which was not known here) removed");
		}
	}

	/**
	 * Speichert die aktuellen Histogramme aller Histogramme in einer
	 * Konfigurationsdatei.
	 */
	public void save() {
		if (!isUseHistograms()) {
			return;
		}

		final String filename = OdysseusConfiguration.getHomeDir() + FILENAME;
		LOG.debug("Writing file " + filename);
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(filename));

			for (final SDFAttribute attribute : attributes.keySet()) {
				LOG.debug("Writing values of " + attribute);

				final AttributeObserver observer = attributes.get(attribute);
				final Collection<Double> values = observer.getValues();

				LOG.debug(" Count: " + values.size());

				bw.write(attribute.toString() + "\n");
				bw.write(String.valueOf(values.size()) + "\n");
				for (final Double v : values) {
					bw.write(String.valueOf(v) + "\n");
				}
				bw.flush();

			}

			for (final String attrName : cachedAttributes.keySet()) {

				boolean found = false;
				for (final SDFAttribute attr : attributes.keySet()) {
					if (attr.toString().equals(attrName)) {
						found = true;
						break;
					}
				}
				if (found) {
					continue;
				}

				LOG.debug("Writing cached values of " + attrName);

				final Collection<Double> values = cachedAttributes.get(attrName);

				LOG.debug(" Count: " + values.size());

				bw.write(attrName + "\n");
				bw.write(String.valueOf(values.size()) + "\n");
				for (final Double v : values) {
					bw.write(String.valueOf(v) + "\n");
				}
				bw.flush();
			}
		} catch (final IOException e) {
			LOG.error("Could not save in {}", filename, e);
		} finally {
			if (bw != null) {
				try {
					bw.flush();
					bw.close();
					LOG.debug("Finished");
				} catch (final IOException e) {
				}
			}
		}
	}

	public void setUseHistograms(boolean useHistograms) {
		this.useHistograms = useHistograms;
	}

	private void createAttributeObserver(SDFAttribute attribute) {

		// only numerical attributes!
		if (attribute.getDatatype().isNumeric()) {

			final DataSourceObserverSink sink = findSink(attribute);
			if (sink != null) {
				AttributeObserver observer = attributes.get(attribute);
				if (observer == null) {
					observer = new AttributeObserver(attribute);

					observer.setSink(sink);

					attributes.put(attribute, observer);
					LOG.debug("Added AttributeObserver for attribute " + attribute);
				} else {
					observer.setSink(sink);
					LOG.debug("Activated temporary AttributeObserver for attribute " + attribute);
				}

			} else {
				LOG.warn("Source for attribute " + attribute + " not found. Cannot create observer now.");
				waitingAttributes.add(attribute);
			}
		} else {
			LOG.warn("Attribute " + attribute + " is not numerical");
		}
	}

	private DataSourceObserverSink findSink(SDFAttribute attribute) {
		for (final SourceInfo sourceInfo : sources.values()) {
			final SDFSchema attributeList = sourceInfo.observer.getOutputSchema();
			if (attributeList.contains(attribute)) {
				return sourceInfo.observer;
			}
		}

		return null;
	}

	/**
	 * Liefert die einzige Instanz dieser Klasse.
	 * 
	 * @return Einzige Instanz der Klasse
	 */
	public static DataSourceManager getInstance() {
		if (instance == null) {
			instance = new DataSourceManager();
		}
		return instance;
	}

}
