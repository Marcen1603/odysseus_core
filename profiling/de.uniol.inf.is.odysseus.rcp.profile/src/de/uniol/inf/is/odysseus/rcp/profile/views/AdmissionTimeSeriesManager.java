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

package de.uniol.inf.is.odysseus.rcp.profile.views;

import java.util.Date;

import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.rcp.profile.util.RepeatingJobThread;
import de.uniol.inf.is.odysseus.rcp.profile.util.SigarWrapper;

public class AdmissionTimeSeriesManager {

	private static final int MAX_DATA_AGE_COUNT = 200;
	private static final SigarWrapper SIGAR_WRAPPER = new SigarWrapper();

	private static final TimeSeries actCpu;
	private static final TimeSeries actMem;

	public static final TimeSeriesCollection CPU_SERIES_COLLECTION;
	public static final TimeSeriesCollection MEM_SERIES_COLLECTION;

	private static RepeatingJobThread updater;

	static {
		actCpu = new TimeSeries("Actual CPU");
		actCpu.setMaximumItemAge(MAX_DATA_AGE_COUNT);
		actMem = new TimeSeries("Actual MEM");

		actMem.setMaximumItemAge(MAX_DATA_AGE_COUNT);

		CPU_SERIES_COLLECTION = new TimeSeriesCollection();
		CPU_SERIES_COLLECTION.addSeries(actCpu);

		MEM_SERIES_COLLECTION = new TimeSeriesCollection();
		MEM_SERIES_COLLECTION.addSeries(actMem);

	}

	public static void start() {
		updater = new RepeatingJobThread(3000, "Load statistic updater") {
			@Override
			public void doJob() {
				update();
			}
		};

		updater.start();
	}

	public static void stop() {
		updater.stopRunning();
		updater = null;
	}

	private static void update() {
		Optional<IWorkbench> optWorkbench = tryGetWorkbench();

		if (optWorkbench.isPresent() && !optWorkbench.get().getDisplay().isDisposed()) {
			final LoadStatistic statistic = createLoadStatistic();
			optWorkbench.get().getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					try {
						Date timestampDate = new Date(statistic.getTimestamp());
						Second timestampSecond = new Second(timestampDate);
						actCpu.add(timestampSecond, statistic.getActCpuLoad());
						actMem.add(timestampSecond, statistic.getActMemLoad());
					} catch (Throwable t) {
						// LOG.error("Could not update statistics", t);
					}
				}
			});
		}
	}

	private static Optional<IWorkbench> tryGetWorkbench() {
		try {
			return Optional.of(PlatformUI.getWorkbench());
		} catch (IllegalStateException ex) {
			return Optional.absent();
		}
	}

	private static LoadStatistic createLoadStatistic() {
		LoadStatistic statistic = new LoadStatistic(SIGAR_WRAPPER.getCpuMax() - SIGAR_WRAPPER.getCpuFree(), Runtime.getRuntime().maxMemory() - Runtime.getRuntime().freeMemory(), System.currentTimeMillis());
		return statistic;
	}
}
