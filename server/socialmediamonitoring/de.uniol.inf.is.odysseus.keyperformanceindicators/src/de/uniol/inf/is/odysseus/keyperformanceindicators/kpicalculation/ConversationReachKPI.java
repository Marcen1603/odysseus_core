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
	private Map<Integer, String> uniqueUserNames = new HashMap<Integer, String>();
	private Map<Integer, String> allUserNames = new HashMap<Integer, String>();
	
	public ConversationReachKPI() 
	{}

	public ConversationReachKPI(String kpiName) 
	{
		this.kpiType = kpiName;
	}

	@Override
	public double manageKPICalculation(List<Tuple<M>> incomingTuple, List<String> con, List<String>all, int positionOfInputText, int positionOfUseNames)
	{	
		this.concreteElements = con;
		this.allElements = all;
		this.counterAllElements = 0;
		 
		this.uniqueUserNames.clear();
		this.allUserNames.clear();
				
		for(Tuple<M> tuple : incomingTuple)
		{
			String currentInputText = tuple.getAttribute(positionOfInputText).toString().toLowerCase();
			String currentUserName = tuple.getAttribute(positionOfUseNames).toString();
			
			findConcreteWordsAndSaveUserNames(currentInputText, currentUserName);
				
			findAllTopicsAndSaveAllUserNames(currentInputText, currentUserName);
		}
		
		//if(((double)this.values.size()) + this.counterAllTopics > 0)
		//return this.conversationReachResult = (((double)this.values.size()) / this.counterAllTopics * 100);
	
		if(((double)this.uniqueUserNames.size()) + ((double)this.allUserNames.size()) > 0)
			return this.setConversationReachResult((((double)this.uniqueUserNames.size()) / ((double)this.allUserNames.size()) * 100));
		else
			return this.setConversationReachResult(0);
	}

	private void findAllTopicsAndSaveAllUserNames(String currentInputText, String currentUserName) 
	{
		for(int i=0; i<this.allElements.size(); i++)
			if(currentInputText.contains(this.allElements.get(i).toLowerCase().toString()))
			{
				if(!this.allUserNames.containsValue(currentUserName))
				{
					this.allUserNames.put(this.allUserNames.size()+1, currentUserName);
					System.out.println("User_Name in all:" + currentUserName);
				}
				this.counterAllElements++; 
			}
	}

	private void findConcreteWordsAndSaveUserNames(String currentInputText, String currentUserName) 
	{
		for(int i=0; i<this.concreteElements.size(); i++)
			if(currentInputText.contains(this.concreteElements.get(i).toString().toLowerCase()))
				if(!this.uniqueUserNames.containsValue(currentUserName))
				{
					this.uniqueUserNames.put(this.uniqueUserNames.size()+1, currentUserName);
					System.out.println("User_Name in concrete:" + currentUserName);
				}
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
