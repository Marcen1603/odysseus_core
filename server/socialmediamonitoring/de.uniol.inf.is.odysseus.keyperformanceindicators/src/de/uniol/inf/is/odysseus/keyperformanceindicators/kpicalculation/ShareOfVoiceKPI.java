package de.uniol.inf.is.odysseus.keyperformanceindicators.kpicalculation;

import java.util.ArrayList;
import java.util.List;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

public class ShareOfVoiceKPI<M extends ITimeInterval> extends AbstractKeyPerformanceIndicators<M> {
	
	private String kpiType = "shareofvoice";
	private List<String> concreteElements = new ArrayList<>();
	private List<String> allElements = new ArrayList<>();
	private double shareOfVoiceResult = 0;
	private double counterConcreteElements = 0;
	private double counterAllElements = 0;

	public ShareOfVoiceKPI()
	{}
	
	public ShareOfVoiceKPI(String kpiName)
	{
		this.kpiType = kpiName;
	}
	
	@Override
	public void setKPIName(String kpiName) 
	{
		this.kpiType = kpiName;
	}
	
	@Override
	public double manageKPICalculation(List<Tuple<M>> incomingTuple, List<String> concrete, List<String> all, int positionOfInputText)
	{	
		this.concreteElements = concrete;
		this.allElements = all;
		
		this.counterConcreteElements = 0;
		this.counterAllElements = 0;
		
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
		if(this.counterConcreteElements + this.counterAllElements > 0)
			this.shareOfVoiceResult = (this.counterConcreteElements / (/*this.counterConcreteElements +*/ this.counterAllElements)) * 100;
		else
			this.shareOfVoiceResult = 0;
	}

	private void findAndCountGivenWordsInLists(String currentInputText) 
	{
		for(int i=0; i<this.concreteElements.size(); i++)
		{
			int lastIndex = currentInputText.indexOf(this.concreteElements.get(i).toString());
			
			if(lastIndex == -1)
			{
				lastIndex = currentInputText.indexOf(this.concreteElements.get(i).toString().toLowerCase());
			}
						
			while(lastIndex != -1)
			{
				this.counterConcreteElements++;
				lastIndex = currentInputText.indexOf(this.concreteElements.get(i).toString().toLowerCase(), lastIndex + this.concreteElements.get(i).toString().length());
			}
			
//			if((currentInputText.contains(this.concreteElements.get(i).toString().toLowerCase())) || (currentInputText.contains(this.concreteElements.get(i).toString())))
//			{
//				this.counterConcreteElements++;
//			}
		}
		
		for(int i=0; i<this.allElements.size(); i++)
		{
			
			int lastIndex = currentInputText.indexOf(this.allElements.get(i).toString());
			
			if(lastIndex == -1)
			{
				lastIndex = currentInputText.indexOf(this.allElements.get(i).toString().toLowerCase());
			}
						
			while(lastIndex != -1)
			{
				this.counterAllElements++;
				lastIndex = currentInputText.indexOf(this.allElements.get(i).toString().toLowerCase(), lastIndex + this.allElements.get(i).toString().length());
			}
						
//			if(currentInputText.contains(this.allElements.get(i).toString().toLowerCase()))
//			{
//				this.counterAllElements++;	
//			}
		}
	}

	@Override
	public String getType() 
	{
		return this.kpiType;
	}
	
	@Override
	public IKeyPerformanceIndicators<M> getInstance(String kpiName) 
	{
		return new ShareOfVoiceKPI<>(kpiName);
	}
}
