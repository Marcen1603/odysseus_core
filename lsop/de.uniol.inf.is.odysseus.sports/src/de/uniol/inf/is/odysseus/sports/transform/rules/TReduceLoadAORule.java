package de.uniol.inf.is.odysseus.sports.transform.rules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalGroupProcessor;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.sports.logicaloperator.ReduceLoadAO;
import de.uniol.inf.is.odysseus.sports.physicaloperator.ReduceLoadPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * Transform rule to transform ReduceLoadAO to ReduceLoadPO.
 * @author Carsten Cordes
 *
 */
public class TReduceLoadAORule extends AbstractTransformationRule<ReduceLoadAO> {

	/**
	 * Logger
	 */
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(TReduceLoadAORule.class);
	
	/**
	 * Priority
	 */
	@Override
	public int getPriority() {
		return 0;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	/**
	 * Executes the rule
	 */
	public void execute(ReduceLoadAO ao, TransformationConfiguration config) throws RuleException {
		ReduceLoadPO po = new ReduceLoadPO<>(ao);
		if(ao.getGroupingAttributes().size()>0) {
			RelationalGroupProcessor r = new RelationalGroupProcessor(
					ao.getInputSchema(), ao.getOutputSchema(),
					ao.getGroupingAttributes(),
					null, false);
			po.setGroupProcessor(r);
		}
		
		defaultExecute(ao, po, config, true, true);
	}

	@Override
	/**
	 * True if all parameters are set.
	 */
	public boolean isExecutable(ReduceLoadAO operator,
			TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	/**
	 * Name of Rule.
	 */
	public String getName() {
		return "ReduceLoadAO --> ReduceLoadPO";
	}

	@Override
	/***
	 * Type of rule.
	 */
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	/**
	 * Class of Rule.
	 */
	public Class<? super ReduceLoadAO> getConditionClass() {
		return ReduceLoadAO.class;
	}
}
