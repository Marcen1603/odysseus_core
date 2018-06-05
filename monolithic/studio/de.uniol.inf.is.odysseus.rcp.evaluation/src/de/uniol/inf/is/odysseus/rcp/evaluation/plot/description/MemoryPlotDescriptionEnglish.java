package de.uniol.inf.is.odysseus.rcp.evaluation.plot.description;

public class MemoryPlotDescriptionEnglish implements IPlotDescription {

	@Override
	public String getName() {
        return "Memory";
	}

	@Override
	public String getNameAxisY() {
        return "Memory in bytes";
	}

	@Override
	public String getNameAxisX() {
		return "Time in ms";
	}

}
