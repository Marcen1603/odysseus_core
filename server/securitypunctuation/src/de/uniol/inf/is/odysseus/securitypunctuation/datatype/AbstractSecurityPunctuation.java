package de.uniol.inf.is.odysseus.securitypunctuation.datatype;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public abstract class AbstractSecurityPunctuation implements ISecurityPunctuation {

	DataDescriptionPart ddp;
	SecurityRestrictionPart srp;
	boolean sign;
	boolean immutable;
	PointInTime timestamp;
	private static final long serialVersionUID = 1L;



	@Override
	public boolean isHeartbeat() {
		return false;
	}

	@Override
	abstract public AbstractSecurityPunctuation clone();

	

	@Override
	public boolean before(PointInTime time) {
		return this.timestamp.before(time);
	}

	@Override
	public boolean beforeOrEquals(PointInTime time) {
		return this.timestamp.beforeOrEquals(time);
	}

	@Override
	public boolean after(PointInTime time) {
		return this.timestamp.after(time);
	}

	@Override
	public boolean afterOrEquals(PointInTime time) {
		return this.timestamp.afterOrEquals(time);
	}

	@Override
	public boolean isPunctuation() {
		return true;
	}

	@Override
	public byte getNumber() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public SDFSchema getSchema() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Tuple<?> getValue() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public IPunctuation clone(PointInTime p_start) {
		// TODO Auto-generated method stub
		return null;
	}
	public boolean compareSP(AbstractSecurityPunctuation input){
		return false;
	}

	public boolean match() {
		// TODO Auto-generated method stub
		return false;
	}


}
