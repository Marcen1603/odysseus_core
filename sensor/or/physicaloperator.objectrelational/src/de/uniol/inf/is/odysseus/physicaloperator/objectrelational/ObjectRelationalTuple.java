package de.uniol.inf.is.odysseus.physicaloperator.objectrelational;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.MetaAttributeContainer;
import de.uniol.inf.is.odysseus.objectrelational.base.SDFORDatatypes;
import de.uniol.inf.is.odysseus.objectrelational.base.SetEntry;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeConstraint;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFDatatypeConstraints;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFDatatypes;

/**
 * 
 * Extending the relational tuple features to sets: new comparable code is 
 * need for sets. Furthermore default relational operators could not handle
 * Sets, in future more features are added. Most code have been taken over by
 * relational tuples. 
 * 
 * @author Jendrik Poloczek
 *
 * @param <T>
 * 
 */
public class ObjectRelationalTuple
	<T extends IMetaAttribute> extends MetaAttributeContainer<T> 
	implements Serializable, Comparable<ObjectRelationalTuple<T>> {

	protected SDFAttributeList schema;
	protected Object[] attributes;
	protected char delimiter = '|';
	protected int memSize = -1;
	
	private static final long serialVersionUID = 1L;
	
	public ObjectRelationalTuple(int outputAttributesCount) {
		this.attributes = new Object[outputAttributesCount];
	}

	public ObjectRelationalTuple(
		ObjectRelationalTuple<T> copy
	) {
		super(copy);
		int attributeLength = copy.attributes.length;
		this.attributes = new Object[attributeLength];
		this.schema = copy.getSchema();
		System.arraycopy(
			copy.attributes, 
			0, 
			this.attributes, 
			0,
			attributeLength
		);
		this.delimiter = copy.delimiter;
	}

	public ObjectRelationalTuple(
		SDFAttributeList schema, 
		String line, 
		char delimiter
	) {
		this.attributes = 
			ObjectRelationalTuple.splitLineToAttributes(
			    line, 
			    delimiter, 
			    schema
			);
	}

	public ObjectRelationalTuple(
		SDFAttributeList schema, 
		Object... attributes
	) {
		if (schema.size() != attributes.length) {
			throw new IllegalArgumentException
				("listsize doesn't match schema");
		}
		
		for (int i = 0; i < schema.size(); ++i) {
			if (!checkDataType(attributes[i], schema.get(i))) {
				throw new IllegalArgumentException(
						"attribute " + i + " has an invalid type");
			}
		}
		
		this.schema = schema;
		this.attributes = Arrays.copyOf(attributes, attributes.length);
	}

	public int getAttributeCount() {
		return this.attributes.length;
	}

	@SuppressWarnings("unchecked")
	public final <K> K getAttribute(int pos) {
		return (K) this.attributes[pos];
	}

	public SDFAttributeList getSchema() {
		return this.schema;
	}
	
	public final void setAttribute(int pos, Object value) {
		this.attributes[pos] = value;
	}

	public void setAttributes(Object[] attributes){
		this.attributes = attributes.clone();
	}

	public ObjectRelationalTuple<T> 
		restrict(int[] attrList, boolean createNew) {
		
		Object[] newAttrs = new Object[attrList.length];
		
		for (int i = 0; i < attrList.length; i++) {
			newAttrs[i] = this.attributes[attrList[i]];
		}
		return restrictCreation(createNew, newAttrs);	
	}

	@Override
	@SuppressWarnings("unchecked")
	public int compareTo(ObjectRelationalTuple<T> tuple) {
		int min = tuple.getAttributeCount();
		if (min > this.attributes.length) {
			min = this.attributes.length;
		}
		int compare = 0;
		int i = 0;
		for (i = 0; i < min && compare == 0; i++) {
			try {
				Object objectB = tuple.getAttribute(i);
				if(objectB instanceof SetEntry[]) {
					int containedInA = 0;
					
					List setA = 
						Arrays.asList((SetEntry[]) this.attributes[i]);
					List setB = 
						Arrays.asList((SetEntry[]) objectB);
					
					for(Object b : setB) {
						if(setA.contains(b)) {
							containedInA++;
						}
					}
					if(containedInA == setB.size()) {
						if(containedInA == setA.size()) {
							return 0;
						} else {
							return 1;
						}
					} else {
						return -1;
					}
				} else {
				compare = 
					((Comparable) this.attributes[i]).
						compareTo(tuple.getAttribute(i));
				}
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
		if (compare < 0) {
			compare = (-1) * i;
		}
		if (compare > 0) {
			compare = i;
		}
		return compare;
	}

	@Override
	@SuppressWarnings("unchecked")
	public final boolean equals(Object o) {
		return this.compareTo((ObjectRelationalTuple) o) == 0;
	}

	@Override
	public ObjectRelationalTuple<T> clone() {
		return new ObjectRelationalTuple<T>(this);
	}

	@Override
	public final int hashCode() {
		int ret = 0;
		for (Object o : this.attributes) {
			ret += o.hashCode();
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final String toString() {
		StringBuffer retBuff = new StringBuffer();
		if (attributes.length > 0){
			retBuff.append(this.attributes[0] == null ? "" : this.attributes[0]);
		}else{
			retBuff.append("null");
		}
		for (int i = 1; i < this.attributes.length; ++i) {
			Object curAttribute = this.attributes[i];
			retBuff.append(delimiter);
			
			if(curAttribute == null) {
				retBuff.append("");
			} else {
				String attrStr;
				if(curAttribute instanceof SetEntry[]) {
					attrStr = Arrays.deepToString(((SetEntry[]) curAttribute));
				} else {
					attrStr = curAttribute.toString();
				}
				retBuff.append(attrStr);
			}
		}
		retBuff.append(" | sz="+(memSize==-1?"(-)":memSize));
		retBuff.append(" | META | " + getMetadata());
		return retBuff.toString();
	}
	
	@SuppressWarnings("unchecked")
    private final boolean checkDataType(
	    Object object,
	    SDFAttribute attribute
	) {
	    if (object instanceof SetEntry[]) {
	        return SDFORDatatypes.isSet(attribute.getDatatype());
	    }
	    
		if (object instanceof String) {
			return SDFDatatypes.
			    isString(attribute.getDatatype());
		}
		
		if (object instanceof Integer) {
			Iterator<SDFDatatypeConstraint> i = attribute.getDtConstraints()
					.iterator();
			while (i.hasNext()) {
				SDFDatatypeConstraint constraint = i.next();
				if (SDFDatatypeConstraints.isInteger(constraint)) {
					return true;
				}
			}
			return false;
		}
		if (object instanceof Double) {
			Iterator<SDFDatatypeConstraint> i = attribute.getDtConstraints()
					.iterator();
			while (i.hasNext()) {
				SDFDatatypeConstraint constraint = i.next();
				if (SDFDatatypeConstraints.isRational(constraint)) {
					return true;
				}
			}
			return false;
		}
		return false;
	}

	protected final static Object[] splitLineToAttributes(
			final String line,
			final char delimiter, 
			final SDFAttributeList schema
	) {
		String[] attributes = 
			line.split(Pattern.quote(new String(new char[] { delimiter })));
		
		int count = attributes.length;
		if (count != schema.size()) {
			throw new IllegalArgumentException(
				"invalid number of attributes: got " + count + " expected "
				+ schema.size());
		}
		
		Object[] tokens = new Object[attributes.length];
		for (int i = 0; i < attributes.length; ++i) {
			tokens[i] = convertAttribute(attributes[i], schema.get(i));
		}
		return tokens;
	}

	private final static Object convertAttribute(String stringValue,
			SDFAttribute attribute) {
		if (SDFDatatypes.isString(attribute.getDatatype())) {
			return stringValue;
		}

		if (attribute.getDatatype().getURI(false) == "Integer") {
			return Integer.parseInt(stringValue);
		}
		if (attribute.getDatatype().getURI(false) == "Double") {
			return Double.parseDouble(stringValue);
		}
		// TODO richtig machen mit den datentypen
		if (SDFDatatypes.isNumerical(attribute.getDatatype())) {
			Iterator<SDFDatatypeConstraint> i = attribute.getDtConstraints()
					.iterator();
			while (i.hasNext()) {
				SDFDatatypeConstraint constraint = i.next();
				if (SDFDatatypeConstraints.isInteger(constraint)) {
					return Integer.parseInt(stringValue);
				}
				if (SDFDatatypeConstraints.isRational(constraint)) {
					return Double.parseDouble(stringValue);
				}
			}

			throw new IllegalArgumentException(
				"missing datatype constraint for numerical" +
				"attribute (integer/rational)"
			);
		}

		throw new IllegalArgumentException(
				"attributes of type " + 
				attribute.getDatatype() + 
				" can't be used with " + 
				RelationalTuple.class
		);
	}

	@SuppressWarnings("unchecked")
	private ObjectRelationalTuple<T> restrictCreation(
	        boolean createNew,
			Object[] newAttrs
	) {
		if(createNew) {
		    
			ObjectRelationalTuple<T> newTuple = 
				new ObjectRelationalTuple<T>(newAttrs.length);
			
			newTuple.setAttributes(newAttrs);
			newTuple.setMetadata((T) this.getMetadata().clone());
			
			return newTuple;
			
		} else {
			this.attributes = newAttrs;
			return this;
		}
	}
	
}
