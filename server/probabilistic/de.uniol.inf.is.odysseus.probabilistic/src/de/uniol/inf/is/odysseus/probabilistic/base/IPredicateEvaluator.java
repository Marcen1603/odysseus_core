/**
 * 
 */
package de.uniol.inf.is.odysseus.probabilistic.base;

import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public interface IPredicateEvaluator {
    ProbabilisticTuple<? extends IProbabilistic> evaluate(ProbabilisticTuple<? extends IProbabilistic> input);
}
