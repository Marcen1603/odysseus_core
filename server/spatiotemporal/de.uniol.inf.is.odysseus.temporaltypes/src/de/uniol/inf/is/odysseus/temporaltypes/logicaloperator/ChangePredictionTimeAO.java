package de.uniol.inf.is.odysseus.temporaltypes.logicaloperator;

import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpressionParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;

/**
 * This operator is used to manipulate the PredictionTime metadata field.
 * 
 * @author Tobias Brandt
 *
 */
@LogicalOperator(name = "PredictionTime", doc = "Change the metadata PredictionTime", minInputPorts = 1, maxInputPorts = 1, category = {
		LogicalOperatorCategory.ADVANCED }, hidden = false)
public class ChangePredictionTimeAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 6126935973966254984L;

	/*
	 * TODO Allow expressions to manipulate the timestamps to have a little more
	 * flexibility. Maybe similar to the TimestampAO.
	 */

	private TimeValueItem valueToAddStart;
	private TimeValueItem valueToAddEnd;
	private boolean alignAtEnd = false;
	private boolean copyTimeInterval;
	private TimeUnit predictionBaseTimeUnit;

	private SDFExpression startExpression;
	private SDFExpression endExpression;

	public ChangePredictionTimeAO() {

	}

	public ChangePredictionTimeAO(ChangePredictionTimeAO other) {
		super(other);
		this.valueToAddStart = other.getValueToAddStart();
		this.valueToAddEnd = other.getValueToAddEnd();
		this.copyTimeInterval = other.isCopyTimeInterval();
		this.alignAtEnd = other.isAlignAtEnd();
		this.predictionBaseTimeUnit = other.getPredictionBaseTimeUnit();

		this.startExpression = other.startExpression != null ? other.startExpression.clone() : null;
		this.endExpression = other.endExpression != null ? other.endExpression.clone() : null;
	}

	@Parameter(name = "copyTimeInterval", doc = "Set to true if the PredictionTimes shall equal the TimeInterval.", type = BooleanParameter.class, optional = true)
	public void setCopyTimeInterval(boolean copyTimeInterval) {
		this.copyTimeInterval = copyTimeInterval;
	}

	public boolean isCopyTimeInterval() {
		return this.copyTimeInterval;
	}

	@Parameter(name = "addToStartValue", doc = "The value that is added to the start timestamp of the stream time to create the start timestamp of the PredictionTime.", type = TimeParameter.class, optional = true)
	public void setAddStartValue(TimeValueItem valueToAddStart) {
		this.valueToAddStart = valueToAddStart;
	}

	@Parameter(name = "addToEndValue", doc = "The value that is addd to the start timestamp of the stream time to create the end timestamp of the PredictionTime.", type = TimeParameter.class, optional = true)
	public void setAddEndValue(TimeValueItem valueToAddEnd) {
		this.valueToAddEnd = valueToAddEnd;
	}

	@Parameter(name = "predictionBaseTimeUnit", doc = "The basetime unit for the prediction time.", type = StringParameter.class, optional = true)
	public void setTimeUnit(String predictionBaseTimeUnitName) {
		this.predictionBaseTimeUnit = TimeUnit.valueOf(predictionBaseTimeUnitName);
	}

	public TimeUnit getPredictionBaseTimeUnit() {
		/*
		 * In case that the prediction base time unit is not set, it's simply the stream
		 * time unit. To avoid null-pointers, the stream time unit is used here instead
		 * of null.
		 */
		this.determineBaseTimeUnit();
		return predictionBaseTimeUnit != null ? predictionBaseTimeUnit : this.getBaseTimeUnit();
	}

	/**
	 * 
	 * @return The value that is added to the start timestamp of the stream time to
	 *         create the start timestamp of the PredictionTime.
	 */
	public TimeValueItem getValueToAddStart() {
		return this.valueToAddStart;
	}

	/**
	 * 
	 * @return The value that is added to the start timestamp of the stream time to
	 *         create end end timestamp of the PredictionTime.
	 */
	public TimeValueItem getValueToAddEnd() {
		return this.valueToAddEnd;
	}

	public boolean isAlignAtEnd() {
		return alignAtEnd;
	}

	@Parameter(name = "alignAtEnd", doc = "The values set as the PredictionTime are aligned at the end timestamp of the streamtime. Default: false.", type = BooleanParameter.class, optional = true)
	public void setAlignAtEnd(boolean alignAtEnd) {
		this.alignAtEnd = alignAtEnd;
	}

	@Parameter(type = NamedExpressionParameter.class, name = "START", aliasname = "StartExpression", optional = true, doc = "Expression to calculate the start time. Could be an attribute, too.")
	public void setStartExpression2(NamedExpression expression) {
		this.startExpression = expression.expression;
	}

	public SDFExpression getStartExpression() {
		return startExpression;
	}

	@Parameter(type = NamedExpressionParameter.class, name = "END", aliasname = "EndExpression", optional = true, doc = "Expression to calculate the end time. Could be an attribute, too.")
	public void setEndExpression2(NamedExpression expression) {
		this.endExpression = expression.expression;
	}

	public SDFExpression getEndExpression() {
		return endExpression;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new ChangePredictionTimeAO(this);
	}

}
