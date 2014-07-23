package de.uniol.inf.is.odysseus.keyperformanceindicators.kpicalculation;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

public class AudienceEngagementKPI<M extends ITimeInterval> extends AbstractKeyPerformanceIndicators<M> {
	
	private String kpiType="audienceengagement";
	private List<String> concreteTopics = new ArrayList<>();
	private List<String> allTopics = new ArrayList<>();
	private String[] urlsToValidate = new String[]{"http://", "https://"};
	private double audienceEngagementResult = 0;
	private double countOfConcreteTopic = 0;
		
	public AudienceEngagementKPI(){}
	
	public AudienceEngagementKPI(String kpiName){
		this.kpiType = kpiName;
	}

	@Override
	public void setKPIName(String kpiName) {
		this.kpiType = kpiName;
	}
	
	
/*	@Override
	public double manageKPICalculation(List<String> incomingTuple, List<String> concreteTopics, List<String>allTopics)
	{
		this.concreteTopics = concreteTopics;
		this.allTopics = allTopics;		
		this.countOfConcreteTopic = 0;
		String currentInputText = null;
			
		for(String tuple : incomingTuple)
		{
			findAndCountGivenWordsInLists(currentInputText);
			calculateAudienceEngagement();
		}
		return this.audienceEngagementResult;
	}
	
*/	
	
	
	
	@Override
	public double manageKPICalculation(List<Tuple<M>> incomingTuple, List<String> concreteTopics, List<String>allTopics, int positionOfInputText)
	{
		this.concreteTopics = concreteTopics;
		this.allTopics = allTopics;		
		this.countOfConcreteTopic = 0;
	
		for(Tuple<M> tuple : incomingTuple)
		{
			String currentInputText = tuple.getAttribute(positionOfInputText).toString().toLowerCase();					
			findAndCountGivenWordsInLists(currentInputText);
			calculateAudienceEngagement();
		}
		return this.audienceEngagementResult;
	}
		
	private void findAndCountGivenWordsInLists(String currentInputText) {
		for(int i=0; i<this.concreteTopics.size(); i++)
		{			
			if(currentInputText.contains(this.concreteTopics.get(i).toString().toLowerCase()))
			{
				this.countOfConcreteTopic++;
				findAndCountLinksToTheConcreteTopic(currentInputText);
			}
		}
	}
	
	private void findAndCountLinksToTheConcreteTopic(String text){
		
		for(String urlValidator : this.urlsToValidate)
		{
			if(text.contains(urlValidator))
				this.countOfConcreteTopic++;
		}
	}
	
	private void calculateAudienceEngagement() {
		this.audienceEngagementResult = (this.countOfConcreteTopic / this.countOfAllTopics);
	}

	@Override
	public IKeyPerformanceIndicators getInstance(String kpi) {
		return new AudienceEngagementKPI(this.kpiType);
	}

	@Override
	public String getType() {
		return this.kpiType;
	}
}
