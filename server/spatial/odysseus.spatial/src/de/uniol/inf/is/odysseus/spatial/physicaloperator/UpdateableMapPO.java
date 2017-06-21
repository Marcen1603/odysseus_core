package de.uniol.inf.is.odysseus.spatial.physicaloperator;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.UpdateExpressionsPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalMapPO;

public class UpdateableMapPO extends RelationalMapPO {

	protected UpdateableMapPO(SDFSchema inputSchema, boolean allowNullInOutput, boolean evaluateOnPunctuation,
			boolean suppressErrors, boolean keepInput, int[] restrictList) {
		super(inputSchema, allowNullInOutput, evaluateOnPunctuation, suppressErrors, keepInput, restrictList);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {

		if (punctuation instanceof UpdateExpressionsPunctuation) {
			// Set new expressions
			UpdateExpressionsPunctuation updateExpressionsPuctuation = (UpdateExpressionsPunctuation) punctuation;
			this.expressions = updateExpressionsPuctuation.getExpressions();
		} else {
			super.processPunctuation(punctuation, port);
		}

	}
}
