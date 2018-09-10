package de.uniol.inf.is.odysseus.pattern.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.pattern.logicaloperator.PatternMatchingAO;
import de.uniol.inf.is.odysseus.pattern.physicaloperator.AbsencePatternMatchingPO;
import de.uniol.inf.is.odysseus.pattern.physicaloperator.AllPatternMatchingPO;
import de.uniol.inf.is.odysseus.pattern.physicaloperator.AnyPatternMatchingPO;
import de.uniol.inf.is.odysseus.pattern.physicaloperator.ModalPatternMatchingPO;
import de.uniol.inf.is.odysseus.pattern.physicaloperator.NPatternMatchingPO;
import de.uniol.inf.is.odysseus.pattern.physicaloperator.AbstractPatternMatchingPO;
import de.uniol.inf.is.odysseus.pattern.physicaloperator.SubsetSelectionPatternMatchingPO;
import de.uniol.inf.is.odysseus.pattern.physicaloperator.TrendPatternMatchingPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.intervalapproach.TIInputStreamSyncArea;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * Transformation rule that creates the physical operators in respect of the pattern type.
 * @author Michael Falk
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class TPatternMatchingAORule extends
		AbstractTransformationRule<PatternMatchingAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(PatternMatchingAO operator,
			TransformationConfiguration config) throws RuleException {
		// physischen Operator auswählen
		AbstractPatternMatchingPO pOperator = null;
		switch (operator.getType()) {
		case ABSENCE:
			pOperator = new AbsencePatternMatchingPO(operator.getType(),
					operator.getTime(), operator.getBufferSize(),
					operator.getTimeUnit(), operator.getOutputMode(),
					operator.getEventTypes(), operator.getAssertions(),
					operator.getReturnExpressions(),
					operator.getInputTypeNames(), operator.getInputSchemas(),
					new TIInputStreamSyncArea());
			break;
		case ALL:
			pOperator = new AllPatternMatchingPO(operator.getType(),
					operator.getTime(), operator.getBufferSize(),
					operator.getTimeUnit(), operator.getOutputMode(),
					operator.getEventTypes(), operator.getAssertions(),
					operator.getReturnExpressions(),
					operator.getInputTypeNames(), operator.getInputSchemas(),
					new TIInputStreamSyncArea(), operator.getInputPort());
			break;
		case ALWAYS:
			pOperator = new ModalPatternMatchingPO(operator.getType(),
					operator.getTime(), operator.getBufferSize(),
					operator.getTimeUnit(), operator.getOutputMode(),
					operator.getEventTypes(), operator.getAssertions(),
					operator.getReturnExpressions(),
					operator.getInputTypeNames(), operator.getInputSchemas(),
					new TIInputStreamSyncArea(), operator.getInputPort());
			break;
		case ANY:
			pOperator = new AnyPatternMatchingPO(operator.getType(),
					operator.getTime(), operator.getBufferSize(),
					operator.getTimeUnit(), operator.getOutputMode(),
					operator.getEventTypes(), operator.getAssertions(),
					operator.getReturnExpressions(),
					operator.getInputTypeNames(), operator.getInputSchemas(),
					new TIInputStreamSyncArea(), operator.getInputPort());
			break;
		case FIRST_N:
			pOperator = new NPatternMatchingPO<>(operator.getType(), operator.getTime(),
					operator.getBufferSize(), operator.getTimeUnit(),
					operator.getOutputMode(), operator.getEventTypes(),
					operator.getAssertions(), operator.getReturnExpressions(),
					operator.getInputTypeNames(), operator.getInputSchemas(),
					new TIInputStreamSyncArea(), operator.getCountEvents(), operator.getInputPort());
			break;
		case FUNCTOR:
			pOperator = new AnyPatternMatchingPO(operator.getType(),
					operator.getTime(), operator.getBufferSize(),
					operator.getTimeUnit(), operator.getOutputMode(),
					operator.getEventTypes(), operator.getAssertions(),
					operator.getReturnExpressions(),
					operator.getInputTypeNames(), operator.getInputSchemas(),
					new TIInputStreamSyncArea(), operator.getInputPort());
			break;
		case LAST_N:
			pOperator = new NPatternMatchingPO<>(operator.getType(), operator.getTime(),
					operator.getBufferSize(), operator.getTimeUnit(),
					operator.getOutputMode(), operator.getEventTypes(),
					operator.getAssertions(), operator.getReturnExpressions(),
					operator.getInputTypeNames(), operator.getInputSchemas(),
					new TIInputStreamSyncArea(), operator.getCountEvents(), operator.getInputPort());
			break;
		case RELATIVE_N_HIGHEST:
			pOperator = new SubsetSelectionPatternMatchingPO(
					operator.getType(), operator.getTime(),
					operator.getBufferSize(), operator.getTimeUnit(),
					operator.getOutputMode(), operator.getEventTypes(),
					operator.getAssertions(), operator.getReturnExpressions(),
					operator.getInputTypeNames(), operator.getInputSchemas(),
					new TIInputStreamSyncArea(), operator.getCountEvents(), operator.getAttribute(), operator.getInputPort());
			break;
		case RELATIVE_N_LOWEST:
			pOperator = new SubsetSelectionPatternMatchingPO(
					operator.getType(), operator.getTime(),
					operator.getBufferSize(), operator.getTimeUnit(),
					operator.getOutputMode(), operator.getEventTypes(),
					operator.getAssertions(), operator.getReturnExpressions(),
					operator.getInputTypeNames(), operator.getInputSchemas(),
					new TIInputStreamSyncArea(), operator.getCountEvents(), operator.getAttribute(), operator.getInputPort());
			break;
		case SOMETIMES:
			pOperator = new ModalPatternMatchingPO(operator.getType(),
					operator.getTime(), operator.getBufferSize(),
					operator.getTimeUnit(), operator.getOutputMode(),
					operator.getEventTypes(), operator.getAssertions(),
					operator.getReturnExpressions(),
					operator.getInputTypeNames(), operator.getInputSchemas(),
					new TIInputStreamSyncArea(), operator.getInputPort());
			break;
		case DECREASING:
			pOperator = new TrendPatternMatchingPO(operator.getType(),
					operator.getTime(), operator.getBufferSize(),
					operator.getTimeUnit(), operator.getOutputMode(),
					operator.getEventTypes(), operator.getAssertions(),
					operator.getReturnExpressions(),
					operator.getInputTypeNames(), operator.getInputSchemas(),
					new TIInputStreamSyncArea(), operator.getAttribute(), operator.getInputPort());
			break;
		case INCREASING:
			pOperator = new TrendPatternMatchingPO(operator.getType(),
					operator.getTime(), operator.getBufferSize(),
					operator.getTimeUnit(), operator.getOutputMode(),
					operator.getEventTypes(), operator.getAssertions(),
					operator.getReturnExpressions(),
					operator.getInputTypeNames(), operator.getInputSchemas(),
					new TIInputStreamSyncArea(), operator.getAttribute(), operator.getInputPort());
			break;
		case NON_DECREASING:
			pOperator = new TrendPatternMatchingPO(operator.getType(),
					operator.getTime(), operator.getBufferSize(),
					operator.getTimeUnit(), operator.getOutputMode(),
					operator.getEventTypes(), operator.getAssertions(),
					operator.getReturnExpressions(),
					operator.getInputTypeNames(), operator.getInputSchemas(),
					new TIInputStreamSyncArea(), operator.getAttribute(), operator.getInputPort());
			break;
		case NON_INCREASING:
			pOperator = new TrendPatternMatchingPO(operator.getType(),
					operator.getTime(), operator.getBufferSize(),
					operator.getTimeUnit(), operator.getOutputMode(),
					operator.getEventTypes(), operator.getAssertions(),
					operator.getReturnExpressions(),
					operator.getInputTypeNames(), operator.getInputSchemas(),
					new TIInputStreamSyncArea(), operator.getAttribute(), operator.getInputPort());
			break;
		case STABLE:
			pOperator = new TrendPatternMatchingPO(operator.getType(),
					operator.getTime(), operator.getBufferSize(),
					operator.getTimeUnit(), operator.getOutputMode(),
					operator.getEventTypes(), operator.getAssertions(),
					operator.getReturnExpressions(),
					operator.getInputTypeNames(), operator.getInputSchemas(),
					new TIInputStreamSyncArea(), operator.getAttribute(), operator.getInputPort());
			break;
		case NON_STABLE:
			pOperator = new TrendPatternMatchingPO(operator.getType(),
					operator.getTime(), operator.getBufferSize(),
					operator.getTimeUnit(), operator.getOutputMode(),
					operator.getEventTypes(), operator.getAssertions(),
					operator.getReturnExpressions(),
					operator.getInputTypeNames(), operator.getInputSchemas(),
					new TIInputStreamSyncArea(), operator.getAttribute(), operator.getInputPort());
			break;
		case MIXED:
			pOperator = new TrendPatternMatchingPO(operator.getType(),
					operator.getTime(), operator.getBufferSize(),
					operator.getTimeUnit(), operator.getOutputMode(),
					operator.getEventTypes(), operator.getAssertions(),
					operator.getReturnExpressions(),
					operator.getInputTypeNames(), operator.getInputSchemas(),
					new TIInputStreamSyncArea(), operator.getAttribute(), operator.getInputPort());
			break;
		default:
			break;
		}
		defaultExecute(operator, pOperator, config, true, true);
	}

	@Override
	public boolean isExecutable(PatternMatchingAO operator,
			TransformationConfiguration transformConfig) {
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