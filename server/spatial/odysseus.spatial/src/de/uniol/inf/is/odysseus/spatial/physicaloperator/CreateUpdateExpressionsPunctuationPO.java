package de.uniol.inf.is.odysseus.spatial.physicaloperator;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.UpdateExpressionsPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode;
import de.uniol.inf.is.odysseus.spatial.logicaloperator.CreateUpdateExpressionsPunctuationAO;

public class CreateUpdateExpressionsPunctuationPO<T extends Tuple<? extends ITimeInterval>> extends AbstractPipe<T, T> {

	private RelationalExpression[] expressions;

	public CreateUpdateExpressionsPunctuationPO(CreateUpdateExpressionsPunctuationAO ao) {
		SDFExpression[] expr = new SDFExpression[ao.getExpressions().size()];

		int counter = 0;
		for (SDFExpression expression : ao.getExpressions()) {
			expr[counter] = ao.getExpressions().get(counter);
			counter++;
		}

		this.expressions = new RelationalExpression[expr.length];
		for (int i = 0; i < expr.length; ++i) {
			this.expressions[i] = new RelationalExpression(expr[i]);
		}
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
	}

	@Override
	protected void process_next(T object, int port) {
		UpdateExpressionsPunctuation punctuation = new UpdateExpressionsPunctuation(object.getMetadata().getStart(), this.expressions);

	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}
}
