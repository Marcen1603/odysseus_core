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
			int lastIndex = currentInputText.indexOf(this.concreteElements.get(i).toString());
		
			if(lastIndex == -1)
			{
				lastIndex = currentInputText.indexOf(this.concreteElements.get(i).toString().toLowerCase());
			}
						
			while(lastIndex != -1)
			{
				this.countOfConcreteElements++;
				lastIndex = currentInputText.indexOf(this.concreteElements.get(i).toString().toLowerCase(), lastIndex + this.concreteElements.get(i).toString().length());
			}
						
//			if(currentInputText.contains(this.concreteElements.get(i).toString().toLowerCase()) || currentInputText.contains(this.concreteElements.get(i).toString()))
//			{
//				this.countOfConcreteElements++;
//				findAndCountLinksToTheConcreteTopic(currentInputText);	
//			}
		}
		
		findAndCountLinksToTheConcreteTopic(currentInputText);
	}
	
	private void findAndCountLinksToTheConcreteTopic(String text)
	{	
		
		int secondIndex = text.indexOf(urlsToValidate[0]);
		
		if(secondIndex == -1)
		{
			secondIndex = text.indexOf(urlsToValidate[0].toLowerCase());
			//System.out.println("Link: " + secondIndex + " String: " + currentInputText);
		}
		
		while(secondIndex != -1)
		{
			this.countOfConcreteElements++;
			//System.out.println("Index-Link: " + secondIndex + " -- " + this.countOfConcreteElements);
			secondIndex = text.indexOf(urlsToValidate[0], secondIndex + urlsToValidate[0].length());
			
		}
		
		
		
		
//		for(String urlValidator : this.urlsToValidate)
//		{
//			if(text.contains(urlValidator))
//				this.countOfConcreteElements++;
//		}
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
