package de.uniol.inf.is.odysseus.securitypunctuation.datatype;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator.SecurityShieldPO;

public class SecurityPunctuation implements ISecurityPunctuation {
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

	public SecurityPunctuation(String tupleRange, String attributes, String roles, boolean sign, boolean immutable,
			long timestamp) {
		this.ddp = new DataDescriptionPart(tupleRange, attributes);
		this.srp = new SecurityRestrictionPart(roles);
		this.immutable = immutable;
		this.sign = sign;
		this.timestamp = new PointInTime(timestamp);
	}

	public SecurityPunctuation(String tupleRange, List<String> attributes, List<String> roles, boolean sign,
			boolean immutable, PointInTime timestamp) {
		this.ddp = new DataDescriptionPart(tupleRange, attributes);
		this.srp = new SecurityRestrictionPart(roles);
		this.immutable = immutable;
		this.sign = sign;
		this.timestamp = new PointInTime(timestamp);
	}

	public SecurityPunctuation(String tupleRange, String attributes, String roles, boolean sign, boolean immutable,
			PointInTime timestamp) {
		this.ddp = new DataDescriptionPart(tupleRange, attributes);
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

	public SecurityPunctuation intersect(ISecurityPunctuation punctuation) {
		List<String> attributes = new ArrayList<>();
		List<String> roles = new ArrayList<>();

		SecurityPunctuation newSP = new SecurityPunctuation("-2,-2", "", "", false, false, -1L);

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

		// intersect tupleRange
		// if (this.ddp.getTupleRange()[0] == -2 && this.ddp.getTupleRange()[1]
		// == -2
		// || punctuation.getDDP().getTupleRange()[0] == -2 &&
		// punctuation.getDDP().getTupleRange()[1] == -2
		// || this.ddp.getTupleRange()[0] >
		// punctuation.getDDP().getTupleRange()[1]
		// || this.ddp.getTupleRange()[1] <
		// punctuation.getDDP().getTupleRange()[0]) {
		// return newSP;
		//
		// } else if (this.ddp.getTupleRange()[0] != -1 &&
		// punctuation.getDDP().getTupleRange()[0] != -1) {
		//
		// if (this.ddp.getTupleRange()[0] >=
		// punctuation.getDDP().getTupleRange()[0]) {
		// tupleRange[0] = this.ddp.getTupleRange()[0];
		// } else
		// tupleRange[0] = punctuation.getDDP().getTupleRange()[0];
		// if (this.ddp.getTupleRange()[1] <=
		// punctuation.getDDP().getTupleRange()[1]) {
		// tupleRange[1] = this.ddp.getTupleRange()[1];
		// } else
		// tupleRange[1] = punctuation.getDDP().getTupleRange()[1];
		// } else if (this.ddp.getTupleRange()[0] == -1) {
		// tupleRange = punctuation.getDDP().getTupleRange();
		// } else if (punctuation.getDDP().getTupleRange()[0] == -1) {
		// tupleRange = this.ddp.getTupleRange();
		// }

		return newSP = new SecurityPunctuation("*", attributes, roles, this.sign, this.immutable, new PointInTime(this.timestamp));

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
			attributes = new ArrayList(this.ddp.getAttributes());
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
//			for (String inputAttributeTwo : inputTwo) {
//				output.add(inputAttributeTwo);
//			}#
			output=new ArrayList(inputTwo);
			return output;
		} else if (inputTwo.get(0).equals("*")) {
			output=new ArrayList(inputTwo);
//			for (String inputAttributeOne : inputOne) {
//				output.add(inputAttributeOne);
//			}
			return output;
		} else {
			output=new ArrayList(inputOne);
			output.retainAll(inputTwo);
			
//			for (String inputAttributeOne : inputOne) {
//				for (String inputAttributeTwo : inputTwo) {
//					if (inputAttributeOne.equals(inputAttributeTwo)) {
//						output.add(inputAttributeOne);
//					}
//
//				}
//			}
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
		// if (timestamp == null) {
		// if (other.timestamp != null)
		// return false;
		// } else if (!timestamp.equals(other.timestamp))
		// return false;
		return true;
	}

	@Override
	public boolean isHeartbeat() {
		// TODO Auto-generated method stub
		return false;
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

	@Override
	public boolean before(PointInTime time) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean beforeOrEquals(PointInTime time) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean after(PointInTime time) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean afterOrEquals(PointInTime time) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPunctuation() {
		// TODO Auto-generated method stub
		return true;
	}

	
	

}
