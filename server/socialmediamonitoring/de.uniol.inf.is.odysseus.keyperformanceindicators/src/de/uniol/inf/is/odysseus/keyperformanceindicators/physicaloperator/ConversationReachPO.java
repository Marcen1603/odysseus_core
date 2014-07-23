package de.uniol.inf.is.odysseus.keyperformanceindicators.physicaloperator;

import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.keyperformanceindicators.kpicalculation.IKeyPerformanceIndicators;
import de.uniol.inf.is.odysseus.keyperformanceindicators.kpicalculation.KPIRegistry;

@SuppressWarnings("rawtypes")
public class ConversationReachPO<M extends ITimeInterval> extends AbstractPipe<Tuple<M>, Tuple<M>> {
	
	DefaultTISweepArea<Tuple<M>> sweepArea = new DefaultTISweepArea<Tuple<M>>();
	private IKeyPerformanceIndicators kpiType;
	
	private List<String> concreteTopic;
	private List<String> allTopics;
	private SDFAttribute incomingText;
	private SDFAttribute userIDs;
	private double thresholdValue = 0;
	
	private int positionOfIncomingText = -1;
	private int positionOfUserIDs = -1;
	
	public ConversationReachPO(List<String> concreteTopic, List<String> allTopics, SDFAttribute incomingText, SDFAttribute userIDs, double thresholdValue)
	{
		super();
		
		this.concreteTopic = concreteTopic;
		this.allTopics = allTopics;
		this.incomingText = incomingText;
		this.userIDs = userIDs;
		this.thresholdValue = thresholdValue;
	}
	
	public ConversationReachPO(ConversationReachPO<M> conversationReachPO)
	{
		super(conversationReachPO);
		
		this.concreteTopic = conversationReachPO.concreteTopic;
		this.allTopics = conversationReachPO.allTopics;
		this.incomingText = conversationReachPO.incomingText;
		this.userIDs = conversationReachPO.userIDs;
		this.thresholdValue = conversationReachPO.thresholdValue;
	}
	
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}
	
	@Override
	public void process_open() throws OpenFailedException
	{
		super.process_open();
		//Get schema of source which is connected on Port 0
		SDFSchema dataSchema = getSubscribedToSource(0).getSchema();
		//Get position of the attribute "incomingText" from input
		this.positionOfIncomingText =  dataSchema.indexOf(this.incomingText);
		this.positionOfUserIDs = dataSchema.indexOf(this.userIDs);
		//Create the required KPI
		this.kpiType = KPIRegistry.getKPIByName("conversationreach");
	}

	@Override
	protected void process_next(Tuple<M> object, int port) 
	{
		
		//In
		sweepArea.insert(object);
		
		//Aufräumen
		Iterator<Tuple<M>> oldElements = this.sweepArea.extractElementsEndBefore(object.getMetadata().getStart());
					
		synchronized(this.sweepArea)
		{
			//Verbinden
			List<Tuple<M>> list = this.sweepArea.queryOverlapsAsList(object.getMetadata());
		
			//Verarbeiten
			calculateCR(list, object, port);
		}
	}
	
	private void calculateCR(List<Tuple<M>> tupleList, Tuple<M> currentTuple, int port)
	{
		double conversationReachResult = 0;
		
		Tuple outputTuple = new Tuple(1, false);
		
		conversationReachResult = this.kpiType.manageKPICalculation(tupleList, this.concreteTopic, this.allTopics, this.positionOfIncomingText, this.positionOfUserIDs);
		
		this.kpiType.monitorThresholdValue(this.thresholdValue, conversationReachResult);
				
		outputTuple.setAttribute(0, conversationReachResult);
		
		outputTuple.setMetadata(currentTuple.getMetadata());
		outputTuple.setRequiresDeepClone(currentTuple.requiresDeepClone());
		transfer(outputTuple, 0);
		
	}

}
