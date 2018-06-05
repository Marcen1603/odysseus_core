package de.uniol.inf.is.odysseus.timeseries.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;

/**
 * 
 * Same OutputSchema as InputSchema.
 * 
 * @author Christoph Schröer
 *
 */
@LogicalOperator(name = "REGULAR_TIME_SERIES", minInputPorts = 1, maxInputPorts = 1, category = {
		LogicalOperatorCategory.MINING }, doc = "Create a regular time series on irregular time series with previous value method.")
public class RegularTimeSeriesAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 7752589543824364696L;

	private TimeValueItem regularWindowSize = null;
	
	// TODO
//	private String regularStrategy;

	public RegularTimeSeriesAO(final RegularTimeSeriesAO regularTimeSeriesAO) {
		super(regularTimeSeriesAO);
		this.regularWindowSize = regularTimeSeriesAO.getRegularWindowSize();
	}

	public RegularTimeSeriesAO() {
		super();
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new RegularTimeSeriesAO(this);
	}

	public TimeValueItem getRegularWindowSize() {
		return this.regularWindowSize;
	}

	@Parameter(name = "regular_window_size", type = TimeParameter.class, doc = "Window size of regular time series.")
	public void setRegularWindowSize(final TimeValueItem regularWindowSize) {
		this.regularWindowSize = regularWindowSize;
	}
}
