package de.uniol.inf.is.odysseus.pattern.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.TIInputStreamSyncArea;
import de.uniol.inf.is.odysseus.pattern.logicaloperator.PatternMatchingAO;
import de.uniol.inf.is.odysseus.pattern.physicaloperator.PatternMatchingPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Michael Falk
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class TPatternMatchingAORule extends AbstractTransformationRule<PatternMatchingAO> {
	
	 @Override
	 public int getPriority() {
		 return 0;
	 }
	 
	  
	 @Override
	 public void execute(PatternMatchingAO operator, TransformationConfiguration config) {
		 if (!validateInputParameter(operator))
			 throw new IllegalArgumentException("Ungültige Kombination von Parametern!");
		 PatternMatchingPO pOperator = new PatternMatchingPO(operator.getType(), operator.getEventTypes(),
				 operator.getExpression(), operator.getInputTypeNames(), operator.getInputSchemas(), new TIInputStreamSyncArea(operator.getSubscribedToSource().size()));
		 defaultExecute(operator, pOperator, config, true, true);
	 }
	 
	 private boolean validateInputParameter(PatternMatchingAO operator) {
		// Elemente der relevanten Eventliste müssen auch als Quellen vorhanden sein
		boolean help = false;
		for (String eventType : operator.getEventTypes()) {
			for (String inputType : operator.getInputTypeNames().values()) {
				if (eventType.equals(inputType)) {
					help = true;
					break;
				}				
			}
			if (!help) break;
		}
		return help;
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