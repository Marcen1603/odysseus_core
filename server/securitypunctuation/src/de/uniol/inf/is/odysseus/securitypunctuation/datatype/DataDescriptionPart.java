package de.uniol.inf.is.odysseus.securitypunctuation.datatype;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public class DataDescriptionPart implements IDataDescriptionPart{
	private static final Logger LOG = LoggerFactory.getLogger(DataDescriptionPart.class);
	// [0] is the start of the tupleRange, [1] is the end
	// if tuple ranges=-1 access to all tuples is allowed if tuple ranges=-2
	// access to no tuples is allowed
	private int[] tupleRange;
	private List<String> attributes;

	

	public DataDescriptionPart(String tupleRange, String attributes) {
		this.tupleRange = tupleRangeToInt(tupleRange);
		this.attributes = new ArrayList<String>(Arrays.asList(attributes.split(",")));

	}

	public DataDescriptionPart(DataDescriptionPart ddp) {
		this.tupleRange = ddp.getTupleRange();
		this.attributes = ddp.getAttributes();
	}

	public DataDescriptionPart(int[] tupleRange2, String attributes2) {
		this.tupleRange = tupleRange2;
		this.attributes = new ArrayList<String>(Arrays.asList(attributes2.split(",")));
	}

	public List<String> getAttributes() {
		return this.attributes;
	}

	public int[] getTupleRange() {
		return this.tupleRange;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attributes == null) ? 0 : attributes.hashCode());
		result = prime * result + Arrays.hashCode(tupleRange);
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
		DataDescriptionPart other = (DataDescriptionPart) obj;
		if (attributes == null) {
			if (other.attributes != null)
				return false;
		} else if (!compare(this.attributes, other.getAttributes()))
			return false;
		if (tupleRange == null) {
			if (other.tupleRange != null)
				return false;
		} else if (tupleRange[0] != other.getTupleRange()[0] && tupleRange[1] != other.getTupleRange()[1])
			return false;
		return true;
	}

	@Override
	public String toString() {
		String str = "TupleRange: [";
		str += tupleRange[0] + "," + tupleRange[1];
		str += "]";
		str += "Attributes: [";
		for (String a : attributes) {
			str += a + ",";
		}
		str += "]";
		return str;
	}

	private boolean compare(List<String> inputOne, List<String> inputTwo) {
		if (!inputOne.isEmpty() && inputTwo.isEmpty() || inputOne.isEmpty() && inputTwo.isEmpty()) {
			return false;

		} else if (inputTwo.containsAll(inputOne) && inputOne.containsAll(inputTwo)) {
			return true;
		} else
			return false;

	}

	// writes the tupleRange into an int array if range=-2 no access to the
	// tuples is allowed if range=-1 access to all tuples is allowed
	private int[] tupleRangeToInt(String tupleRange) {
		int[] range = new int[2];
		if (StringUtils.isBlank(tupleRange)) {
			range[0] = -2;
			range[1] = -2;
			return range;
		} else if (!tupleRange.equals("*")) {
			String[] str = tupleRange.split(",");
			range[0] = Integer.parseInt(str[0]);
			range[1] = Integer.parseInt(str[1]);

		} else if (tupleRange.equals("*")) {
			range[0] = -1;
			range[1] = -1;
		}
		return range;
	}

	/**
	 * @param object
	 * @param schema
	 * @return
	 */
	public boolean match(IStreamObject<?> object, SDFSchema schema) {
		boolean match = false;
		Tuple<?> obj = (Tuple<?>) object;
		if(StringUtils.isBlank(this.getAttributes().get(0))){
			return false;
		}
		if (this.attributes.get(0).equals("*")) {
			match = true;
		}

		for (int i = 0; i < schema.size(); i++) {
			
			if (!schema.get(i).getAttributeName().equals("id") && !schema.get(i).getAttributeName().equals("TS")
					&& !schema.getAttribute(i).getAttributeName().equals("start")
					&& !schema.getAttribute(i).getAttributeName().equals("end")) {
				if (!this.attributes.contains(schema.get(i).getAttributeName())) {
					return false;
				}
			}
		}
		match = true;

		// checks if the TupleID is within the tupleRange of the SP
		int idIndex = schema.findAttributeIndex("id");
		if (tupleRange[0] == -1 && tupleRange[1] == -1 && match == true) {
			return true;
		} else if (tupleRange[0] == -2 || tupleRange[1] == -2) {
			return false;
		} else if (((long) obj.getAttribute(idIndex)) >= (long)tupleRange[0]
				&& ((long) obj.getAttribute(idIndex)) <= (long)tupleRange[1] && match == true) {

			return true;

		} else
			return false;

	}
}
