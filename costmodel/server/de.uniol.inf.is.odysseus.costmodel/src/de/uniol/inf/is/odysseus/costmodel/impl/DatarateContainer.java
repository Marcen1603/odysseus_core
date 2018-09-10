package de.uniol.inf.is.odysseus.costmodel.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class DatarateContainer {

	private static final Logger LOG = LoggerFactory.getLogger(DatarateContainer.class);

	private final Map<String, Double> datarateMap = Maps.newConcurrentMap();

	private boolean loaded = false;

	public Optional<Double> getDatarate(String sourceName) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(sourceName), "sourceName must not be null or empty!");

		loadFile();
		return Optional.fromNullable(datarateMap.get(sourceName));
	}

	private void loadFile() {
		if (!loaded) {
			loaded = true;

			File file = new File(Config.DATARATE_FILE_NAME);
			if (file.exists()) {
				datarateMap.clear();
				datarateMap.putAll(readDataratesFromFile(file));
			}
		}
	}

	private static Map<String, Double> readDataratesFromFile(File file) {
		Map<String, Double> resultMap = Maps.newHashMap();

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line = br.readLine();
			while (line != null) {
				int separatorPos = line.indexOf("=");
				if (separatorPos != -1) {
					String sourceName = line.substring(0, separatorPos);
					String datarateString = line.substring(separatorPos + 1);

					try {
						double datarate = Double.valueOf(datarateString);
						resultMap.put(sourceName, datarate);
						LOG.debug("Loaded datarate for source {} : {}", sourceName, datarate);
					} catch (Throwable t) {
						LOG.error("Errornous line in datarate file: {}", line);
					}
				} else {
					LOG.error("Errornous line in datarate file: {}", line);
				}

				line = br.readLine();
			}
		} catch (IOException e) {
			LOG.error("Could not load data rate file", e);
		}

		return resultMap;
	}

	public void putDatarate(String sourceName, double datarate) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(sourceName), "sourceName must not be null or empty!");
		Preconditions.checkArgument(datarate >= 0, "datarate must be non-negative!");

		datarateMap.put(sourceName, datarate);
	}

	public void save() {
		File file = new File(Config.DATARATE_FILE_NAME);
		LOG.debug("Saving datarates to file {}", file);

		try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
			for (String sourceName : datarateMap.keySet()) {
				bw.write(sourceName + "=" + datarateMap.get(sourceName) + "\n");
			}
		} catch (IOException e) {
			LOG.error("Could not save datarates", e);
		}
	}

	public Collection<String> getDatarateSourceNames() {
		loadFile();
		
		return Lists.newArrayList(datarateMap.keySet());
	}
}
