package de.uniol.inf.is.odysseus.rcp.evaluation.plot.description;

public class ThroughputPlotDescriptionEnglish implements IPlotDescription {

	@Override
	public String getName() {
		return "Throughput";
	}

	@Override
	public String getNameAxisX() {
		return "Number of elements";
	}

	@Override
	public String getNameAxisY() {
		return "Time in ms";
	}

}
