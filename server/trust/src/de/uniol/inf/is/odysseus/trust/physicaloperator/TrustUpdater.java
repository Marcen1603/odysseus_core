package de.uniol.inf.is.odysseus.trust.physicaloperator;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.server.metadata.AbstractMetadataUpdater;
import de.uniol.inf.is.odysseus.trust.ITrust;

public class TrustUpdater<M extends ITrust> extends AbstractMetadataUpdater<M, Tuple<M>> {

	// > 0 if an attribute is used to set trust
	private int trustAttributeIndex = -1;

	// != null if an expression is used to set trust
	private RelationalExpression<M> trustExpression;

	public TrustUpdater(int attributeIndex) {
		trustAttributeIndex = attributeIndex;
	}

	public TrustUpdater(RelationalExpression<M> expression) {
		trustExpression = expression;
	}

	@Override
	public void updateMetadata(Tuple<M> inElem) {
		double trustValue;
		if (trustAttributeIndex < 0) {
			trustValue = ((Number) trustExpression.evaluate(inElem, null, null)).doubleValue();
		} else {
			trustValue = inElem.getAttribute(trustAttributeIndex);
		}
		inElem.getMetadata().setTrust(trustValue);
	}

}