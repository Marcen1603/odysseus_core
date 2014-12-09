package de.uniol.inf.is.odysseus.processmining.dataanalyser.strategies;

import de.uniol.inf.is.odysseus.processmining.dataanalyser.models.InvariantAnalysisResult;
import de.uniol.inf.is.odysseus.processmining.dataanalyser.utils.StrategieType;

public interface IInvariantStrategy {
	public InvariantAnalysisResult calculateStrategy();
}
