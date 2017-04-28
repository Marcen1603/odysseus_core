package de.uniol.inf.is.odysseus.securitypunctuation.datatype;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;

public class SecurityPunctuation extends AbstractSecurityPunctuation {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SecurityPunctuation(SecurityPunctuation securityPunctuation) {
		this.ddp = securityPunctuation.getDDP();
		this.srp = securityPunctuation.getSRP();
		this.immutable = securityPunctuation.getImmutable();
		this.sign = securityPunctuation.getSign();
		this.timestamp = securityPunctuation.getTime();
	}

	public SecurityPunctuation(DataDescriptionPart ddp, SecurityRestrictionPart srp, boolean sign, boolean immutable,
			long timestamp) {
		this.ddp = ddp;
		this.srp = srp;
		this.immutable = immutable;
		this.sign = sign;
		this.timestamp = new PointInTime(timestamp);
	}

	public SecurityPunctuation(String stream, String tupleRange, String attributes, String roles, boolean sign,
			boolean immutable, long timestamp) {
		this.ddp = new DataDescriptionPart(stream, tupleRange, attributes);
		this.srp = new SecurityRestrictionPart(roles);
		this.immutable = immutable;
		this.sign = sign;
		this.timestamp = new PointInTime(timestamp);
	}

	public SecurityPunctuation() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public DataDescriptionPart getDDP() {
		return this.ddp;
	}

	@Override
	public SecurityRestrictionPart getSRP() {
		return this.srp;
	}

	@Override
	public boolean getSign() {
		return this.sign;
	}

	@Override
	public boolean getImmutable() {
		return this.immutable;
	}

	@Override
	public PointInTime getTime() {
		return timestamp;
	}

	@Override
	public void setDDP(DataDescriptionPart ddp) {
		this.ddp = new DataDescriptionPart(ddp);

	}

	@Override
	public void setSRP(SecurityRestrictionPart srp) {
		this.srp = new SecurityRestrictionPart(srp);

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
	public void setTime(PointInTime timestamp) {
		this.timestamp = timestamp;

	}

	@Override
	public AbstractSecurityPunctuation clone() {
		return new SecurityPunctuation(this);
	}

	@Override
	public String toString(){
		return ddp.toString()+";"+srp.toString()+";"+sign+";"+immutable+";"+timestamp;
	}
	
	//compares if two SecurityPunctuations are referring to the same Objects
	public boolean compareSP(AbstractSecurityPunctuation input) {
		if (this.ddp.equals(input.getDDP()) && this.immutable == input.getImmutable() && this.sign == input.getSign()) {
			return true;
		}else return false;
	}

	@Override
	public SDFSchema getSchema() {
		List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
		attributes.add(new SDFAttribute("SecurityPunctuation", "type", SDFDatatype.STRING));
		attributes.add(new SDFAttribute("SecurityPunctuation", "point", SDFDatatype.TIMESTAMP));
		attributes.add(new SDFAttribute("SecurityPunctuation", "streamname", SDFDatatype.STRING));
		attributes.add(new SDFAttribute("SecurityPunctuation", "tupleRange", SDFDatatype.STRING));
		attributes.add(new SDFAttribute("SecurityPunctuation", "attributes", SDFDatatype.STRING));
		attributes.add(new SDFAttribute("SecurityPunctuation", "roles", SDFDatatype.STRING));
		attributes.add(new SDFAttribute("SecurityPunctuation", "sign", SDFDatatype.BOOLEAN));
		attributes.add(new SDFAttribute("SecurityPunctuation", "immutable", SDFDatatype.BOOLEAN));

		return SDFSchemaFactory.createNewSchema("SecurityPunctuation", Tuple.class, attributes);

	}

}
