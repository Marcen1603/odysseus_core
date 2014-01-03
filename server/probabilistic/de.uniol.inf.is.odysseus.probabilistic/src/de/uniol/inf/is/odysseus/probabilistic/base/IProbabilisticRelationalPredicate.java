/**
 * 
 */
package de.uniol.inf.is.odysseus.probabilistic.base;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public interface IProbabilisticRelationalPredicate extends IPredicate<ProbabilisticTuple<? extends IProbabilistic>> {
    void init(SDFSchema leftSchema, SDFSchema rightSchema);

    void replaceAttribute(SDFAttribute curAttr, SDFAttribute newAttr);

}
