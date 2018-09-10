package de.uniol.inf.is.odysseus.server.intervalapproach;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.PairMap;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;

public class GroupCoalescePO<M extends ITimeInterval> extends
		AbstractCoalescePO<M> {

	Object lastGroupID = null;
	int maxElementsPerGroup = -1;
	int currentCount = 0;
	boolean createOnHeartbeat = false;

	public GroupCoalescePO(SDFSchema inputSchema, SDFSchema outputSchema,
			List<SDFAttribute> groupingAttributes,
			Map<SDFSchema, Map<AggregateFunction, SDFAttribute>> aggregations,
			int maxElementsPerGroup) {
		super(inputSchema, outputSchema, groupingAttributes, aggregations);
		this.maxElementsPerGroup = maxElementsPerGroup;
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	public void process_open() throws OpenFailedException {
		getGroupProcessor().init();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(IStreamObject<M> object, int port) {
//		System.err.println("IN [" + port + "] : TUPL: " + object.getMetadata().getStart());
		
		super.process_next(object, port);
		// 1st check if the same group as last one
		Object currentGroupID = getGroupProcessor().getGroupID(object);


		// The created object is a combiniation of all objects before
		// --> so the new object needs the start timestamp of the first
		// participating object and the other metadata of the last
		// participating object (maybe a metadata merge function should be used)
		
		if (currentPartialAggregates != null
				&& currentGroupID == lastGroupID
				&& (maxElementsPerGroup == -1 || currentCount < maxElementsPerGroup)) {
			// TODO: Is it really neceessary to create a new partial aggregate?
			PairMap<SDFSchema, AggregateFunction, IPartialAggregate<IStreamObject<M>>, M> newP = calcMerge(
					currentPartialAggregates, object, true);
			M newMeta = (M) object.getMetadata().clone();
			// keep start
			newMeta.setStart(currentPartialAggregates.getMetadata().getStart());
			newP.setMetadata(newMeta);
			currentPartialAggregates = newP;
			currentCount++;
			createHeartbeat(newMeta.getStart());
			
		} else {
			if (currentPartialAggregates != null) {
				createAndSend();
			}
			// init new PartialAggregate
			currentPartialAggregates = calcInit(object);
			currentPartialAggregates.setMetadata((M) object.getMetadata()
					.clone());
			currentCount = 1;
		}
		lastGroupID = currentGroupID;
	}

	protected void createAndSend() {
		PairMap<SDFSchema, AggregateFunction, IStreamObject<M>, ?> result = calcEval(currentPartialAggregates, false);
		IStreamObject<M> out = getGroupProcessor().createOutputElement(
				lastGroupID, result);
		out.setMetadata(currentPartialAggregates.getMetadata());
		 
//		System.err.println("OUT : TUPL: " + out.getMetadata().getStart());
		transfer(out);
		sendPunctuations(getWatermark());
	}
	
	@Override
	protected void process_done(int port) {
		if (currentPartialAggregates != null) {
			createAndSend();
		}	
	}
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
//		System.err.println("IN [" + port + "]: PUNC: " + punctuation);
		
		if (createOnHeartbeat && punctuation.isHeartbeat()) {
			if (currentPartialAggregates != null) {
				createAndSend();
			}
		}else{
			super.processPunctuation(punctuation, port);
		}
	}
	
	public void setCreateOnHeartbeat(boolean createOnHeartbeat) {
		this.createOnHeartbeat = createOnHeartbeat;
	}
	
	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {
		
		if (! (ipo instanceof GroupCoalescePO)){
			return false;
		}
		
		return super.isSemanticallyEqual(ipo);
	}

}
