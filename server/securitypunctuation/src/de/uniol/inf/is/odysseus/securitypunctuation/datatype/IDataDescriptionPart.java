package de.uniol.inf.is.odysseus.securitypunctuation.datatype;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public interface IDataDescriptionPart {

	/**
	 * Checks if the Security Punctuation this DDP belongs to refers to the
	 * object
	 * 
	 * @param object
	 *            object to be checked
	 * @param schema
	 *            InputSchema of the calling Operator
	 * @return true, if this Data Description Part relates to the object false,
	 *         if this DDP doesn't relate to the object
	 */
	public boolean match(IStreamObject<?> object, SDFSchema schema);
}
