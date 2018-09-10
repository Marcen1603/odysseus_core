package de.uniol.inf.is.odysseus.keyperformanceindicators.physicaloperator;

import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.keyperformanceindicators.kpicalculation.IKeyPerformanceIndicators;
import de.uniol.inf.is.odysseus.keyperformanceindicators.kpicalculation.KPIRegistry;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;

public class ConversationReachPO<M extends ITimeInterval> extends AbstractPipe<Tuple<M>, Tuple<M>> {
	
	final private ITimeIntervalSweepArea<Tuple<M>> sweepArea;
	private IKeyPerformanceIndicators<M> kpiType;
	
	private List<String> concreteTopic;
	private List<String> allTopics;
	private SDFAttribute incomingText;
	private SDFAttribute userIDs;
	private double thresholdValue = 0;
	
	private int positionOfIncomingText = -1;
	private int positionOfUserIDs = -1;
	
	public ConversationReachPO(List<String> concreteTopic, List<String> allTopics, SDFAttribute incomingText, SDFAttribute userIDs, double thresholdValue, ITimeIntervalSweepArea<Tuple<M>> sweepArea )
	{
		super();
		
		this.concreteTopic = concreteTopic;
		this.allTopics = allTopics;
		this.incomingText = incomingText;
		this.userIDs = userIDs;
		this.thresholdValue = thresholdValue;
		this.sweepArea = sweepArea;
	}
	

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}
	
	@SuppressWarnings("unchecked")
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
		
		@SuppressWarnings("unused")
		Iterator<Tuple<M>> oldElements = this.sweepArea.extractElementsBefore(object.getMetadata().getStart());
					
		synchronized(this.sweepArea)
		{
			//Verbinden
			List<Tuple<M>> list = this.sweepArea.queryOverlapsAsList(object.getMetadata());
		
			//Verarbeiten
			calculateCR(list, object, port);
		}
	}
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}

	
	private void calculateCR(List<Tuple<M>> tupleList, Tuple<M> currentTuple, int port)
	{
		double conversationReachResult = 0;
		
		Tuple<M> outputTuple = new Tuple<>(1, false);
		
		conversationReachResult = this.kpiType.manageKPICalculation(tupleList, this.concreteTopic, this.allTopics, this.positionOfIncomingText, this.positionOfUserIDs);
		
		this.kpiType.monitorThresholdValue(this.thresholdValue, conversationReachResult);
				
		outputTuple.setAttribute(0, conversationReachResult);
		
		outputTuple.setMetadata(currentTuple.getMetadata());
		outputTuple.setRequiresDeepClone(currentTuple.requiresDeepClone());
		transfer(outputTuple, 0);
		
	}

}
