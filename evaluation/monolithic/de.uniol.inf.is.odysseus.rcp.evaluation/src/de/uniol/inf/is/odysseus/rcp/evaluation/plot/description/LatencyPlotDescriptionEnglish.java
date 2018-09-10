package de.uniol.inf.is.odysseus.rcp.evaluation.plot.description;

public class LatencyPlotDescriptionEnglish implements IPlotDescription {

	@Override
	public String getName() {
		return "Latency";
	}

	@Override
	public String getNameAxisX() {
		return "Number of elements";
	}

	@Override
	public String getNameAxisY() {
		return "Time in ns";
	}

}
