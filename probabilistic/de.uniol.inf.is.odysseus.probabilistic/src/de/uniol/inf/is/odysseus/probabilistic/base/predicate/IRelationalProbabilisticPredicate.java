package de.uniol.inf.is.odysseus.probabilistic.base.predicate;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public interface IRelationalProbabilisticPredicate extends IProbabilisticPredicate<Tuple<?>> {
    public void init(SDFSchema leftSchema, SDFSchema rightSchema);
    public void replaceAttribute(SDFAttribute curAttr, SDFAttribute newAttr);
}
