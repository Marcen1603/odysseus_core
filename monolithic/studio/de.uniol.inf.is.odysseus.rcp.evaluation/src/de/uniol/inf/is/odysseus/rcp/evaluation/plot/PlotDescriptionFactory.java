package de.uniol.inf.is.odysseus.rcp.evaluation.plot;

import de.uniol.inf.is.odysseus.rcp.evaluation.model.EvaluationType;
import de.uniol.inf.is.odysseus.rcp.evaluation.plot.description.IPlotDescription;
import de.uniol.inf.is.odysseus.rcp.evaluation.plot.description.LatencyPlotDescriptionEnglish;
import de.uniol.inf.is.odysseus.rcp.evaluation.plot.description.ThroughputPlotDescriptionEnglish;

public abstract class PlotDescriptionFactory {
	
	
	public static IPlotDescription createPlotDescription(EvaluationType type){
		switch (type) {
		case LATENCY:
			return new LatencyPlotDescriptionEnglish();
		case THROUGHPUT:
			return new ThroughputPlotDescriptionEnglish();
		default:
			break;
		}
		return null;
	}
	
	

}
