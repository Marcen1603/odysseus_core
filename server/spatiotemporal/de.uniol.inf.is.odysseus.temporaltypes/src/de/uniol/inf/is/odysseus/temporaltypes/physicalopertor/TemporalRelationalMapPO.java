package de.uniol.inf.is.odysseus.temporaltypes.physicalopertor;

import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalMapPO;
import de.uniol.inf.is.odysseus.temporaltypes.expressions.TemporalRelationalExpression;
import de.uniol.inf.is.odysseus.temporaltypes.function.TemporalFunction;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IValidTimes;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalDatatype;

/**
 * RelationalMapPO that can work with temporal expressions. This is done by
 * initializing expressions with temporal attributes as temporal expressions.
 * 
 * @author Tobias Brandt
 *
 * @param <T>
 */
public class TemporalRelationalMapPO<T extends IValidTimes> extends RelationalMapPO<T> {

	public TemporalRelationalMapPO(SDFSchema inputSchema, SDFExpression[] expressions, boolean allowNullInOutput,
			boolean evaluateOnPunctuation, boolean expressionsUpdateable, boolean suppressErrors, boolean keepInput,
			int[] keepList) {
		super(inputSchema, expressions, allowNullInOutput, evaluateOnPunctuation, expressionsUpdateable, suppressErrors,
				keepInput, keepList);
	}

	@SuppressWarnings("unchecked")
	protected void init(SDFSchema schema, SDFExpression[] expr) {
		// It is not possible to create generic arrays, therefore the suppress warning.
		this.expressions = new RelationalExpression[expr.length];
		for (int i = 0; i < expr.length; ++i) {
			/*
			 * In case of a copy expression do not use a temporal expression, cause this
			 * could lead to an unwanted conversion from a temporal function to a generic
			 * temporal function.
			 */
			if (TemporalDatatype.expressionHasTemporalAttribute(expr[i], schema) && !isCopyExpression(expr[i])
					&& !isTemporalFunction(expr[i])) {
				this.expressions[i] = new TemporalRelationalExpression<>(expr[i]);
			} else {
				this.expressions[i] = new RelationalExpression<T>(expr[i]);
			}
			this.expressions[i].initVars(schema);
		}
	}

	/**
	 * Tests if the given expression is a temporal function. If so, a normal
	 * expression should be used, not a temporal one because the temporal function
	 * can work with temporal types.
	 */
	private boolean isTemporalFunction(SDFExpression expression) {
		return expression.getMEPExpression() instanceof TemporalFunction;
	}

	/**
	 * Checks if the expression simply copies / renames an attribute
	 * 
	 * @param expression
	 *            The expression to test
	 * @return True, if the expression simply copies an attribute, false otherwise
	 */
	private boolean isCopyExpression(SDFExpression expression) {

		SDFAttribute firstAttribute = expression.getAllAttributes().size() == 1 ? expression.getAllAttributes().get(0)
				: null;

		// If its more than one attribute, it cannot be a simple copy
		if (firstAttribute == null) {
			return false;
		}

		/*
		 * Compare names of the attribute and the expression. If the names are equal
		 * (with or without the source in front of the name), it is a simple copy.
		 */
		return firstAttribute.getAttributeName().equals(expression.getExpressionString())
				|| firstAttribute.getURI().equals(expression.getExpressionString());
	}

}
