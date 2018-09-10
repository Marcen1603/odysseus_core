package de.uniol.inf.is.odysseus.anomalydetection.rest.dto.response;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.uniol.inf.is.odysseus.anomalydetection.rest.datatypes.AreaChartVisualizationInformation;
import de.uniol.inf.is.odysseus.anomalydetection.rest.datatypes.CollectionColoringInformation;
import de.uniol.inf.is.odysseus.anomalydetection.rest.datatypes.ConnectionInformation;
import de.uniol.inf.is.odysseus.anomalydetection.rest.datatypes.GaugeVisualizationInformation;

/**
 * @author Tobias Brandt
 * @since 17.06.2015.
 */
public class CollectionInformation {

	private String name;
	private List<ConnectionInformation> connectionInformation;
	private List<GaugeVisualizationInformation> gaugeVisualizationInformation;
	private List<AreaChartVisualizationInformation> areaChartVisualizationInformation;
	private UUID identifier = UUID.randomUUID();
	private CollectionColoringInformation collectionColoringInformation;

	public CollectionInformation() {
		this.connectionInformation = new ArrayList<>();
		this.gaugeVisualizationInformation = new ArrayList<>();
		this.areaChartVisualizationInformation = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

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

	public void addGaugeVisualizationInformation(GaugeVisualizationInformation info) {
		this.gaugeVisualizationInformation.add(info);
	}

	public List<AreaChartVisualizationInformation> getAreaChartVisualizationInformation() {
		return areaChartVisualizationInformation;
	}

	public void setAreaChartVisualizationInformation(
			List<AreaChartVisualizationInformation> areaChartVisualizationInformation) {
		this.areaChartVisualizationInformation = areaChartVisualizationInformation;
	}

	public void addAreaChartVisualizationInformation(AreaChartVisualizationInformation info) {
		this.areaChartVisualizationInformation.add(info);
	}

	public UUID getIdentifier() {
		return identifier;
	}

	public CollectionColoringInformation getCollectionColoringInformation() {
		return collectionColoringInformation;
	}

	public void setCollectionColoringInformation(CollectionColoringInformation collectionColoringInformation) {
		this.collectionColoringInformation = collectionColoringInformation;
	}

}
