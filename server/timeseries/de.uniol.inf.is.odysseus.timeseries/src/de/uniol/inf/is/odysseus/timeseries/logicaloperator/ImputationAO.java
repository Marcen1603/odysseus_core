package de.uniol.inf.is.odysseus.timeseries.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;

/**
 * 
 * Same OutputSchema as InputSchema.
 * 
 * @author Christoph Schröer
 *
 */
@LogicalOperator(name = "IMPUTATION", minInputPorts = 1, maxInputPorts = 1, category = {
		LogicalOperatorCategory.MINING }, doc = "Impute missing data in a regular (time series) data stream.")
public class ImputationAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 7752589543824364696L;

	private TimeValueItem imputationWindowSize = null;
	
	private String imputationStrategy = null;

	public ImputationAO(final ImputationAO op) {
		super(op);
		this.imputationWindowSize = op.getImputationWindowSize();
		this.imputationStrategy = op.getImputationStrategy();
	}

	public ImputationAO() {
		super();
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new ImputationAO(this);
	}

	public TimeValueItem getImputationWindowSize() {
		return this.imputationWindowSize;
	}

	@Parameter(name = "imputation_window_size", type = TimeParameter.class, doc = "Window size of regular time series to detect missing data.")
	public void setImputationWindowSize(TimeValueItem imputationWindowSize) {
		this.imputationWindowSize = imputationWindowSize;
	}

	public String getImputationStrategy() {
		return this.imputationStrategy;
	}

	@Parameter(name = "imputation_strategy", type = StringParameter.class, doc = "Strategy to impute missing data.", optional = true)
	public void setImputationStrategy(String imputationStrategy) {
		this.imputationStrategy = imputationStrategy;
	}
}
