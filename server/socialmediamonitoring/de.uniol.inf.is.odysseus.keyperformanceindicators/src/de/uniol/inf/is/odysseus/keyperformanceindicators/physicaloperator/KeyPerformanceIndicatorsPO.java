package de.uniol.inf.is.odysseus.keyperformanceindicators.physicaloperator;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.keyperformanceindicators.kpicalculation.IKeyPerformanceIndicators;
import de.uniol.inf.is.odysseus.keyperformanceindicators.kpicalculation.KPIRegistry;

@SuppressWarnings("unchecked") 
public class KeyPerformanceIndicatorsPO<M extends ITimeInterval> extends AbstractPipe<Tuple<M>, Tuple<M>> {
	
	DefaultTISweepArea<Tuple<M>> sweepArea = new DefaultTISweepArea<Tuple<M>>();

	protected ITransferArea<Tuple<M>, Tuple<M>> tranferFunction;
	protected IMetadataMergeFunction<M> metaDataMerge;
	
	private IKeyPerformanceIndicators algo;
	
	private String kpiName;
	private String domain;
	private double thresholdValue = 0;
	private List<String> subsetOfTerms = new ArrayList<String>();
	private List<String> totalQuantityOfTerms = new ArrayList<String>();
	private SDFAttribute incomingText;
	private SDFAttribute userIDs;
	
	private int outputPort = 0;
	private int totalInputPorts = 1;
	private int positionOfIncomingText = -1;
	private int positionOfUserIDs = -1;
		
	public KeyPerformanceIndicatorsPO(int outputPort, String domain, String kpiName, List<String> subsetOfTerms, List<String> totalQuantityOfTerms, SDFAttribute incomingText, double thresholdValue, SDFAttribute userIDs, int totalInputPorts, IMetadataMergeFunction<M> metaDataMerge,
									  ITransferArea<Tuple<M>, Tuple<M>> transferFunction)
	{
		super();
		this.outputPort = outputPort;
		this.domain = domain;
		this.kpiName = kpiName;
		this.subsetOfTerms = subsetOfTerms;
		this.totalQuantityOfTerms = totalQuantityOfTerms;
		this.incomingText = incomingText;
		this.thresholdValue = thresholdValue;
		this.userIDs = userIDs;
		this.totalInputPorts = totalInputPorts;
		this.metaDataMerge = metaDataMerge;
		this.tranferFunction = transferFunction;
	}
	
	public KeyPerformanceIndicatorsPO(KeyPerformanceIndicatorsPO<M> splitPO)
	{
		super(splitPO);
		this.outputPort = splitPO.outputPort;
		this.domain = splitPO.domain;
		this.kpiName = splitPO.kpiName;
		this.subsetOfTerms = splitPO.subsetOfTerms;
		this.totalQuantityOfTerms = splitPO.totalQuantityOfTerms;
		this.incomingText = splitPO.incomingText;
		this.thresholdValue = splitPO.thresholdValue;
		this.userIDs = splitPO.userIDs;
		this.totalInputPorts = splitPO.totalInputPorts;
		this.metaDataMerge = splitPO.metaDataMerge;
		this.tranferFunction = splitPO.tranferFunction;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}
	
	@Override
	public void process_open() throws OpenFailedException
	{
		super.process_open();
		
		/*if(this.totalInputPorts == 2)
		{
			this.sweepAreaLeft.clear();
			this.sweepAreaRight.clear();
			
			this.areas[0] = this.sweepAreaLeft;
			this.areas[1] = this.sweepAreaRight;
			
			this.metaDataMerge.init();
			this.tranferFunction.init(this, getSubscribedToSource().size());
			
			//SDFSchema dataSchema2 = getSubscribedToSource(1).getSchema();
		}	*/	
		
		SDFSchema dataSchema = getSubscribedToSource(0).getSchema();
	
		this.positionOfIncomingText = dataSchema.indexOf(this.incomingText);
		
		if(this.userIDs != null){
			this.positionOfUserIDs = dataSchema.indexOf(this.userIDs);
		}
		
		algo = KPIRegistry.getKPIByName(this.kpiName.toLowerCase());
		this.algo.setCountOfAllTopics(this.totalQuantityOfTerms.size());
	}

	@Override
	protected void process_next(Tuple<M> object, int port) 
	{
		//In
		sweepArea.insert(object);
		
		System.out.println("Tupel: " + object.getAttribute(positionOfIncomingText));
	
		//Aufräumen
		Iterator<Tuple<M>> oldElements = this.sweepArea.extractElementsEndBefore(object.getMetadata().getStart());
					
		synchronized(this.sweepArea)
		{
			//Verbinden
			List<Tuple<M>> list = this.sweepArea.queryOverlapsAsList(object.getMetadata());
		
			//Verarbeiten
			processKeyIndicator(list, object, port);
		}
	}
	
	public void processKeyIndicator(List<Tuple<M>> tupleList, Tuple<M> currentTuple, int port)
	{	
		double kpiResult = 0;
		
		if(this.positionOfUserIDs > -1 && this.kpiName.equals("conversationreach")){
			kpiResult = this.algo.manageKPICalculation(tupleList, this.subsetOfTerms, this.totalQuantityOfTerms, this.positionOfIncomingText, this.positionOfUserIDs);
		}
		else{
			kpiResult = this.algo.manageKPICalculation(tupleList, this.subsetOfTerms, this.totalQuantityOfTerms, this.positionOfIncomingText);
		}
		
		this.algo.monitorThresholdValue(this.thresholdValue, kpiResult);
				
		Tuple<M> outputTuple = prepareTupelForOutput(currentTuple, kpiResult);
		
		transfer(outputTuple, this.outputPort);		
	}

	private Tuple<M> prepareTupelForOutput(Tuple<M> currentTuple, double kpiResult) {
		Tuple<M> outputTuple = new Tuple(1, false);
		outputTuple.setAttribute(0, kpiResult);

		outputTuple.setMetadata(currentTuple.getMetadata());
		outputTuple.setRequiresDeepClone(currentTuple.requiresDeepClone());
		return outputTuple;
	}
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		//TODO
	}

	@Override
    public KeyPerformanceIndicatorsPO<M> clone() {
        return new KeyPerformanceIndicatorsPO<M>(this);
    }
}
