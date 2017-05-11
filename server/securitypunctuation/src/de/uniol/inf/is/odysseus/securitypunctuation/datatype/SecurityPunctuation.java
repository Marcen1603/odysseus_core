package de.uniol.inf.is.odysseus.securitypunctuation.datatype;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator.SecurityShieldPO;

public class SecurityPunctuation extends AbstractSecurityPunctuation {
	private static final Logger LOG = LoggerFactory.getLogger(SecurityShieldPO.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 7436458563105118060L;

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

	public SecurityPunctuation(int[] tupleRange, String attributes, String roles, boolean sign, boolean immutable,
			long timestamp) {
		this.ddp = new DataDescriptionPart(tupleRange, attributes);
		this.srp = new SecurityRestrictionPart(roles);
		this.immutable = immutable;
		this.sign = sign;
		this.timestamp = new PointInTime(timestamp);
	}

	public SecurityPunctuation(int[] tupleRange, String attributes, String roles, boolean sign, boolean immutable,
			PointInTime timestamp) {
		this.ddp = new DataDescriptionPart(tupleRange, attributes);
		this.srp = new SecurityRestrictionPart(roles);
		this.immutable = immutable;
		this.sign = sign;
		this.timestamp = timestamp;
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
	public AbstractSecurityPunctuation clone() {
		return new SecurityPunctuation(this);
	}

	@Override
	public String toString() {
		return ddp.toString() + ";" + srp.toString() + "; Sign: " + sign + "; Immutable: " + immutable + "; TS: "
				+ timestamp;
	}

	// muss überschrieben werden für andere Arten von SPs
	public AbstractSecurityPunctuation intersectPunctuation(AbstractSecurityPunctuation punctuation) {
		String attributes = "";
		String roles = "";
		int[] tupleRange = new int[2];
		SecurityPunctuation newSP = new SecurityPunctuation("-2,-2", "", "", false, false, -1L);

		if (this.sign != punctuation.getSign()) {
			return newSP;
		}
		if (this.immutable != punctuation.getImmutable()) {
			return newSP;
		}

		// intersect attributes

		attributes = mergeStrings(punctuation.getDDP().getAttributes(), this.getDDP().getAttributes());
		if (attributes.equals("")) {
			return newSP;
		}

		// intersect roles
		roles = mergeStrings(this.getSRP().getRoles(), punctuation.getSRP().getRoles());
		if (roles.equals("")) {
			return newSP;
		}

		// intersect tupleRange
		if (this.ddp.getTupleRange()[0] == -2 && this.ddp.getTupleRange()[1] == -2
				|| punctuation.getDDP().getTupleRange()[0] == -2 && punctuation.getDDP().getTupleRange()[1] == -2
				|| this.ddp.getTupleRange()[0] > punctuation.getDDP().getTupleRange()[1]
				|| this.ddp.getTupleRange()[1] < punctuation.getDDP().getTupleRange()[0]) {
			return newSP;

		} else if (this.ddp.getTupleRange()[0] != -1 && punctuation.getDDP().getTupleRange()[0] != -1) {

			if (this.ddp.getTupleRange()[0] >= punctuation.getDDP().getTupleRange()[0]) {
				tupleRange[0] = this.ddp.getTupleRange()[0];
			} else
				tupleRange[0] = punctuation.getDDP().getTupleRange()[0];
			if (this.ddp.getTupleRange()[1] <= punctuation.getDDP().getTupleRange()[1]) {
				tupleRange[1] = this.ddp.getTupleRange()[1];
			} else
				tupleRange[1] = punctuation.getDDP().getTupleRange()[1];
		} else if (this.ddp.getTupleRange()[0] == -1) {
			tupleRange = punctuation.getDDP().getTupleRange();
		} else if (punctuation.getDDP().getTupleRange()[0] == -1) {
			tupleRange = this.ddp.getTupleRange();
		}

		return newSP = new SecurityPunctuation(tupleRange, attributes, roles, this.sign, this.immutable,
				this.timestamp);

	}

	private String mergeStrings(List<String> inputOne, List<String> inputTwo) {
		String output = "";
		// falls eines der gemergten Attribute leer ist, ist die Schnittmenge
		// der Attribute auch leer, somit die SP ungültig
		if (inputOne.get(0).equals("") || inputTwo.get(0).equals("")) {
			return output;

		} else if (inputOne.get(0).equals("*")) {
			for (String inputAttributeTwo : inputTwo) {
				output += inputAttributeTwo;
			}
			return output;
		} else if (inputTwo.get(0).equals("*")) {
			for (String inputAttributeOne : inputOne) {
				output += inputAttributeOne;
			}
			return output;
		} else {
			for (String role : inputOne) {
				for (String inputRole : inputTwo) {
					if (role.equals(inputRole)) {
						output += role;
					}

				}
			}
		}

		return output;
	}
	public boolean isEmpty() {
		if (this.equals(new SecurityPunctuation("-2,-2", "", "", false, false, -1L))) {
			return true;
		} else
			return false;
	}
	

//	@Override
//	public SDFSchema getSchema() {
//		List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
//
//		attributes.add(new SDFAttribute("SecurityPunctuation", "tupleRange", SDFDatatype.STRING));
//		attributes.add(new SDFAttribute("SecurityPunctuation", "attributes", SDFDatatype.STRING));
//		attributes.add(new SDFAttribute("SecurityPunctuation", "roles", SDFDatatype.STRING));
//		attributes.add(new SDFAttribute("SecurityPunctuation", "sign", SDFDatatype.BOOLEAN));
//		attributes.add(new SDFAttribute("SecurityPunctuation", "immutable", SDFDatatype.BOOLEAN));
//		attributes.add(new SDFAttribute("SecurityPunctuation", "timestamp", SDFDatatype.TIMESTAMP));
//		return SDFSchemaFactory.createNewSchema("SecurityPunctuation", Tuple.class, attributes);
//
//	}

}
