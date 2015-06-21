package de.uniol.inf.is.odysseus.condition.rest.datatypes;

import java.util.List;

import de.uniol.inf.is.odysseus.condition.rest.dto.response.CollectionInformation;

public class ClientConfiguration {

	// To which query the client has to connect and which query IDs and outputs
	// it has to connect to
	private List<ConnectionInformation> connectionInformation;

	// Which of the attributes of the connections should be visualized with a
	// gauge
	private List<GaugeVisualizationInformation> gaugeVisualizationInformation;

	// And which with a areaChart
	private List<AreaChartVisualizationInformation> areaChartVisualizationInformation;

	// Which collections should be created
	private List<CollectionInformation> collections;

	public List<ConnectionInformation> getConnectionInformation() {
		return connectionInformation;
	}

	public void setConnectionInformation(List<ConnectionInformation> connectionInformation) {
		this.connectionInformation = connectionInformation;
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

	public List<CollectionInformation> getCollections() {
		return collections;
	}

	public void setCollections(List<CollectionInformation> collections) {
		this.collections = collections;
	}

}
