package de.uniol.inf.is.odysseus.securitypunctuation.datatype;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;

public class SecurityPunctuation extends AbstractSecurityPunctuation {
	private static final long serialVersionUID = 7436458563105118060L;

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

	public SecurityPunctuation(long[] ts, String attributes, String roles, boolean sign, boolean immutable,
			PointInTime timestamp) {
		super(timestamp);
		this.ddp = new DataDescriptionPart(ts, attributes);
		this.srp = new SecurityRestrictionPart(roles);
		this.immutable = immutable;
		this.sign = sign;
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

	// checks if an element out of the given list of roles is contained in this
	// sp
	public boolean checkRole(List<String> roles) {
		for (String role : roles) {
			if (this.srp.getRoles().get(0).equals("*")) {
				return true;
			} else if (this.srp.getRoles().contains(role)) {
				return true;
			}
		}
		return false;
	}

	public SecurityPunctuation intersect(ISecurityPunctuation punctuation, SDFSchema schema, SDFSchema otherSchema,
			PointInTime ts) {

		List<String> attributes = new ArrayList<>();
		List<String> roles = new ArrayList<>();

		SecurityPunctuation newSP = new SecurityPunctuation("-2,-2", "", "", false, false, ts);

		if (this.sign != punctuation.getSign()) {
			return newSP;
		}
		if (this.immutable != punctuation.getImmutable()) {
			return newSP;
		}

		// union attributes for join
		if (schema != null && otherSchema != null) {
			attributes = unionJoinAttributes(punctuation.getDDP(), schema, otherSchema);
		}
		// union attribiutes for SPAnalyzer
		else {
			attributes = unionAttributes(punctuation.getDDP());
		}
		if (attributes.isEmpty()) {
			return newSP;
		}
		// intersect roles
		roles = mergeStrings(this.getSRP().getRoles(), punctuation.getSRP().getRoles());
		if (roles.isEmpty()) {
			return newSP;
		}

		return newSP = new SecurityPunctuation("*", attributes, roles, this.sign, this.immutable, ts);

	}

	public List<String> unionAttributes(IDataDescriptionPart ddp) {
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

	public List<String> unionJoinAttributes(IDataDescriptionPart ddp, SDFSchema schema, SDFSchema otherSchema) {
		List<String> attributes = new ArrayList<>();
		if (!ddp.getAttributes().get(0).equals("*") && !this.ddp.getAttributes().get(0).equals("*")) {
			
			//if both SPs dont contain attributes
			if (StringUtils.isBlank(ddp.getAttributes().get(0))
					&& StringUtils.isBlank(this.ddp.getAttributes().get(0))) {
				return attributes;
			} else if (StringUtils.isBlank(ddp.getAttributes().get(0))
					&& !StringUtils.isBlank(this.ddp.getAttributes().get(0))) {
				attributes = new ArrayList<>(this.ddp.getAttributes());
				return attributes;

			} else if (!StringUtils.isBlank(ddp.getAttributes().get(0))
					&& StringUtils.isBlank(this.ddp.getAttributes().get(0))) {
				attributes = new ArrayList<>(ddp.getAttributes());
				return attributes;
			}
			attributes = new ArrayList<>(ddp.getAttributes());
			for (String inputAttribute : this.ddp.getAttributes()) {
				if (!attributes.contains(inputAttribute)) {
					attributes.add(inputAttribute);
				}

			}
			return attributes;
		} else if (this.ddp.getAttributes().get(0).equals("*") || ddp.getAttributes().get(0).equals("*")) {
			if (this.ddp.getAttributes().get(0).equals("*") && !ddp.getAttributes().get(0).equals("*")) {
				for (SDFAttribute attribute : otherSchema) {
					attributes.add(attribute.getAttributeName());

				}
				if (!StringUtils.isBlank(ddp.getAttributes().get(0))) {
					for (String inputAttribute : ddp.getAttributes()) {
						if (!attributes.contains(inputAttribute)) {
							attributes.add(inputAttribute);
						}

					}
				}
				return attributes;

			} else if (ddp.getAttributes().get(0).equals("*") && !this.ddp.getAttributes().get(0).equals("*")) {
				for (SDFAttribute attribute : schema) {
					attributes.add(attribute.getAttributeName());
				}
				if (!StringUtils.isBlank(this.ddp.getAttributes().get(0))) {
					for (String inputAttribute : this.ddp.getAttributes()) {
						if (!attributes.contains(inputAttribute)) {
							attributes.add(inputAttribute);
						}
					}
				}
				return attributes;
			} else {
				attributes.add("*");
			}

		}

		return attributes;
	}

	private List<String> mergeStrings(List<String> inputOne, List<String> inputTwo) {
		List<String> output = new ArrayList<>();
		if (inputOne.get(0).equals("") || inputTwo.get(0).equals("")) {
			return output;

		} else if (inputOne.get(0).equals("*")) {
			output = new ArrayList<>(inputTwo);
			return output;
		} else if (inputTwo.get(0).equals("*")) {
			output = new ArrayList<>(inputTwo);
			return output;
		} else {
			output = new ArrayList<>(inputOne);
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
		// result = prime * result + ((timestamp == null) ? 0 :
		// timestamp.hashCode());
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
		// ignore the timestamp
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
	public AbstractSecurityPunctuation createEmptySP(PointInTime timestamp) {
		return new SecurityPunctuation("-2,-2", "", "", false, false, timestamp);
	}

	@Override
	public AbstractPunctuation clone(PointInTime newTime) {
		return new SecurityPunctuation(this);
	}

	@Override
	public Tuple<?> restrictObject(Tuple<?> object, SDFSchema schema, List<ISecurityPunctuation> matchingSPs,
			String tupleRangeAttribute) {
		List<String> attributes = new ArrayList<String>();
		attributes.add(tupleRangeAttribute);
		for (ISecurityPunctuation sp : matchingSPs) {
			for (String str : sp.getDDP().getAttributes()) {
				if (str.equals("*")) {
					return object;
				} else if (!attributes.contains(str)) {
					attributes.add(str);
				}
			}
		}
		for (int i = 0; i < schema.size(); i++) {
			String attribute = schema.get(i).getAttributeName();
			if (!attributes.contains(attribute) && !attribute.equals(tupleRangeAttribute)) {
				object.setAttribute(i, null);
			}

		}
		return object;
	}

	@Override
	public List<ISecurityPunctuation> union(List<ISecurityPunctuation> buffer) {
		for (ISecurityPunctuation sp : buffer) {

			if (sp.getImmutable() == this.getImmutable() && sp.getSign() == this.getSign()) {
				// if the DDPs of the SPs are the same, they get unioned,
				// i.e. the Roles in their SRP are unioned
				if (this.getDDP().equals(sp.getDDP())) {
					sp.getSRP().unionRoles(this.getSRP());
					return buffer;
				}
				// if the roles of the SPs are the same and the tupleRange
				// are the same, the SPs are intersected, resulting in a
				// union of the attributes in the DDP
				else if (this.getSRP().equals(sp.getSRP())
						&& (this.getDDP().getTupleRange()[0] == sp.getDDP().getTupleRange()[0]
								&& this.getDDP().getTupleRange()[1] == sp.getDDP().getTupleRange()[1])) {
					SecurityPunctuation punctuation = new SecurityPunctuation(
							this.intersect(sp, null, null, sp.getTime()));
					punctuation.setDDP(new DataDescriptionPart(
							String.valueOf(sp.getDDP().getTupleRange()[0]) + ","
									+ String.valueOf(sp.getDDP().getTupleRange()[1]),
							punctuation.getDDP().getAttributes()));

					buffer.add(punctuation);

					buffer.remove(sp);
					return buffer;
				}

			}

		}
		buffer.add(this);

		return buffer;

	}

}
