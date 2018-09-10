package de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.collection.PairMap;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;

public class NoGroupProcessor<R,W extends IClone> implements IGroupProcessor<R, W>, Serializable {

	private static final long serialVersionUID = -6186678898444676392L;

	@Override
	public Object getGroupID(R elem) {
		return 0L;
	}
	
	@Override
	public Long getAscendingGroupID(R elem) {
		return 0L;
	}
	
	@Override
	public void setGroup(Object id, R elem) {
		// ignore
	}
	
	@Override
	public R getGroupingPart(R elem) {
		return null;
	}

	@Override
	public void init() {
		// ignore
	}

	@Override
	public W createOutputElement(Object groupID,
			PairMap<SDFSchema, AggregateFunction, W, ?> r) {
		throw new IllegalArgumentException("Cannot create Element");
	}

	@Override
	public W createOutputElement2(Object groupID,
			PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, ?> e) {
		throw new IllegalArgumentException("Cannot create Element");
	}
	
	@Override
	public String toGroupString(R elem) {
		return elem.toString();
	}
	
	@Override
	public IGroupProcessor<R,W> clone() {
		return this;
	}

}
