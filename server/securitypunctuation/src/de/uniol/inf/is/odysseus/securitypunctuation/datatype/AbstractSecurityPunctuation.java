package de.uniol.inf.is.odysseus.securitypunctuation.datatype;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPunctuation;

@SuppressWarnings("serial")
public abstract class AbstractSecurityPunctuation extends AbstractPunctuation implements ISecurityPunctuation {

	public AbstractSecurityPunctuation(AbstractPunctuation punct) {
		super(punct);
	}

	public AbstractSecurityPunctuation(long timestamp) {
		super(timestamp);
	}

	public AbstractSecurityPunctuation(PointInTime timestamp) {
		super(timestamp);
	}

	IDataDescriptionPart ddp;
	ISecurityRestrictionPart srp;
	boolean sign;
	boolean immutable;
	PointInTime timestamp;

	@Override
	public Tuple<?> getValue() {
		return null;
	}

	@Override
	public IDataDescriptionPart getDDP() {
		return ddp;
	}

	@Override
	public ISecurityRestrictionPart getSRP() {
		return srp;
	}

	@Override
	public boolean getSign() {
		return sign;
	}

	@Override
	public boolean getImmutable() {
		return immutable;
	}

	@Override
	public void setDDP(IDataDescriptionPart ddp) {
		this.ddp = ddp;

	}

	@Override
	public void setSRP(ISecurityRestrictionPart srp) {
		this.srp = srp;

	}

	@Override
	public void setImmutable(boolean immutable) {
		this.immutable = immutable;

	}

	@Override
	public void setSign(boolean sign) {
		this.sign = sign;

	}
	@Override
	public AbstractPunctuation clone(PointInTime newTime) {
		return null;
	}
	@Override
	public void setTime(PointInTime timestamp) {
		this.timestamp = timestamp;
	}

}
