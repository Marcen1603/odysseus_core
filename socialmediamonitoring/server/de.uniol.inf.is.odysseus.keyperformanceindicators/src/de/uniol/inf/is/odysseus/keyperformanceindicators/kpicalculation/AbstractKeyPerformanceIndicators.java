package de.uniol.inf.is.odysseus.keyperformanceindicators.kpicalculation;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

public abstract class AbstractKeyPerformanceIndicators<M extends ITimeInterval> implements IKeyPerformanceIndicators<M>{

	protected double valueToMonitor;
	
	@Override
	public double manageKPICalculation(List<Tuple<M>> tuple, List<String> ownCompany, List<String>allCompanies, int positionOfInputText, int positionOfUseNames){
		return 0;
	}
	
	@Override
	public double manageKPICalculation(List<Tuple<M>> tuple, List<String> ownCompany, List<String>allCompanies, int positionOfInputText){
		return 0;
	}
		
	@Override
	public void monitorThresholdValue(double valueToMonitor, double currentResult){
		this.valueToMonitor = valueToMonitor;
		//if(currentResult < this.valueToMonitor)
			//System.out.println("Grenzwert " + this.valueToMonitor + " unterschritten!" + currentResult);
	}
}
