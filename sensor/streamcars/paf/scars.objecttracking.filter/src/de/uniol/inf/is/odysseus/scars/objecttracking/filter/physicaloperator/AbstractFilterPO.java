package de.uniol.inf.is.odysseus.scars.objecttracking.filter.physicaloperator;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.Parameters;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IObjectTrackingLatency;

public abstract class AbstractFilterPO<M extends IProbability & IObjectTrackingLatency & IConnectionContainer>
		extends AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {

	// path to new and old objects
	private String oldObjListPath;
	private String newObjListPath;

	// optional parameters for the filter function. Not used right now
	private HashMap<Enum<Parameters>, Object> parameters;

	public AbstractFilterPO() {
		super();
	}

	public AbstractFilterPO(AbstractFilterPO<M> copy) {
		super(copy);
		this.setNewObjListPath(new String(copy.getNewObjListPath()));
		this.setOldObjListPath(new String(copy.getOldObjListPath()));
		this.setParameters(new HashMap<Enum<Parameters>, Object>(copy.getParameters()));
	}

	@Override
	protected void process_next(MVRelationalTuple<M> object, int port) {
		object.getMetadata().setObjectTrackingLatencyStart();
		object = computeAll(object);
		// transfer to broker
		object.getMetadata().setObjectTrackingLatencyEnd();
		transfer(object);
	}

	public abstract MVRelationalTuple<M> computeAll(MVRelationalTuple<M> object);

	@Override
	public abstract AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> clone();

	@Override
	public de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	// Getter & Setter

	public String getOldObjListPath() {
		return this.oldObjListPath;
	}

	public void setOldObjListPath(String oldObjListPath) {
		this.oldObjListPath = oldObjListPath;
	}

	public String getNewObjListPath() {
		return this.newObjListPath;
	}

	public void setNewObjListPath(String newObjListPath) {
		this.newObjListPath = newObjListPath;
	}

	public HashMap<Enum<Parameters>, Object> getParameters() {
		return parameters;
	}

	public void setParameters(HashMap<Enum<Parameters>, Object> parameters) {
		this.parameters = parameters;
	}
}
