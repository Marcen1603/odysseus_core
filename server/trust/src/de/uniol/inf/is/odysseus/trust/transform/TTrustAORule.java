package de.uniol.inf.is.odysseus.trust.transform;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetadataInitializer;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetadataUpdater;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.MetadataUpdatePO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;
import de.uniol.inf.is.odysseus.trust.ITrust;
import de.uniol.inf.is.odysseus.trust.Trust;
import de.uniol.inf.is.odysseus.trust.logicaloperator.TrustAO;
import de.uniol.inf.is.odysseus.trust.physicaloperator.TrustUpdater;

public class TTrustAORule extends AbstractTransformationRule<TrustAO> {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void execute(TrustAO operator, TransformationConfiguration config) throws RuleException {
		RelationalExpression<ITrust> expression;
		IMetadataUpdater mUpdater;

		int attributeIndex = onlyOneAttributeInExpression(operator.getTrustExpression(), operator.getInputSchema());
		if (attributeIndex == -1) {
			expression = new RelationalExpression<>(operator.getTrustExpression());
			expression.initVars(operator.getInputSchema());
			mUpdater = new TrustUpdater<ITrust>(expression);
		} else {
			mUpdater = new TrustUpdater<ITrust>(attributeIndex);
		}

		if (operator.getPhysInputPOs().size() == 1
				&& operator.getPhysInputOperators().get(0) instanceof IMetadataInitializer) {
			IPhysicalOperator source = operator.getPhysInputOperators().get(0);
			((IMetadataInitializer) source).addMetadataUpdater(mUpdater);
			// Use the existing physical operator as replacement ...
			defaultExecute(operator, source, config, true, false, false);
		} else {
			MetadataUpdatePO<?, ?> po = new MetadataUpdatePO<ITrust, Tuple<? extends ITrust>>(mUpdater);
			defaultExecute(operator, po, config, true, true);
		}
	}

	@Override
	public boolean isExecutable(TrustAO operator, TransformationConfiguration config) {
		return operator.getInputSchema().getMetaschema().containsAll(Trust.schema);
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	private int onlyOneAttributeInExpression(SDFExpression expression, SDFSchema schema) {
		List<SDFAttribute> attributes = expression.getAllAttributes();

		if (expression.getMEPExpression().isVariable() && attributes.size() == 1) {

			SDFAttribute attr = schema.findAttribute(attributes.get(0).getAttributeName());
			if (attr != null) {
				return schema.indexOf(attr);
			}
		}

		return -1;
	}

}