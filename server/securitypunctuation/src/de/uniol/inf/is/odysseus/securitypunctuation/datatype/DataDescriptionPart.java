package de.uniol.inf.is.odysseus.securitypunctuation.datatype;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public class DataDescriptionPart implements IDataDescriptionPart {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8776732310776468142L;


	// [0] is the start of the tupleRange, [1] is the end
	// if tuple ranges=-1 access to all tuples is allowed if tuple ranges=-2
	// access to no tuples is allowed
	private long[] tupleRange;
	private List<String> attributes;

	public DataDescriptionPart(String tupleRange, List<String> attributes) {
		this.tupleRange = tupleRangeToInt(tupleRange);
		this.attributes = attributes;
		Collections.sort(this.attributes);
	}

	public DataDescriptionPart(String tupleRange, String attributes) {
		this.tupleRange = tupleRangeToInt(tupleRange);
		this.attributes = new ArrayList<String>(Arrays.asList(attributes.split(",")));
		Collections.sort(this.attributes);
	}

	public DataDescriptionPart(DataDescriptionPart ddp) {
		this.tupleRange = ddp.getTupleRange();
		this.attributes = ddp.getAttributes();
		Collections.sort(this.attributes);
	}

	public DataDescriptionPart(long[] tupleRange, String attributes) {
		this.tupleRange = tupleRange;
		this.attributes = new ArrayList<String>(Arrays.asList(attributes.split(",")));
		Collections.sort(this.attributes);
	}

	public List<String> getAttributes() {
		return this.attributes;
	}

	public long[] getTupleRange() {
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
		} else if (tupleRange[0] != other.getTupleRange()[0] || tupleRange[1] != other.getTupleRange()[1])
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
		if (inputOne.size() != inputTwo.size()) {
			return false;
		} else if (!inputOne.equals(inputTwo)) {
			return false;
		}
		return true;


	}

	// writes the tupleRange into an int array if range=-2 no access to the
	// tuples is allowed if range=-1 access to all tuples is allowed
	private long[] tupleRangeToInt(String tupleRange) {
		long[] range = new long[2];
		if (StringUtils.isBlank(tupleRange)) {
			range[0] = -2;
			range[1] = -2;
			return range;
		} else if (!tupleRange.equals("*")) {
			String[] str = tupleRange.split(",");
			range[0] = Long.valueOf(str[0]);
			range[1] = Long.valueOf(str[1]);

		} else if (tupleRange.equals("*")) {
			range[0] = -1;
			range[1] = -1;
		}
		return range;
	}


	public boolean match(IStreamObject<? extends ITimeInterval> object, SDFSchema schema, String tupleRangeAttribute) {

		 // checks if the TupleID is within the tupleRange of the SP
		
		Tuple<?> obj = (Tuple<?>) object;
		if (tupleRange[0] == -1L && tupleRange[1] == -1L) {
			return true;
		} else if (tupleRange[0] == -2L || tupleRange[1] == -2L) {
			return false;
		}
		if (tupleRangeAttribute == null) {
			if ((object.getMetadata().getStart()).afterOrEquals(new PointInTime(tupleRange[0]))
					&& (object.getMetadata().getStart()).beforeOrEquals(new PointInTime(tupleRange[1]))) {

				return true;
			} else
				return false;
		} else {
			int index = schema.findAttributeIndex(tupleRangeAttribute);
			long tupleID = obj.getAttribute(index).getClass().equals(Integer.class)
					? ((Integer) obj.getAttribute(index)).longValue() : (long) obj.getAttribute(index);

			if (tupleID >= tupleRange[0] && tupleID <= tupleRange[1]) {
				return true;
			}

			return false;
			
		}

	}
}
