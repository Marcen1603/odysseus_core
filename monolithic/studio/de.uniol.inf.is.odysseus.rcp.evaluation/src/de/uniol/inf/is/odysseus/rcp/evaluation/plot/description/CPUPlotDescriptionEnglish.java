package de.uniol.inf.is.odysseus.rcp.evaluation.plot.description;

public class CPUPlotDescriptionEnglish implements IPlotDescription {

	@Override
	public String getName() {
        return "CPU";
	}

	@Override
	public String getNameAxisY() {
        return "%";
	}

	@Override
	public String getNameAxisX() {
		return "Time in ms";
	}

}
