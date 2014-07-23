package de.uniol.inf.is.odysseus.keyperformanceindicators.kpicalculation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

public class ShareOfVoiceKPI<M extends ITimeInterval> extends AbstractKeyPerformanceIndicators<M> {
	
	private String kpiType = "shareofvoice";
	private List<String> ownBrands = new ArrayList<>();
	private List<String> allBrands = new ArrayList<>();
	private double shareOfVoiceResult = 0;
	private double counterOwnBrands = 0;
	private double counterAllBrands = 0;

	public ShareOfVoiceKPI()
	{}
	
	public ShareOfVoiceKPI(String kpiName)
	{
		this.kpiType = kpiName;
	}
	
	@Override
	public void setKPIName(String kpiName) {
		this.kpiType = kpiName;
	}
	
	@Override
	public double manageKPICalculation(List<Tuple<M>> incomingTuple, List<String> concrete, List<String> all, int positionOfInputText)
	{	
		this.ownBrands = concrete;
		this.allBrands = all;
		
		this.counterOwnBrands = 0;
		this.counterAllBrands = 0;
		
		for(Tuple<M> tuple : incomingTuple)
		{
			String currentInputText = tuple.getAttribute(positionOfInputText).toString().toLowerCase();					
			findAndCountGivenWordsInLists(currentInputText);				
			calculateShareOfVoice();
		}
		return this.shareOfVoiceResult;
	}

	/*
	 * Calculates the SOV result with the two counters
	 */
	private void calculateShareOfVoice() {
		if(this.counterOwnBrands + this.counterAllBrands > 0)
			this.shareOfVoiceResult = (this.counterOwnBrands / (this.counterOwnBrands + this.counterAllBrands)) * 100;
		else
			this.shareOfVoiceResult = 0;
	}

	private void findAndCountGivenWordsInLists(String currentInputText) {
		for(int i=0; i<this.ownBrands.size(); i++)
		{
			if(currentInputText.contains(this.ownBrands.get(i).toString().toLowerCase()))
				this.counterOwnBrands++;
		}
		
		for(int i=0; i<this.allBrands.size(); i++)
		{
			if(currentInputText.contains(this.allBrands.get(i).toString().toLowerCase()))
				this.counterAllBrands++;
		}
	}

	@Override
	public String getType() {
		return this.kpiType;
	}
	
	@Override
	public IKeyPerformanceIndicators getInstance(String kpiName) {
		return new ShareOfVoiceKPI(kpiName);
	}
}
