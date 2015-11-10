package de.uniol.inf.is.odysseus.codegenerator.operator.rule;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimeWindowAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;

/**
 * abstract rule for the TimeWindowAO (SlidingPeriodicWindow)
 * 
 * @author MarcPreuschaft
 *
 * @param <T>
 */
public abstract class AbstractCSlidingPeriodicWindowTIPORule<T extends TimeWindowAO> extends
		AbstractCOperatorRule<TimeWindowAO> {

	public AbstractCSlidingPeriodicWindowTIPORule(String name) {
		super(name);
	}



	@Override
	public boolean isExecutable(TimeWindowAO logicalOperator,
			TransformationConfiguration transformationConfiguration) {

		if (logicalOperator.getInputSchema().hasMetatype(ITimeInterval.class)){

			switch (logicalOperator.getWindowType()) {
			case TIME:
				if (logicalOperator.getWindowSlide() == null
						&& logicalOperator.getWindowAdvance() == null
						|| logicalOperator.getWindowSlide() == null) {
					return false;
				} else {
					return true;
				}
			default:
				return false;

			}
		}
		return false;

	}

}
