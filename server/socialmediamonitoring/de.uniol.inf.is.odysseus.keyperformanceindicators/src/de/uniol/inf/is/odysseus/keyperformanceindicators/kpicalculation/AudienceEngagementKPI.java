package de.uniol.inf.is.odysseus.keyperformanceindicators.kpicalculation;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

public class AudienceEngagementKPI<M extends ITimeInterval> extends AbstractKeyPerformanceIndicators<M> {
	
	private String kpiType="audienceengagement";
	private List<String> concreteElements = new ArrayList<>();
	private List<String> allElements = new ArrayList<>();
	private String[] urlsToValidate = new String[]{"http://", "https://"};
	private double audienceEngagementResult = 0;
	private double countOfConcreteElements = 0;
		
	public AudienceEngagementKPI(){}
	
	public AudienceEngagementKPI(String kpiName)
	{
		this.kpiType = kpiName;
	}

	@Override
	public void setKPIName(String kpiName) 
	{
		this.kpiType = kpiName;
	}
		
	@Override
	public double manageKPICalculation(List<Tuple<M>> incomingTuple, List<String> con, List<String>all, int positionOfInputText)
	{
		this.concreteElements = con;
		this.allElements = all;
		this.countOfConcreteElements = 0;
		
		for(Tuple<M> tuple : incomingTuple)
		{
			String currentInputText = tuple.getAttribute(positionOfInputText).toString().toLowerCase();					
			findAndCountGivenWordsInLists(currentInputText);
			calculateAudienceEngagement();
		}
		return this.audienceEngagementResult;
	}
		
	private void findAndCountGivenWordsInLists(String currentInputText) 
	{
		for(int i=0; i<this.concreteElements.size(); i++)
		{			
			if(currentInputText.contains(this.concreteElements.get(i).toString().toLowerCase()))
			{
				this.countOfConcreteElements++;
				findAndCountLinksToTheConcreteTopic(currentInputText);
			}
		}
	}
	
	private void findAndCountLinksToTheConcreteTopic(String text)
	{		
		for(String urlValidator : this.urlsToValidate)
		{
			if(text.contains(urlValidator))
				this.countOfConcreteElements++;
		}
	}
	
	private void calculateAudienceEngagement() 
	{	
		this.audienceEngagementResult = (this.countOfConcreteElements / this.allElements.size());
	}

	@Override
	public IKeyPerformanceIndicators<M> getInstance(String kpi) {
		return new AudienceEngagementKPI<>(this.kpiType);
	}

	@Override
	public String getType() 
	{
		return this.kpiType;
	}

	public List<String> getAllTopics() 
	{
		return allElements;
	}

	public void setAllTopics(List<String> allTopics) 
	{
		this.allElements = allTopics;
	}
}
