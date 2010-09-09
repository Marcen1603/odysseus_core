package de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.test.nest.fixtures;

import java.util.List;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.ObjectTrackingMetadata;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public interface Factory {
	public SDFAttributeList getInputSchema();
	public SDFAttributeList getOutputSchema();
	public SDFAttributeList getGroupingAttributes();
	public SDFAttribute getNestingAttribute();
	public List<MVRelationalTuple<ObjectTrackingMetadata<Object>>> getInputTuples();
}
