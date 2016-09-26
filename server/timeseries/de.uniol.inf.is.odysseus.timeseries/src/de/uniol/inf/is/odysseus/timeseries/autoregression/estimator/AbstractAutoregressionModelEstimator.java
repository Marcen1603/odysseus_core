package de.uniol.inf.is.odysseus.timeseries.autoregression.estimator;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.machine_learning.learner.AbstractLearner;

/**
 * 
 * This is an abstract learner for autoregression models like GARCH
 * 
 * In time series context, the type of predictions is Double. 
 * 
 * @author Christoph Schröer
 *
 */
abstract public class AbstractAutoregressionModelEstimator
		extends AbstractLearner<Tuple<ITimeInterval>, ITimeInterval, Double> {

}
