package de.uniol.inf.is.odysseus.intervalapproach;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.collection.PairMap;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;

public class GroupCoalescePO<M extends ITimeInterval> extends
		AbstractCoalescePO<M> {

	int lastGroupID = -1;
	int maxElementsPerGroup = 0;
	int currentCount = 0;
	boolean createOnHeartbeat = true;

	public GroupCoalescePO(SDFSchema inputSchema, SDFSchema outputSchema,
			List<SDFAttribute> groupingAttributes,
			Map<SDFSchema, Map<AggregateFunction, SDFAttribute>> aggregations,
			int maxElementsPerGroup, ITransferArea<IStreamObject<?>, IStreamObject<?>> transferArea) {
		super(inputSchema, outputSchema, groupingAttributes, aggregations, transferArea);
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
	protected void process_next(IStreamObject<? extends M> object, int port) {
		// 1st check if the same group as last one
		Integer currentGroupID = getGroupProcessor().getGroupID(object);


		// The created object is a combiniation of all objects before
		// --> so the new object needs the start timestamp of the first
		// participating object and the other metadata of the last
		// participating object (maybe a metadata merge function should be used)
		
		if (currentPartialAggregates != null
				&& currentGroupID == lastGroupID
				&& (maxElementsPerGroup == -1 || currentCount < maxElementsPerGroup)) {
			PairMap<SDFSchema, AggregateFunction, IPartialAggregate<IStreamObject<? extends M>>, M> newP = calcMerge(
					currentPartialAggregates, object);
			M newMeta = (M) object.getMetadata().clone();
			// keep start
			newMeta.setStart(currentPartialAggregates.getMetadata().getStart());
			newP.setMetadata(newMeta);
			currentPartialAggregates = newP;
			currentCount++;
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
		PairMap<SDFSchema, AggregateFunction, IStreamObject<M>, ?> result = calcEval(currentPartialAggregates);
		IStreamObject<M> out = getGroupProcessor().createOutputElement(
				lastGroupID, result);
		out.setMetadata(currentPartialAggregates.getMetadata());
		transferArea.transfer(out);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		if (createOnHeartbeat && punctuation.isHeartbeat()) {
			if (currentPartialAggregates != null) {
				createAndSend();
			}
		}
		transferArea.sendPunctuation(punctuation);
	}

	@Override
	public AbstractPipe<IStreamObject<? extends M>, IStreamObject<M>> clone() {
		throw new RuntimeException("Sorry, currently not implemented!");
	}
	
	public void setCreateOnHeartbeat(boolean createOnHeartbeat) {
		this.createOnHeartbeat = createOnHeartbeat;
	}

}
