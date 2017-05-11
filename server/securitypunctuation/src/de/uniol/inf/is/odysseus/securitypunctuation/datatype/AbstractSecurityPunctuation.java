package de.uniol.inf.is.odysseus.securitypunctuation.datatype;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator.SecurityShieldPO;

public abstract class AbstractSecurityPunctuation implements ISecurityPunctuation {

	/**
	 * 
	 */
	private static final long serialVersionUID = 494019333142952706L;
	DataDescriptionPart ddp;
	SecurityRestrictionPart srp;
	boolean sign;
	boolean immutable;
	PointInTime timestamp;
	private static final Logger LOG = LoggerFactory.getLogger(SecurityShieldPO.class);


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




	public AbstractSecurityPunctuation intersectPunctuation(AbstractSecurityPunctuation punctuation) {
		return null;
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ddp == null) ? 0 : ddp.hashCode());
		result = prime * result + (immutable ? 1231 : 1237);
		result = prime * result + (sign ? 1231 : 1237);
		result = prime * result + ((srp == null) ? 0 : srp.hashCode());
		result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractSecurityPunctuation other = (AbstractSecurityPunctuation) obj;
		if (ddp == null) {
			if (other.ddp != null)
				return false;
		} else if (!ddp.equals(other.ddp))
			return false;
		if (immutable != other.immutable)
			return false;
		if (sign != other.sign)
			return false;
		if (srp == null) {
			if (other.srp != null)
				return false;
		} else if (!srp.equals(other.srp))
			return false;
		if (timestamp == null) {
			if (other.timestamp != null)
				return false;
		} else if (!timestamp.equals(other.timestamp))
			return false;
		return true;
	}

	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	

}
