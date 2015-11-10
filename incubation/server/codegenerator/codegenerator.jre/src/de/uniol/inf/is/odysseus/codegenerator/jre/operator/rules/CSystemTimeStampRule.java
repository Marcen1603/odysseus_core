package de.uniol.inf.is.odysseus.codegenerator.jre.operator.rules;


import de.uniol.inf.is.odysseus.codegenerator.jre.utils.StringTemplate;
import de.uniol.inf.is.odysseus.codegenerator.modell.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.codegenerator.operator.rule.AbstractCSystemTimeStampRule;
import de.uniol.inf.is.odysseus.codegenerator.utils.DefaultCodegeneratorStatus;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimestampAO;
import de.uniol.inf.is.odysseus.server.intervalapproach.window.SystemTimeIntervalFactory;

/**
 * This rule generate from a TimeWindowAO the code for the 
 * SystemTimeTimestamp operator. 
 * 
 * template: operator/systemTimeTimestamp.st
 * 
 * @author MarcPreuschaft
 *
 */
public class CSystemTimeStampRule extends AbstractCSystemTimeStampRule<TimestampAO>{

	public CSystemTimeStampRule() {
		super(CSystemTimeStampRule.class.getName());
	}


	
	@Override
	public CodeFragmentInfo getCode(TimestampAO operator) {
		CodeFragmentInfo systemTimeCode = new CodeFragmentInfo();
		
		//OperatorVariable holen, auf den der Timestamp angewendet werden soll 
		String targetOperatorVariable = DefaultCodegeneratorStatus.getInstance().getVariable(operator.getSubscribedToSource().iterator().next().getTarget());
		
		
		TimestampAO timestampAO = (TimestampAO)operator;
		
		//String targetOperatorVariable = "test";
		
	
		StringTemplate systemTimeTimestampTemplate = new StringTemplate("operator","systemTimeTimestamp");
		systemTimeTimestampTemplate.getSt().add("timestampAO", timestampAO);
		systemTimeTimestampTemplate.getSt().add("targetOperatorVariable", targetOperatorVariable);
		
		
	
		
		systemTimeCode.addFrameworkImport(SystemTimeIntervalFactory.class.getName());
		systemTimeCode.addFrameworkImport(ITimeInterval.class.getName());
		systemTimeCode.addFrameworkImport(IStreamObject.class.getName());
		
		
		
		
		systemTimeCode.addCode(systemTimeTimestampTemplate.getSt().render());
		
		
		
		return systemTimeCode;
	}

}
