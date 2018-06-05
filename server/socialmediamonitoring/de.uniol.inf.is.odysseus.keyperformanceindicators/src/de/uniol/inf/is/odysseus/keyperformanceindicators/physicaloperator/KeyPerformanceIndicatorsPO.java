package de.uniol.inf.is.odysseus.keyperformanceindicators.physicaloperator;
/**
 *  Christopher Licht
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;

@SuppressWarnings("unchecked") 
public class KeyPerformanceIndicatorsPO<M extends ITimeInterval> extends AbstractPipe<Tuple<M>, Tuple<M>> {
	
	final private ITimeIntervalSweepArea<Tuple<M>> sweepArea;

	protected ITransferArea<Tuple<M>, Tuple<M>> tranferFunction;
	protected IMetadataMergeFunction<M> metaDataMerge;
	
	private IKeyPerformanceIndicators<M> algo;
	
	private String kpiName;
	private double thresholdValue = 0;
	private List<String> subsetOfTerms = new ArrayList<String>();
	private List<String> totalQuantityOfTerms = new ArrayList<String>();
	private SDFAttribute incomingText;
	private SDFAttribute userNames;
	
	private int outputPort = 0;
	@SuppressWarnings("unused")
	private int totalInputPorts = 1;
	private int positionOfIncomingText = -1;
	private int positionOfUserNames = -1;
		
	public KeyPerformanceIndicatorsPO(int outputPort, String kpiName, List<String> subsetOfTerms, List<String> totalQuantityOfTerms, SDFAttribute incomingText, double thresholdValue, SDFAttribute userNames, int totalInputPorts, IMetadataMergeFunction<M> metaDataMerge,
									  ITransferArea<Tuple<M>, Tuple<M>> transferFunction, ITimeIntervalSweepArea<Tuple<M>> sweepArea)
	{
		super();
		this.outputPort = outputPort;
		this.kpiName = kpiName;
		this.subsetOfTerms = subsetOfTerms;
		this.totalQuantityOfTerms = totalQuantityOfTerms;
		this.incomingText = incomingText;
		this.thresholdValue = thresholdValue;
		this.userNames = userNames;
		this.totalInputPorts = totalInputPorts;
		this.metaDataMerge = metaDataMerge;
		this.tranferFunction = transferFunction;
		this.sweepArea = sweepArea;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
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
		
		if(this.userNames != null){
			this.positionOfUserNames = dataSchema.indexOf(this.userNames);
		}
		
		algo = KPIRegistry.getKPIByName(this.kpiName.toLowerCase());
	}

	@Override
	protected void process_next(Tuple<M> tuple, int port) 
	{
		//In
		sweepArea.insert(tuple);

		//Aufräumen
		@SuppressWarnings("unused")
		Iterator<Tuple<M>> oldElements = this.sweepArea.extractElementsBefore(tuple.getMetadata().getStart());
					
		synchronized(this.sweepArea)
		{
			//Verbinden
			List<Tuple<M>> list = this.sweepArea.queryOverlapsAsList(tuple.getMetadata());
		
			//Verarbeiten
			processKeyIndicator(list, tuple, port);
		}
	}
	
	public void processKeyIndicator(List<Tuple<M>> tupleList, Tuple<M> currentTuple, int port)
	{	
		double kpiResult = 0;
		
		if(this.positionOfUserNames > -1 && this.kpiName.equals("conversationreach")){
			kpiResult = this.algo.manageKPICalculation(tupleList, this.subsetOfTerms, this.totalQuantityOfTerms, this.positionOfIncomingText, this.positionOfUserNames);
		}
		else{
			kpiResult = this.algo.manageKPICalculation(tupleList, this.subsetOfTerms, this.totalQuantityOfTerms, this.positionOfIncomingText);
		}
		
		this.algo.monitorThresholdValue(this.thresholdValue, kpiResult);
				
		Tuple<M> outputTuple = prepareTupelForOutput(currentTuple, kpiResult);
		
		transfer(outputTuple, this.outputPort);		
	}

	private Tuple<M> prepareTupelForOutput(Tuple<M> currentTuple, double kpiResult) {
		Tuple<M> outputTuple = new Tuple<>(1, false);
		outputTuple.setAttribute(0, kpiResult);

		outputTuple.setMetadata(currentTuple.getMetadata());
		outputTuple.setRequiresDeepClone(currentTuple.requiresDeepClone());
		return outputTuple;
	}
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
	}

}
