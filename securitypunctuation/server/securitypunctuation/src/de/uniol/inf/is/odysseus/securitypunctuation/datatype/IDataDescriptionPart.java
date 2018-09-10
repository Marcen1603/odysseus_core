package de.uniol.inf.is.odysseus.securitypunctuation.datatype;

import java.io.Serializable;
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public interface IDataDescriptionPart extends Serializable {

	/**
	 * Checks if the Security Punctuation, this DDP belongs to, refers to the
	 * object
	 * 
	 * @param object
	 *            object to be checked
	 * @param schema
	 *            InputSchema of the calling Operator
	 * @return true, if this Data Description Part relates to the object false,
	 *         if this DDP doesn't relate to the object
	 */
	public boolean match(IStreamObject<? extends ITimeInterval> object, SDFSchema schema,String tupleRangeAttribute);

	public List<String> getAttributes();
	
	public long[] getTupleRange();
}
