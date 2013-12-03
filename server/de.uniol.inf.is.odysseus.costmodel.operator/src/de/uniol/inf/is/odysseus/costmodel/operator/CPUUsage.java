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

package de.uniol.inf.is.odysseus.costmodel.operator;

import java.util.List;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class CPUUsage extends Thread {

	private static final Logger LOG = LoggerFactory.getLogger(CPUUsage.class);

	private static final long MEASURE_INTERVAL_MILLIS = 1000;
	private static final long MEAN_TIME_MILLIS = 10 * 1000;

	private static final int MEASUREMENTS_COUNT = (int) (MEAN_TIME_MILLIS / MEASURE_INTERVAL_MILLIS);

	private boolean isRunning = true;

	private final Sigar sigar = new Sigar();
	private final List<Double> usageList = Lists.newLinkedList();
	private double usageSum = 0;
	private double usageMean = 0;

	@Override
	public void run() {
		try {
			int cpuCount = sigar.getCpuPercList().length;
			while (isRunning) {
				CpuPerc perc = sigar.getCpuPerc();
				double cpu = (perc.getUser()) * cpuCount;
				usageSum += cpu;
				
				usageList.add(cpu);
				if( usageList.size() > MEASUREMENTS_COUNT ) {
					double u = usageList.remove(0);
					usageSum -= u;
				}
				
				usageMean = usageSum / usageList.size();
				
				waitSomeTime();
			}

		} catch (SigarException ex) {
			LOG.error("Could not measure cpu usage", ex);
		}
	}
	
	public void stopMeasurements() {
		isRunning = false;
	}
	
	public double getCpuMeanUsage() {
		return usageMean;
	}

	private static void waitSomeTime() {
		try {
			Thread.sleep(MEASURE_INTERVAL_MILLIS);
		} catch (InterruptedException ex) {
		}
	}
}
