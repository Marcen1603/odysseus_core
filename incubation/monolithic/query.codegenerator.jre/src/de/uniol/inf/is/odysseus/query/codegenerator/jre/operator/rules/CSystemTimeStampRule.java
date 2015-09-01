package de.uniol.inf.is.odysseus.query.codegenerator.jre.operator.rules;


import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimestampAO;
import de.uniol.inf.is.odysseus.query.codegenerator.jre.utils.JreCodegeneratorStatus;
import de.uniol.inf.is.odysseus.query.codegenerator.jre.utils.StringTemplate;
import de.uniol.inf.is.odysseus.query.codegenerator.operator.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.query.codegenerator.operator.rule.AbstractCSystemTimeStampRule;
import de.uniol.inf.is.odysseus.server.intervalapproach.window.SystemTimeIntervalFactory;


public class CSystemTimeStampRule extends AbstractCSystemTimeStampRule<TimestampAO>{

	public CSystemTimeStampRule() {
		super(CSystemTimeStampRule.class.getName());
	}


	
	@Override
	public CodeFragmentInfo getCode(TimestampAO operator) {
		CodeFragmentInfo systemTimeCode = new CodeFragmentInfo();
		
		//OperatorVariable holen, auf den der Timestamp angewendet werden soll 
		String targetOperatorVariable = JreCodegeneratorStatus.getInstance().getVariable(operator.getSubscribedToSource().iterator().next().getTarget());
		
		
		TimestampAO timestampAO = (TimestampAO)operator;
		
		//String targetOperatorVariable = "test";
		
	
		StringTemplate systemTimeTimestampTemplate = new StringTemplate("operator","systemTimeTimestamp");
		systemTimeTimestampTemplate.getSt().add("timestampAO", timestampAO);
		systemTimeTimestampTemplate.getSt().add("targetOperatorVariable", targetOperatorVariable);
		
		
	
		
		systemTimeCode.addImport(SystemTimeIntervalFactory.class.getName());
		systemTimeCode.addImport(ITimeInterval.class.getName());
		systemTimeCode.addImport(IStreamObject.class.getName());
		
		
		
		
		systemTimeCode.addCode(systemTimeTimestampTemplate.getSt().render());
		
		
		
		return systemTimeCode;
	}

}
