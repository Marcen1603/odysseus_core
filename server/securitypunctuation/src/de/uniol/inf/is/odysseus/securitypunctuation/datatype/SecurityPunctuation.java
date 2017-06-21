package de.uniol.inf.is.odysseus.securitypunctuation.datatype;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator.SecurityShieldPO;

public class SecurityPunctuation extends AbstractPunctuation implements ISecurityPunctuation{
	private static final Logger LOG = LoggerFactory.getLogger(SecurityShieldPO.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 7436458563105118060L;
	
	private DataDescriptionPart ddp;
	private SecurityRestrictionPart srp;
	private boolean sign;
	private boolean immutable;
	private PointInTime timestamp;

	public SecurityPunctuation(SecurityPunctuation securityPunctuation) {
		super(securityPunctuation);
		this.ddp = securityPunctuation.getDDP();
		this.srp = securityPunctuation.getSRP();
		this.immutable = securityPunctuation.getImmutable();
		this.sign = securityPunctuation.getSign();
		this.timestamp = securityPunctuation.getTime();
	}

	public SecurityPunctuation(DataDescriptionPart ddp, SecurityRestrictionPart srp, boolean sign, boolean immutable,
			long timestamp) {
		super(timestamp);
		this.ddp = ddp;
		this.srp = srp;
		this.immutable = immutable;
		this.sign = sign;
		this.timestamp = new PointInTime(timestamp);
	}

	public SecurityPunctuation(String tupleRange, String attributes, String roles, boolean sign, boolean immutable,
			long timestamp) {
		super(timestamp);
		this.ddp = new DataDescriptionPart(tupleRange, attributes);
		this.srp = new SecurityRestrictionPart(roles);
		this.immutable = immutable;
		this.sign = sign;
		this.timestamp = new PointInTime(timestamp);
	}

	public SecurityPunctuation(String tupleRange, List<String> attributes, List<String> roles, boolean sign,
			boolean immutable, PointInTime timestamp) {
		super(timestamp);
		this.ddp = new DataDescriptionPart(tupleRange, attributes);
		this.srp = new SecurityRestrictionPart(roles);
		this.immutable = immutable;
		this.sign = sign;
		this.timestamp = new PointInTime(timestamp);
	}

	public SecurityPunctuation(String tupleRange, String attributes, String roles, boolean sign, boolean immutable,
			PointInTime timestamp) {
		super(timestamp);
		this.ddp = new DataDescriptionPart(tupleRange, attributes);
		this.srp = new SecurityRestrictionPart(roles);
		this.immutable = immutable;
		this.sign = sign;
		this.timestamp = timestamp;
	}

	public SecurityPunctuation(long[] ts, String attributes, String roles, boolean sign, boolean immutable, PointInTime timestamp) {
		super(timestamp);
		this.ddp=new DataDescriptionPart(ts,attributes);
		this.srp = new SecurityRestrictionPart(roles);
		this.immutable = immutable;
		this.sign = sign;
		this.timestamp = timestamp;
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
	public SecurityPunctuation clone() {
		return new SecurityPunctuation(this);
	}

	@Override
	public String toString() {
		return ddp.toString() + ";" + srp.toString() + "; Sign: " + sign + "; Immutable: " + immutable + "; TS: "
				+ timestamp;
	}

	public SecurityPunctuation intersect(ISecurityPunctuation punctuation,PointInTime ts) {
		List<String> attributes = new ArrayList<>();
		List<String> roles = new ArrayList<>();

		SecurityPunctuation newSP = new SecurityPunctuation("-2,-2", "", "", false, false, ts);

		if (this.sign != punctuation.getSign()) {
			return newSP;
		}
		if (this.immutable != punctuation.getImmutable()) {
			return newSP;
		}

		// union attributes
		attributes=unionAttributes(punctuation.getDDP());
		// intersect roles
		roles = mergeStrings(this.getSRP().getRoles(), punctuation.getSRP().getRoles());
		if (roles.isEmpty()) {
			return newSP;
		}

		return newSP = new SecurityPunctuation("*", attributes, roles, this.sign, this.immutable, ts);

	}

	public List<String> unionAttributes(DataDescriptionPart ddp) {
		List<String> attributes = new ArrayList<>();

		if (this.ddp.getAttributes().get(0).equals("*") || ddp.getAttributes().get(0).equals("*")) {
			attributes.add("*");
			return attributes;
		} else if (StringUtils.isBlank(ddp.getAttributes().get(0))) {
			attributes = new ArrayList<>(this.ddp.getAttributes());
			return attributes;
		} else if (StringUtils.isBlank(this.ddp.getAttributes().get(0))) {
			attributes = new ArrayList<>(ddp.getAttributes());
			return attributes;
		} else
			attributes = new ArrayList<>(this.ddp.getAttributes());
		for (String inputAttribute : ddp.getAttributes()) {
			if (!attributes.contains(inputAttribute)) {
				attributes.add(inputAttribute);
			}
		}
		return attributes;
	}

	private List<String> mergeStrings(List<String> inputOne, List<String> inputTwo) {
		List<String> output = new ArrayList<>();
		if (inputOne.get(0).equals("") || inputTwo.get(0).equals("")) {
			return output;

		} else if (inputOne.get(0).equals("*")) {
			output=new ArrayList<>(inputTwo);
			return output;
		} else if (inputTwo.get(0).equals("*")) {
			output=new ArrayList<>(inputTwo);
			return output;
		} else {
			output=new ArrayList<>(inputOne);
			output.retainAll(inputTwo);
			
		}

		return output;
	}

	public boolean isEmpty() {
		if (this.equals(new SecurityPunctuation("-2,-2", "", "", false, false, -1L))) {
			return true;
		} else
			return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ddp == null) ? 0 : ddp.hashCode());
		result = prime * result + (immutable ? 1231 : 1237);
		result = prime * result + (sign ? 1231 : 1237);
		result = prime * result + ((srp == null) ? 0 : srp.hashCode());
		//result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
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
		SecurityPunctuation other = (SecurityPunctuation) obj;
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
		//ignore the timestamp
		// if (timestamp == null) {
		// if (other.timestamp != null)
		// return false;
		// } else if (!timestamp.equals(other.timestamp))
		// return false;
		return true;
	}



	@Override
	public SDFSchema getSchema() {
		List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
		attributes.add(new SDFAttribute("SecurityPunctuation", "tupleRange", SDFDatatype.MATRIX_INTEGER));
		attributes.add(new SDFAttribute("SecurityPunctuation", "attributes", SDFDatatype.LIST_STRING));
		attributes.add(new SDFAttribute("SecurityPunctuation", "roles", SDFDatatype.LIST_STRING));
		attributes.add(new SDFAttribute("SecurityPunctuation", "sign", SDFDatatype.BOOLEAN));
		attributes.add(new SDFAttribute("SecurityPunctuation", "immutable", SDFDatatype.BOOLEAN));
		attributes.add(new SDFAttribute("SecurityPunctuation", "point", SDFDatatype.TIMESTAMP));
		return SDFSchemaFactory.createNewSchema("SecurityPunctuation", SecurityPunctuation.class, attributes);
	}

	@Override
	public Tuple<?> getValue() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ISecurityPunctuation createEmptySP() {
		return new SecurityPunctuation("-2,-2","","",false,false,-1L);
	}

	@Override
	public AbstractPunctuation clone(PointInTime newTime) {
		return new SecurityPunctuation(this);
	}

	
	

}
