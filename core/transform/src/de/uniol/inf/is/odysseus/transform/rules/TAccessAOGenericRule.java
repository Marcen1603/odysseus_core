package de.uniol.inf.is.odysseus.transform.rules;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.datahandler.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.IToObjectInputStreamTransformer;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.IToStringArrayTransformer;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.ITransformer;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.InputHandlerRegistry;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.TransformerRegistry;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.pull.AccessPO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.pull.IInputHandler;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * This rule handles all generic access operator implementations
 * 
 * @author Marco Grawunder
 * 
 */
public class TAccessAOGenericRule extends AbstractTransformationRule<AccessAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void execute(AccessAO operator, TransformationConfiguration config) {
		String accessPOName = operator.getSourcename();

		if ("genericpull".equals(operator.getAdapter().toLowerCase())) {

			IInputHandler inputHandlerPrototype = InputHandlerRegistry
					.getInputHandler(operator.getInput());
			if (inputHandlerPrototype == null) {
				throw new TransformationException("No input handler "
						+ operator.getInput() + " found.");
			}
			IInputHandler input = inputHandlerPrototype.getInstance(operator
					.getOptionsMap());

			IDataHandler dataHandlerPrototype = DataHandlerRegistry
					.getDataHandler(operator.getDataHandler());
			if (dataHandlerPrototype == null) {
				throw new TransformationException("No data handler "
						+ operator.getDataHandler() + " found.");
			}
			IDataHandler dataHandler = dataHandlerPrototype
					.getInstance(operator.getOutputSchema());

			ITransformer transformerPrototype = TransformerRegistry
					.getDataHandler(operator.getTransformer());
			if (transformerPrototype == null) {
				throw new TransformationException("No transformer "
						+ operator.getTransformer() + " found.");
			}
			ITransformer transformer = transformerPrototype.getInstance(
					operator.getOptionsMap(), operator.getOutputSchema());

			ISource accessPO = null;
			if (transformer instanceof IToStringArrayTransformer) {
				accessPO = new AccessPO(input,
						(IToStringArrayTransformer) transformer, dataHandler);
			} else if (transformer instanceof IToObjectInputStreamTransformer) {
				accessPO = new AccessPO(input,
						(IToObjectInputStreamTransformer) transformer,
						dataHandler);
			}

			accessPO.setOutputSchema(operator.getOutputSchema());
			getDataDictionary().putAccessPlan(accessPOName, accessPO);
			Collection<ILogicalOperator> toUpdate = config
					.getTransformationHelper().replace(operator, accessPO);
			for (ILogicalOperator o : toUpdate) {
				update(o);
			}
			retract(operator);
			insert(accessPO);

		} else { // must be generic push, else isExecutable would not had
					// returned true
			throw new TransformationException("GenericPush currenlty not implemented!");
		}
	}

	@Override
	public boolean isExecutable(AccessAO operator,
			TransformationConfiguration config) {
		return (getDataDictionary().getAccessPlan(operator.getSourcename()) == null
				&& operator.getAdapter() != null && ("genericpull"
				.equals(operator.getAdapter().toLowerCase()) || "genericpush"
				.equals(operator.getAdapter().toLowerCase())));
	}

	@Override
	public String getName() {
		return "AccessAO (generic) --> AccessPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.ACCESS;
	}

	@Override
	public Class<?> getConditionClass() {
		return AccessAO.class;
	}

}
