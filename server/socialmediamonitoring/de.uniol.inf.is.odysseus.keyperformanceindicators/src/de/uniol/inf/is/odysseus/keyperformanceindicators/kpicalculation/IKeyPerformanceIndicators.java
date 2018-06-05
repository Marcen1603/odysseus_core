package de.uniol.inf.is.odysseus.keyperformanceindicators.kpicalculation;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

public interface IKeyPerformanceIndicators<M extends ITimeInterval> {

	IKeyPerformanceIndicators<M> getInstance(String kpi);
	
	void setKPIName(String kpiName);
		
	double manageKPICalculation(List<Tuple<M>> tuple, List<String> ownCompany, List<String>allCompanies, int positionOfInputText, int positionOfUseNames);
	
	double manageKPICalculation(List<Tuple<M>> tuple, List<String> ownCompany, List<String>allCompanies, int positionOfInputText);
	
	void monitorThresholdValue(double valueToMonitor, double currentResult);

	String getType();
}
