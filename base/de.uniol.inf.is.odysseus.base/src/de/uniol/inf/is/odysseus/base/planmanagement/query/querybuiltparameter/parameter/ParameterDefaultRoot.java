package de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.parameter;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.AbstractQueryBuildParameter;

/**
 * {@link AbstractQueryBuildParameter} which provides a physical root for the
 * physical plan of a query.
 * 
 * @author Wolf Bauer
 * 
 */
public final class ParameterDefaultRoot extends
		AbstractQueryBuildParameter<IPhysicalOperator> {

	/**
	 * Creates a ParameterDefaultRoot.
	 * 
	 * @param value
	 *            physical root for the physical plan of a query.
	 */
	public ParameterDefaultRoot(IPhysicalOperator value) {
		super(value);
	}
}
