package de.uniol.inf.is.odysseus.keyperformanceindicators.kpicalculation;

import java.io.File;
import java.io.FileWriter;
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
	
	File file;
	FileWriter writer;
	
	public ConversationReachKPI() 
	{
//		try {
//			writer = new FileWriter("C:\\Users\\Rick\\workspace2\\Projekt2\\kpiwithtimestamps.txt");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}

	public ConversationReachKPI(String kpiName) 
	{
		this.kpiType = kpiName;
		
//		try {
//			writer = new FileWriter("C:\\Users\\Rick\\workspace2\\Projekt2\\kpiwithtimestamps.txt");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
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
		
//			try {
//				writer.write("----------------Start--------------");
//				
//				for(Tuple<M> tuple : incomingTuple)
//				{
////					Date datstart = new Date(Long.parseLong(tuple.getMetadata().getStart().toString()));
////					Date datend = new Date(Long.parseLong(tuple.getMetadata().getEnd().toString()));	
////					writer.write("Tuple: " + tuple.getAttribute(positionOfInputText).toString() + " --- Start: " + datstart + " ---End: " + datend);
//				}
//				
//				writer.write("KPI : " + String.valueOf((((double)this.uniqueUserNames.size()) / ((double)this.allUserNames.size()) * 100)) + "\n");
//			
//				writer.write("----------------Ende--------------");
//				
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}

		if(((double)this.uniqueUserNames.size()) + ((double)this.allUserNames.size()) > 0)
			return this.setConversationReachResult((((double)this.uniqueUserNames.size()) / ((double)this.allUserNames.size()) * 100));
		else
			return this.setConversationReachResult(0);
	}

	private void findAllTopicsAndSaveAllUserNames(String currentInputText, String currentUserName) 
	{
		for(int i=0; i<this.allElements.size(); i++)
		{	
			int lastIndex = currentInputText.indexOf(this.allElements.get(i).toString());
			
			if(lastIndex == -1)
			{
				lastIndex = currentInputText.indexOf(this.allElements.get(i).toString().toLowerCase());
			}
						
			while(lastIndex != -1)
			{	
				if(!this.allUserNames.containsValue(currentUserName))
				{
					this.allUserNames.put(this.allUserNames.size()+1, currentUserName);
				}
				this.counterAllElements++;
				lastIndex = currentInputText.indexOf(this.allElements.get(i).toString().toLowerCase(), lastIndex + this.allElements.get(i).toString().length());
			}
			
//			if(currentInputText.contains(this.allElements.get(i).toLowerCase().toString()))
//			{
//				if(!this.allUserNames.containsValue(currentUserName))
//				{
//					this.allUserNames.put(this.allUserNames.size()+1, currentUserName);
//					System.out.println("User_Name in all:" + currentUserName);
//				}
//				this.counterAllElements++; 
//			}
		}
	}

	private void findConcreteWordsAndSaveUserNames(String currentInputText, String currentUserName) 
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
				if(!this.uniqueUserNames.containsValue(currentUserName))
				{
					this.uniqueUserNames.put(this.uniqueUserNames.size()+1, currentUserName);
				}
				lastIndex = currentInputText.indexOf(this.concreteElements.get(i).toString().toLowerCase(), lastIndex + this.concreteElements.get(i).toString().length());
			}
		}
//			if(currentInputText.contains(this.concreteElements.get(i).toString().toLowerCase()))
//				if(!this.uniqueUserNames.containsValue(currentUserName))
//				{
//					this.uniqueUserNames.put(this.uniqueUserNames.size()+1, currentUserName);
//					System.out.println("User_Name in concrete:" + currentUserName);
//				}
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
