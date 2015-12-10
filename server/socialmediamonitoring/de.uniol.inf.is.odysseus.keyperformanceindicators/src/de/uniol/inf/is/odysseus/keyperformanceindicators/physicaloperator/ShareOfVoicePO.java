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

public class ShareOfVoicePO <M extends ITimeInterval> extends AbstractPipe<Tuple<M>, Tuple<M>> {

	final private ITimeIntervalSweepArea<Tuple<M>> sweepArea;
	private IKeyPerformanceIndicators<M> kpiType;
	
	private List<String> ownCompany;
	private List<String> allCompanies;
	private double thresholdValue;
	
	private SDFAttribute incomingText;
	
	private int positionOfInputText = -1;
	
	public ShareOfVoicePO(List<String> ownCompany, List<String> allCompanies, SDFAttribute incomingText, double thresholdValue, ITimeIntervalSweepArea<Tuple<M>> sweepArea)
	{
		super();
		this.ownCompany = ownCompany;
		this.allCompanies = allCompanies;
		this.incomingText = incomingText;
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
		this.positionOfInputText =  dataSchema.indexOf(this.incomingText);
		//Create the required KPI
		this.kpiType = KPIRegistry.getKPIByName("shareofvoice");
	}
	
	@Override
	protected void process_next(Tuple<M> object, int port) {
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
			calculateSoV(list, object, port);
		}
	}
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}
	
	private void calculateSoV(List<Tuple<M>> tuple, Tuple<M> currentTuple, int port)
	{		
		double shareOfVoiceResult = 0;
		
		Tuple<M> outputTuple = new Tuple<>(1, false);
		
		shareOfVoiceResult = this.kpiType.manageKPICalculation(tuple, this.ownCompany, this.allCompanies, this.positionOfInputText);
		
		this.kpiType.monitorThresholdValue(this.thresholdValue, shareOfVoiceResult);
				
		outputTuple.setAttribute(0, shareOfVoiceResult);
		
		outputTuple.setMetadata(currentTuple.getMetadata());
		outputTuple.setRequiresDeepClone(currentTuple.requiresDeepClone());
		transfer(outputTuple, 0);
	}
}
