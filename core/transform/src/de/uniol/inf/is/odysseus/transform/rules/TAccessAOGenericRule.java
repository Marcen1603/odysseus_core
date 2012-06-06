package de.uniol.inf.is.odysseus.transform.rules;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.connection.AccessConnectionHandlerRegistry;
import de.uniol.inf.is.odysseus.core.connection.IAccessConnectionHandler;
import de.uniol.inf.is.odysseus.core.datahandler.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IInputDataHandler;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.objecthandler.IObjectHandler;
import de.uniol.inf.is.odysseus.core.objecthandler.InputDataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.objecthandler.ObjectHandlerRegistry;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.IToObjectInputStreamTransformer;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.IToStringArrayTransformer;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.ITransformer;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.InputHandlerRegistry;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.TransformerRegistry;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.pull.AccessPO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.pull.IInputHandler;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.push.ReceiverPO;
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

			IDataHandler dataHandler = DataHandlerRegistry
					.getDataHandler(operator.getDataHandler(), operator.getOutputSchema());
			if (dataHandler == null) {
				throw new TransformationException("No data handler "
						+ operator.getDataHandler() + " found.");
			}

			ITransformer transformerPrototype = TransformerRegistry
					.getTransformer(operator.getTransformer());
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

			ReceiverPO accessPO = null;
			final IDataHandler dataHandler;
			IDataHandler concreteHandler = null;
			if (operator.getInputSchema() != null
					&& operator.getInputSchema().size() > 0) {
				concreteHandler =  DataHandlerRegistry
						.getDataHandler(operator.getDataHandler(),operator.getInputSchema());
			} else {
				concreteHandler = DataHandlerRegistry
						.getDataHandler(operator.getDataHandler(), operator.getOutputSchema());
			}
			
			IAccessConnectionHandler connectionPrototype = AccessConnectionHandlerRegistry.get(operator.getAccessConnectionHandler());
			if (connectionPrototype == null){
				throw new TransformationException("No access connection handler "+operator.getAccessConnectionHandler()+" found.");
			}
			IAccessConnectionHandler connection = connectionPrototype.getInstance(operator.getOptionsMap());

			IObjectHandler objectHandlerPrototype = ObjectHandlerRegistry.get(operator.getObjectHandler());
			if (objectHandlerPrototype == null){
				throw new TransformationException("No object handler "+operator.getObjectHandler()+" found!");
			}
			
			IObjectHandler objectHandler = objectHandlerPrototype.getInstance(concreteHandler);
			
			IInputDataHandler inputDataHandlerPrototype = InputDataHandlerRegistry.get(operator.getInputDataHandler());
			if (inputDataHandlerPrototype == null){
				throw new TransformationException("No input data handler "+operator.getInputDataHandler()+" found");
			}
			
			IInputDataHandler inputDataHandler = inputDataHandlerPrototype.getInstance(operator.getOptionsMap());

			accessPO = new ReceiverPO(objectHandler, inputDataHandler,
					connection);
			accessPO.setOutputSchema(operator.getOutputSchema());
			getDataDictionary().putAccessPlan(accessPOName, accessPO);
			Collection<ILogicalOperator> toUpdate = config
					.getTransformationHelper().replace(operator, accessPO);
			for (ILogicalOperator o : toUpdate) {
				update(o);
			}
			retract(operator);
			insert(accessPO);
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
