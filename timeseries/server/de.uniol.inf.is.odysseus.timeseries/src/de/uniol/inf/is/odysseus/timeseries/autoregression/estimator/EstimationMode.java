package de.uniol.inf.is.odysseus.timeseries.autoregression.estimator;

import de.uniol.inf.is.odysseus.machine_learning.learner.AbstractLearner;

/**
 * 
 * E. g. for {@link AbstractLearner}, whether the learner should learn or not.
 * 
 * @author Christoph Schröer
 *
 */
public enum EstimationMode {
	ESTIMATING, NON_ESTIMATING;
}
