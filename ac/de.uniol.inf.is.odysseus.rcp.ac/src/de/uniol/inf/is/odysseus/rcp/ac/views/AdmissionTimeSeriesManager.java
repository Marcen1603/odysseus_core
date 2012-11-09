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

package de.uniol.inf.is.odysseus.rcp.ac.views;

import java.util.Date;

import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.ac.standard.AdmissionStatus;
import de.uniol.inf.is.odysseus.ac.standard.IAdmissionStatusListener;
import de.uniol.inf.is.odysseus.ac.standard.StandardAC;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorCost;

public class AdmissionTimeSeriesManager implements IAdmissionStatusListener {

	private static final int MAX_DATA_AGE_COUNT = 200;

	private static final TimeSeries actCpu;
	private static final TimeSeries maxCpu;
	private static final TimeSeries minCpu;
	private static final TimeSeries totalCpu;
	private static final TimeSeries actMem;
	private static final TimeSeries maxMem;
	private static final TimeSeries minMem;
	private static final TimeSeries totalMem;
	
	public static final TimeSeriesCollection CPU_SERIES_COLLECTION;
	public static final TimeSeriesCollection MEM_SERIES_COLLECTION;
	
	static {
		actCpu = new TimeSeries("Actual");
		maxCpu = new TimeSeries("Max.");
		minCpu = new TimeSeries("Min.");
		totalCpu = new TimeSeries("Total");

		actCpu.setMaximumItemAge(MAX_DATA_AGE_COUNT);
		maxCpu.setMaximumItemAge(MAX_DATA_AGE_COUNT);
		minCpu.setMaximumItemAge(MAX_DATA_AGE_COUNT);
		totalCpu.setMaximumItemAge(MAX_DATA_AGE_COUNT);

		actMem = new TimeSeries("Actual");
		maxMem = new TimeSeries("Max.");
		minMem = new TimeSeries("Min.");
		totalMem = new TimeSeries("Total");

		actMem.setMaximumItemAge(MAX_DATA_AGE_COUNT);
		maxMem.setMaximumItemAge(MAX_DATA_AGE_COUNT);
		minMem.setMaximumItemAge(MAX_DATA_AGE_COUNT);
		totalMem.setMaximumItemAge(MAX_DATA_AGE_COUNT);
		
		CPU_SERIES_COLLECTION = new TimeSeriesCollection();
		CPU_SERIES_COLLECTION.addSeries(actCpu);
		CPU_SERIES_COLLECTION.addSeries(maxCpu);
		CPU_SERIES_COLLECTION.addSeries(minCpu);
		CPU_SERIES_COLLECTION.addSeries(totalCpu);
		
		MEM_SERIES_COLLECTION = new TimeSeriesCollection();
		MEM_SERIES_COLLECTION.addSeries(actMem);
		MEM_SERIES_COLLECTION.addSeries(maxMem);
		MEM_SERIES_COLLECTION.addSeries(minMem);
		MEM_SERIES_COLLECTION.addSeries(totalMem);
	}

	@Override
	public void updateAdmissionStatus(StandardAC admissionControl, AdmissionStatus status) {
		// async call from admission control!
		Optional<IWorkbench> optWorkbench = tryGetWorkbench();

		if (optWorkbench.isPresent()) {
			final LoadStatistic statistic = createLoadStatistic(status);
			optWorkbench.get().getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					try {
						Date timestampDate = new Date(statistic.getTimestamp());
						Second timestampSecond = new Second(timestampDate);
						actCpu.add(timestampSecond, statistic.getActCpuLoad());
						maxCpu.add(timestampSecond, statistic.getMaxCpuLoad());
						minCpu.add(timestampSecond, statistic.getMinCpuLoad());
						totalCpu.add(timestampSecond, statistic.getTotalCpuLoad());
	
						actMem.add(timestampSecond, statistic.getActMemLoad());
						maxMem.add(timestampSecond, statistic.getMaxMemLoad());
						minMem.add(timestampSecond, statistic.getMinMemLoad());
						totalMem.add(timestampSecond, statistic.getTotalMemLoad());
					} catch( Throwable t ) {}
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

	private static LoadStatistic createLoadStatistic(AdmissionStatus status) {
		OperatorCost actCost = (OperatorCost) status.getActCost();
		OperatorCost maxCost = (OperatorCost) status.getMaxCost();
		OperatorCost minCost = (OperatorCost) status.getMinCost();

		LoadStatistic statistic = new LoadStatistic(actCost.getCpuCost(), maxCost.getCpuCost(), minCost.getCpuCost(), Runtime.getRuntime().availableProcessors(), actCost.getMemCost(),
				maxCost.getMemCost(), minCost.getMemCost(), Runtime.getRuntime().totalMemory(), status.getTimestamp());
		return statistic;
	}
}
