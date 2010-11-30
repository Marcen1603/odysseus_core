package de.uniol.inf.is.odysseus.logicaloperator;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public interface OutputSchemaSettable {
	public void setOutputSchema(SDFAttributeList outputSchema);
	public void setOutputSchema(SDFAttributeList outputSchema, int port);
}
