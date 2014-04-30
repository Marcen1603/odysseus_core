package de.uniol.inf.is.odysseus.costmodel.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;

public class CpuTimeContainer {

	private static final Logger LOG = LoggerFactory.getLogger(CpuTimeContainer.class);

	private final Map<Class<?>, Double> cpuTimeMap = Maps.newConcurrentMap();

	private boolean loaded = false;

	private CpuTimeUpdateThread updateCpuTimeThread;

	public Optional<Double> getCpuTime(Class<?> operatorType) {
		Preconditions.checkNotNull(operatorType, "operatorType must not be null!");

		loadFile();
		return Optional.fromNullable(cpuTimeMap.get(operatorType));
	}

	private void loadFile() {
		if (!loaded) {
			loaded = true;

			File file = new File(Config.CPUTIME_FILE_NAME);
			if (file.exists()) {
				cpuTimeMap.clear();
				cpuTimeMap.putAll(readCpuTimesFromFile(file));
			}
		}
	}

	private static Map<Class<?>, Double> readCpuTimesFromFile(File file) {
		Map<Class<?>, Double> resultMap = Maps.newHashMap();

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line = br.readLine();
			while (line != null) {
				int separatorPos = line.indexOf("=");
				if (separatorPos != -1) {
					try {
						String operatorTypeName = line.substring(0, separatorPos);
						Class<?> operatorType = Class.forName(operatorTypeName);
						
						String cpuTimeString = line.substring(separatorPos + 1);
						try {
							double cpuTime = Double.valueOf(cpuTimeString);
							resultMap.put(operatorType, cpuTime);
//							LOG.debug("Loaded cputime for operator type{} : {}", operatorTypeName, cpuTime);
							System.err.println("Loaded cputime for operator type " + operatorTypeName + " : " + cpuTime);
						} catch (Throwable t) {
							LOG.error("Errornous line in datarate file: {}", line, t);
						}
					} catch( ClassNotFoundException ex ) {
						LOG.error("Errornous line in cputime file: {}", line, ex);
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

	public void putDatarate(Class<?> operatorType, double datarate) {
		Preconditions.checkNotNull(operatorType, "operatorType must not be null!");
		Preconditions.checkArgument(datarate >= 0, "datarate must be non-negative!");

		cpuTimeMap.put(operatorType, datarate);
	}

	public void save() {
		File file = new File(Config.CPUTIME_FILE_NAME);
		LOG.debug("Saving cputimes to file {}", file);

		try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
			for (Class<?> operatorType : cpuTimeMap.keySet()) {
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
			protected void updateCpuTime(Class<? extends IPhysicalOperator> operatorType, double cpuTime) {
				Double cpuTimeValue = cpuTimeMap.get(operatorType);
				
				if( cpuTimeValue == null || (cpuTimeValue != null && cpuTimeValue < cpuTime )) {
					cpuTimeMap.put(operatorType, cpuTime);
				}
			}
			
		};
		updateCpuTimeThread.start();
	}

	public void unsetExecutor() {
		updateCpuTimeThread.stopRunning();
	}
}
