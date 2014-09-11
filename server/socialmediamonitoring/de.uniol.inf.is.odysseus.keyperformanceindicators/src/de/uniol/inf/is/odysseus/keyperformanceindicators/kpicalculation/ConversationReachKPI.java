package de.uniol.inf.is.odysseus.keyperformanceindicators.kpicalculation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

public class ConversationReachKPI<M extends ITimeInterval> extends AbstractKeyPerformanceIndicators<M> {

	private String kpiType="conversationreach";
	private double conversationReachResult = 0;
	private double counterAllElements = 0;
	private List<String> concreteElements = new ArrayList<>();
	private List<String> allElements = new ArrayList<>();	
	private Map<Integer, String> uniqueUserIDs = new HashMap<Integer, String>();
	private Map<Integer, String> allUserIDs = new HashMap<Integer, String>();
	
	public ConversationReachKPI() 
	{}

	public ConversationReachKPI(String kpiName) 
	{
		this.kpiType = kpiName;
	}

	@Override
	public double manageKPICalculation(List<Tuple<M>> incomingTuple, List<String> con, List<String>all, int positionOfInputText, int positionOfUserIDs)
	{	
		this.concreteElements = con;
		this.allElements = all;
		this.counterAllElements = 0;
		 
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
			return this.setConversationReachResult((((double)this.uniqueUserIDs.size()) / ((double)this.allUserIDs.size()) * 100));
		else
			return this.setConversationReachResult(0);
	}

	private void findAllTopicsAndSaveAllUserIDs(String currentInputText, String currentUserID) 
	{
		for(int i=0; i<this.allElements.size(); i++)
			if(currentInputText.contains(this.allElements.get(i).toLowerCase().toString()))
			{
				if(!this.allUserIDs.containsValue(currentUserID))
					this.allUserIDs.put(this.allUserIDs.size()+1, currentUserID);
				this.counterAllElements++; //Kann eventuell entfernt werdenS
			}
	}

	private void findConcreteWordsAndSaveUserIDs(String currentInputText, String currentUserID) 
	{
		for(int i=0; i<this.concreteElements.size(); i++)
			if(currentInputText.contains(this.concreteElements.get(i).toString().toLowerCase()))
				if(!this.uniqueUserIDs.containsValue(currentUserID))
					this.uniqueUserIDs.put(this.uniqueUserIDs.size()+1, currentUserID);
	}	
	
	@Override
	public void setKPIName(String kpiName) {
		this.kpiType = kpiName;
	}

	@Override
	public IKeyPerformanceIndicators<M> getInstance(String kpi) {
		return new ConversationReachKPI<>(this.kpiType);
	}

	@Override
	public String getType() {
		return this.kpiType;
	}

	public double getConversationReachResult() {
		return conversationReachResult;
	}

	public double setConversationReachResult(double conversationReachResult) {
		this.conversationReachResult = conversationReachResult;
		return conversationReachResult;
	}
}
