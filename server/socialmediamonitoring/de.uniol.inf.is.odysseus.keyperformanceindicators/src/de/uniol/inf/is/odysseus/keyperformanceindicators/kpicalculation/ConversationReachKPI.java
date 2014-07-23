package de.uniol.inf.is.odysseus.keyperformanceindicators.kpicalculation;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

public class ConversationReachKPI<M extends ITimeInterval> extends AbstractKeyPerformanceIndicators<M> {

	private String kpiType="conversationreach";
	private double conversationReachResult = 0;
	private List<String> concreteTopics = new ArrayList<>();
	private List<String> allTopics = new ArrayList<>();	
	private double counterAllTopics = 0;
	private Map<Integer, String> uniqueUserIDs = new HashMap<Integer, String>();
	private Map<Integer, String> allUserIDs = new HashMap<Integer, String>();
	
	public ConversationReachKPI() {
	}

	public ConversationReachKPI(String kpiName) {
		this.kpiType = kpiName;
	}

	@Override
	public double manageKPICalculation(List<Tuple<M>> incomingTuple, List<String> concreteTopics, List<String>allTopics, int positionOfInputText, int positionOfUserIDs)
	{	
		this.concreteTopics = concreteTopics;
		this.allTopics = allTopics;
		this.counterAllTopics = 0;
		 
		this.uniqueUserIDs.clear();
		this.allUserIDs.clear();
				
		for(Tuple<M> tuple : incomingTuple)
		{
			String currentInputText = tuple.getAttribute(positionOfInputText).toString().toLowerCase();
			String currentUserID = tuple.getAttribute(positionOfUserIDs).toString();
			
			findConcreteWordsAndSaveUserIDs(currentInputText, currentUserID);
				
			findAllTopicsAndSaveAllUserIDs(currentInputText, currentUserID);
		}
		
		//if(((double)this.values.size()) + this.counterAllTopics > 0)
		//return this.conversationReachResult = (((double)this.values.size()) / this.counterAllTopics * 100);
	
		if(((double)this.uniqueUserIDs.size()) + ((double)this.allUserIDs.size()) > 0)
			return this.conversationReachResult = (((double)this.uniqueUserIDs.size()) / ((double)this.allUserIDs.size()) * 100);
		else
			return this.conversationReachResult = 0;
	}

	private void findAllTopicsAndSaveAllUserIDs(String currentInputText, String currentUserID) {
		for(int i=0; i<this.allTopics.size(); i++)
			if(currentInputText.contains(this.allTopics.get(i).toLowerCase().toString()))
			{
				if(!this.allUserIDs.containsValue(currentUserID))
					this.allUserIDs.put(this.allUserIDs.size()+1, currentUserID);
				this.counterAllTopics++;
			}
	}

	private void findConcreteWordsAndSaveUserIDs(String currentInputText, String currentUserID) {
		for(int i=0; i<this.concreteTopics.size(); i++)
			if(currentInputText.contains(this.concreteTopics.get(i).toString().toLowerCase()))
				if(!this.uniqueUserIDs.containsValue(currentUserID))
					this.uniqueUserIDs.put(this.uniqueUserIDs.size()+1, currentUserID);
	}	
	
	@Override
	public void setKPIName(String kpiName) {
		this.kpiType = kpiName;
	}

	@Override
	public IKeyPerformanceIndicators getInstance(String kpi) {
		return new ConversationReachKPI(this.kpiType);
	}

	@Override
	public String getType() {
		return this.kpiType;
	}
}
