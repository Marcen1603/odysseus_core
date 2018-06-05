package de.uniol.inf.is.odysseus.rcp.evaluation.plot;

import de.uniol.inf.is.odysseus.rcp.evaluation.model.EvaluationType;
import de.uniol.inf.is.odysseus.rcp.evaluation.plot.description.CPUPlotDescriptionEnglish;
import de.uniol.inf.is.odysseus.rcp.evaluation.plot.description.IPlotDescription;
import de.uniol.inf.is.odysseus.rcp.evaluation.plot.description.LatencyPlotDescriptionEnglish;
import de.uniol.inf.is.odysseus.rcp.evaluation.plot.description.MemoryPlotDescriptionEnglish;
import de.uniol.inf.is.odysseus.rcp.evaluation.plot.description.ThroughputPlotDescriptionEnglish;

public abstract class PlotDescriptionFactory {
	
	
	public static IPlotDescription createPlotDescription(EvaluationType type){
        switch (type) {
            case LATENCY:
                return new LatencyPlotDescriptionEnglish();
            case THROUGHPUT:
                return new ThroughputPlotDescriptionEnglish();
            case CPU:
                return new CPUPlotDescriptionEnglish();
            case MEMORY:
                return new MemoryPlotDescriptionEnglish();
            default:
                break;
        }
        return null;
	}
	
	

}
