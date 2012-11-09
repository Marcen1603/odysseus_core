package de.uniol.inf.is.odysseus.rcp.ac.views;

import org.jfree.data.time.TimeSeriesCollection;

public class AdmissionStatusCPUView extends AbstractAdmissionView {

	@Override
	protected TimeSeriesCollection getTimeSeriesCollection() {
		return AdmissionTimeSeriesManager.CPU_SERIES_COLLECTION;
	}

	@Override
	protected String getChartTitle() {
		return "CPU Load";
	}

	@Override
	protected String getXLabel() {
		return "Time";
	}

	@Override
	protected String getYLabel() {
		return "Processors";
	}

	@Override
	protected double getUpperBound() {
		return Runtime.getRuntime().availableProcessors();
	}

}
