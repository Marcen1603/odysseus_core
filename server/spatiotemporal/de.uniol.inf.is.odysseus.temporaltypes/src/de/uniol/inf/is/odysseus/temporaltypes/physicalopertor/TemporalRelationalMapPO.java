package de.uniol.inf.is.odysseus.temporaltypes.physicalopertor;

import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalMapPO;
import de.uniol.inf.is.odysseus.temporaltypes.expressions.TemporalRelationalExpression;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IValidTime;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalDatatype;

public class TemporalRelationalMapPO<T extends IValidTime> extends RelationalMapPO<T> {

	public TemporalRelationalMapPO(SDFSchema inputSchema, SDFExpression[] expressions, boolean allowNullInOutput,
			boolean evaluateOnPunctuation, boolean expressionsUpdateable, boolean suppressErrors, boolean keepInput,
			int[] keepList) {
		super(inputSchema, expressions, allowNullInOutput, evaluateOnPunctuation, expressionsUpdateable, suppressErrors,
				keepInput, keepList);
		// TODO Auto-generated constructor stub
	}

	protected void init(SDFSchema schema, SDFExpression[] expr) {
		this.expressions = new RelationalExpression[expr.length];
		for (int i = 0; i < expr.length; ++i) {
			if (this.expressionHasTemporalAttribute(expr[i])) {
				this.expressions[i] = new TemporalRelationalExpression<>(expr[i]);
			} else {
				this.expressions[i] = new RelationalExpression<T>(expr[i]);
			}
			this.expressions[i].initVars(schema);
		}
	}

	private boolean expressionHasTemporalAttribute(SDFExpression expression) {
		for (SDFAttribute attribute : expression.getAllAttributes()) {
			if (attribute.getDatatype() instanceof TemporalDatatype) {
				return true;
			}
		}
		return false;
	}

}
