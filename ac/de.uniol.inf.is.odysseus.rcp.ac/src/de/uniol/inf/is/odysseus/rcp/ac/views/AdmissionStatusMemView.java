package de.uniol.inf.is.odysseus.rcp.ac.views;

import org.jfree.data.time.TimeSeriesCollection;

public class AdmissionStatusMemView extends AbstractAdmissionView {

	@Override
	protected TimeSeriesCollection getTimeSeriesCollection() {
		return AdmissionTimeSeriesManager.MEM_SERIES_COLLECTION;
	}

	@Override
	protected String getChartTitle() {
		return "Memory Usage";
	}

	@Override
	protected String getXLabel() {
		return "Time";
	}

	@Override
	protected String getYLabel() {
		return "Used Memory (MB)";
	}

	@Override
	protected double getUpperBound() {
		return Runtime.getRuntime().totalMemory() / 1024.0 / 1024.0;
	}

}
