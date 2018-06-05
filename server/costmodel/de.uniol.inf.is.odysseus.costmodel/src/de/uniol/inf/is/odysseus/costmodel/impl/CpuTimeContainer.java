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

import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;

public class CpuTimeContainer {

	private static final Logger LOG = LoggerFactory.getLogger(CpuTimeContainer.class);

	private final Map<String, Double> cpuTimeMap = Maps.newConcurrentMap();

	private boolean loaded = false;

	private CpuTimeUpdateThread updateCpuTimeThread;

	public Optional<Double> getCpuTime(String operatorType) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(operatorType), "operatorType must not be null or empty!");

		loadFile();
		return Optional.fromNullable(cpuTimeMap.get(determineOperatorTypeName(operatorType)));
	}

	private void loadFile() {
		if (!loaded) {
			loaded = true;

			File file = new File(Config.CPUTIME_FILE_NAME);
			if (file.exists()) {
				Map<String, Double> loadedCpuTimeMap = readCpuTimesFromFile(file);

				for (String operatorClass : loadedCpuTimeMap.keySet()) {
					insertCpuTimeIfNeeded(operatorClass, loadedCpuTimeMap.get(operatorClass));
				}
			}
		}
	}

	private static Map<String, Double> readCpuTimesFromFile(File file) {
		Map<String, Double> resultMap = Maps.newHashMap();

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line = br.readLine();
			while (line != null) {
				int separatorPos = line.indexOf("=");
				if (separatorPos != -1) {
					String operatorTypeName = line.substring(0, separatorPos);
					String cpuTimeString = line.substring(separatorPos + 1);
					try {
						double cpuTime = Double.valueOf(cpuTimeString);
						resultMap.put(operatorTypeName, cpuTime);
						LOG.debug("Loaded cputime for operator type{} : {}", operatorTypeName, cpuTime);
						// System.err.println("Loaded cputime for operator type "
						// + operatorTypeName + " : " + cpuTime);
					} catch (Throwable t) {
						LOG.error("Errornous line in datarate file: {}", line, t);
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

	public void save() {
		File file = new File(Config.CPUTIME_FILE_NAME);
		LOG.debug("Saving cputimes to file {}", file);

		try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
			for (String operatorType : cpuTimeMap.keySet()) {
				bw.write(operatorType + "=" + cpuTimeMap.get(operatorType) + "\n");
			}
		} catch (IOException e) {
			LOG.error("Could not save cpu times", e);
		}
	}

	public void setExecutor(final IServerExecutor executor) {
		Preconditions.checkNotNull(executor, "ServerExecutor must not be null!");

		updateCpuTimeThread = new CpuTimeUpdateThread(executor) {
			@Override
			protected void updateCpuTime(String operatorType, double cpuTime) {
				insertCpuTimeIfNeeded(operatorType, cpuTime);
			}
		};
		updateCpuTimeThread.start();
	}

	private void insertCpuTimeIfNeeded(String operatorType, double cpuTime) {
		if (Double.isNaN(cpuTime)) {
			return;
		}

		String typeName = determineOperatorTypeName(operatorType);
		Double cpuTimeValue = cpuTimeMap.get(typeName);

		if (cpuTimeValue == null || cpuTimeValue < cpuTime) {
			cpuTimeMap.put(typeName, cpuTime);
		}
	}
	
	private static String determineOperatorTypeName(String name) {
		if( name.endsWith("PO")) {
			return name.substring(0, name.length() - 2);
		}
		
		if( name.endsWith("AO")) {
			return name.substring(0, name.length() - 2);
		}
		
		if( name.endsWith("TIPO")) {
			return name.substring(0, name.length() - 4);
		}
		
		return name;
	}

	public void unsetExecutor() {
		updateCpuTimeThread.stopRunning();
	}

	public Collection<String> getCpuTimeOperatorClasses() {
		loadFile();

		return Lists.newArrayList(cpuTimeMap.keySet());
	}
}
