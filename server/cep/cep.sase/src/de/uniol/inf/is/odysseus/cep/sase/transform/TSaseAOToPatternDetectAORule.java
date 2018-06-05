package de.uniol.inf.is.odysseus.cep.sase.transform;

import java.util.List;

import de.uniol.inf.is.odysseus.cep.PatternDetectAO;
import de.uniol.inf.is.odysseus.cep.sase.SaseBuilder;
import de.uniol.inf.is.odysseus.cep.sase.logicaloperator.SaseAO;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.CreateQueryCommand;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TSaseAOToPatternDetectAORule extends AbstractTransformationRule<SaseAO> {


	@Override
	public void execute(SaseAO operator, TransformationConfiguration config) throws RuleException {
		SaseBuilder parser = new SaseBuilder();
		PatternDetectAO<?> newOp = null;
		List<IExecutorCommand> op = parser.parse(operator.getQuery(), getCaller(),
				getDataDictionary(), false, false);
		// I know there is only one operator
		IExecutorCommand cmd = op.get(0);
		if (cmd instanceof CreateQueryCommand) {
			newOp = (PatternDetectAO<?>) ((CreateQueryCommand)cmd).getQuery().getLogicalPlan().getRoot();
			newOp.setOneMatchPerInstance(operator.isOneMatchPerInstance());
			newOp.setHeartbeatRate(operator.getHeartbeatrate());
			//Init Schema (and other things)
			replace(operator, newOp, config);
			SDFSchema outputSchema = newOp.getOutputSchema();
			List<SDFAttribute> attributes = operator.getOutputSchema().getAttributes();
			
			SDFSchema newOutputSchema = SDFSchemaFactory.createNewWithAttributes(attributes, outputSchema);
			newOp.setOutputSchema(newOutputSchema);
			retract(operator);
			insert(newOp);
		}
	}

	@Override
	public boolean isExecutable(SaseAO operator,
			TransformationConfiguration config) {
		return true;
	}

	@Override
	public String getName() {
		return "SASEAO --> PATTERNDETECTAO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.SUBSTITUTION;
	}

	
}
