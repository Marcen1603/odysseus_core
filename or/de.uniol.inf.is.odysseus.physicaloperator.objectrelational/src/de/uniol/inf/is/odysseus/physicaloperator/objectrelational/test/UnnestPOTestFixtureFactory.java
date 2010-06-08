package de.uniol.inf.is.odysseus.physicaloperator.objectrelational.test;

import java.util.List;

import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.ObjectRelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public interface UnnestPOTestFixtureFactory {
    public SDFAttributeList getInputSchema();
    public SDFAttributeList getOutputSchema();
    public SDFAttribute getNestingAttribute();
    public List<ObjectRelationalTuple<TimeInterval>> getInputTuples();
}
