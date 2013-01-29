package de.uniol.inf.is.odysseus.probabilistic.base.predicate;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public interface IProbabilisticPredicate extends
		IPredicate<ProbabilisticTuple<?>> {
	public void init(SDFSchema leftSchema, SDFSchema rightSchema);

	public void replaceAttribute(SDFAttribute curAttr, SDFAttribute newAttr);
}
