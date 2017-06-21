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
	
	
	


	@Override
	public IPunctuation clone() {
		// TODO Auto-generated method stub
		return null;
	}

	

	@Override
	public DataDescriptionPart getDDP() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SecurityRestrictionPart getSRP() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getSign() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getImmutable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setDDP(DataDescriptionPart ddp) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSRP(SecurityRestrictionPart srp) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setImmutable(boolean immutable) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSign(boolean sign) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTime(PointInTime timestamp) {
		// TODO Auto-generated method stub

	}

	@Override
	public ISecurityPunctuation intersect(ISecurityPunctuation punctuation,PointInTime ts) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	

}
