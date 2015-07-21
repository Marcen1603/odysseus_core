package de.uniol.inf.is.odysseus.condition.rest.datatypes;

import java.util.List;

public class OverviewInformation {
	// Which connections should be shown on the overview
	private List<ConnectionInformation> overviewConnections;

	// Which of the attributes of the connections should be visualized with a
	// gauge
	private List<GaugeVisualizationInformation> gaugeVisualizationInformation;

	// And which with a areaChart
	private List<AreaChartVisualizationInformation> areaChartVisualizationInformation;

	public List<ConnectionInformation> getOverviewConnections() {
		return overviewConnections;
	}

	public void setOverviewConnections(List<ConnectionInformation> overviewConnections) {
		this.overviewConnections = overviewConnections;
	}
	
	public List<GaugeVisualizationInformation> getGaugeVisualizationInformation() {
		return gaugeVisualizationInformation;
	}

	public void setGaugeVisualizationInformation(List<GaugeVisualizationInformation> gaugeVisualizationInformation) {
		this.gaugeVisualizationInformation = gaugeVisualizationInformation;
	}

	public List<AreaChartVisualizationInformation> getAreaChartVisualizationInformation() {
		return areaChartVisualizationInformation;
	}

	public void setAreaChartVisualizationInformation(
			List<AreaChartVisualizationInformation> areaChartVisualizationInformation) {
		this.areaChartVisualizationInformation = areaChartVisualizationInformation;
	}
}
