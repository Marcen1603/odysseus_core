package de.uniol.inf.is.odysseus.pattern.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.TIInputStreamSyncArea;
import de.uniol.inf.is.odysseus.pattern.logicaloperator.PatternMatchingAO;
import de.uniol.inf.is.odysseus.pattern.physicaloperator.AbsencePatternMatchingPO;
import de.uniol.inf.is.odysseus.pattern.physicaloperator.AllPatternMatchingPO;
import de.uniol.inf.is.odysseus.pattern.physicaloperator.AnyPatternMatchingPO;
import de.uniol.inf.is.odysseus.pattern.physicaloperator.FunctorPatternMatchingPO;
import de.uniol.inf.is.odysseus.pattern.physicaloperator.ModalPatternMatchingPO;
import de.uniol.inf.is.odysseus.pattern.physicaloperator.PatternMatchingPO;
import de.uniol.inf.is.odysseus.pattern.physicaloperator.SubsetSelectionPatternMatchingPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Michael Falk
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class TPatternMatchingAORule extends AbstractTransformationRule<PatternMatchingAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(PatternMatchingAO operator,	TransformationConfiguration config) {
		// physischen Operator auswählen
		PatternMatchingPO pOperator = null;
		switch (operator.getType()) {
		case ABSENCE:
			pOperator = new AbsencePatternMatchingPO(operator.getType(),
					operator.getTime(), operator.getBufferSize(),
					operator.getTimeUnit(), operator.getOutputMode(),
					operator.getEventTypes(), operator.getAssertions(),
					operator.getReturnExpressions(), operator.getInputTypeNames(),
					operator.getInputSchemas(), new TIInputStreamSyncArea(operator
							.getSubscribedToSource().size()));
			break;
		case ALL:
			pOperator = new AllPatternMatchingPO(operator.getType(),
					operator.getTime(), operator.getBufferSize(),
					operator.getTimeUnit(), operator.getOutputMode(),
					operator.getEventTypes(), operator.getAssertions(),
					operator.getReturnExpressions(), operator.getInputTypeNames(),
					operator.getInputSchemas(), new TIInputStreamSyncArea(operator
							.getSubscribedToSource().size()));
			break;
		case ALWAYS:
			pOperator = new ModalPatternMatchingPO(operator.getType(),
					operator.getTime(), operator.getBufferSize(),
					operator.getTimeUnit(), operator.getOutputMode(),
					operator.getEventTypes(), operator.getAssertions(),
					operator.getReturnExpressions(), operator.getInputTypeNames(),
					operator.getInputSchemas(), new TIInputStreamSyncArea(operator
							.getSubscribedToSource().size()));
			break;
		case ANY:
			pOperator = new AnyPatternMatchingPO(operator.getType(),
					operator.getTime(), operator.getBufferSize(),
					operator.getTimeUnit(), operator.getOutputMode(),
					operator.getEventTypes(), operator.getAssertions(),
					operator.getReturnExpressions(), operator.getInputTypeNames(),
					operator.getInputSchemas(), new TIInputStreamSyncArea(operator
							.getSubscribedToSource().size()));
			break;
		case COUNT:
			break;
		case FIRST_N_PATTERN:
			pOperator = new SubsetSelectionPatternMatchingPO(operator.getType(),
					operator.getTime(), operator.getBufferSize(),
					operator.getTimeUnit(), operator.getOutputMode(),
					operator.getEventTypes(), operator.getAssertions(),
					operator.getReturnExpressions(), operator.getInputTypeNames(),
					operator.getInputSchemas(), new TIInputStreamSyncArea(operator
							.getSubscribedToSource().size()));
			break;
		case FUNCTOR:
			pOperator = new FunctorPatternMatchingPO(operator.getType(),
					operator.getTime(), operator.getBufferSize(),
					operator.getTimeUnit(), operator.getOutputMode(),
					operator.getEventTypes(), operator.getAssertions(),
					operator.getReturnExpressions(), operator.getInputTypeNames(),
					operator.getInputSchemas(), new TIInputStreamSyncArea(operator
							.getSubscribedToSource().size()),
					operator.getAttribute(), operator.getValue());
			break;
		case LAST_N_PATTERN:
			pOperator = new SubsetSelectionPatternMatchingPO(operator.getType(),
					operator.getTime(), operator.getBufferSize(),
					operator.getTimeUnit(), operator.getOutputMode(),
					operator.getEventTypes(), operator.getAssertions(),
					operator.getReturnExpressions(), operator.getInputTypeNames(),
					operator.getInputSchemas(), new TIInputStreamSyncArea(operator
							.getSubscribedToSource().size()));
			break;
		case RELATIVE_N_HIGHEST:
			break;
		case RELATIVE_N_LOWEST:
			break;
		case SOMETIMES:
			pOperator = new ModalPatternMatchingPO(operator.getType(),
					operator.getTime(), operator.getBufferSize(),
					operator.getTimeUnit(), operator.getOutputMode(),
					operator.getEventTypes(), operator.getAssertions(),
					operator.getReturnExpressions(), operator.getInputTypeNames(),
					operator.getInputSchemas(), new TIInputStreamSyncArea(operator
							.getSubscribedToSource().size()));
			break;
		case VALUE_MAX:
			break;
		case VALUE_MIN:
			break;
		default:
			break;

		}
		defaultExecute(operator, pOperator, config, true, true);
	}

	@Override
	public boolean isExecutable(PatternMatchingAO operator, TransformationConfiguration transformConfig) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "PatternMatchingAO -> PatternMatchingPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super PatternMatchingAO> getConditionClass() {
		return PatternMatchingAO.class;
	}

}