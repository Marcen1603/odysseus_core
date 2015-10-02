package de.uniol.inf.is.odysseus.query.codegenerator.jre.operator.rules;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimestampAO;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetadataInitializer;
import de.uniol.inf.is.odysseus.query.codegenerator.jre.utils.StringTemplate;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.query.codegenerator.operator.rule.AbstractCApplicationTimestamp;
import de.uniol.inf.is.odysseus.query.codegenerator.utils.DefaultCodegeneratorStatus;
import de.uniol.inf.is.odysseus.relational_interval.RelationalTimestampAttributeTimeIntervalMFactory;

public class CApplicationTimestampRule extends AbstractCApplicationTimestamp<TimestampAO>{
	
	
	public CApplicationTimestampRule() {
		super(CApplicationTimestampRule.class.getName());
	}

	@Override
	public CodeFragmentInfo getCode(TimestampAO operator) {
	CodeFragmentInfo codeFragmentInfo = new CodeFragmentInfo();
		
		String operatorVariable = DefaultCodegeneratorStatus.getInstance().getVariable(operator.getSubscribedToSource().iterator().next().getTarget());
		
		TimestampAO timestampAO = (TimestampAO)operator;
		
		SDFSchema schema = timestampAO.getInputSchema();
		boolean clearEnd = timestampAO.isClearEnd();
		int year = schema.indexOf(timestampAO.getStartTimestampYear());
		int month = schema
				.indexOf(timestampAO.getStartTimestampMonth());
		int day = schema.indexOf(timestampAO.getStartTimestampDay());
		int hour = schema.indexOf(timestampAO.getStartTimestampHour());
		int minute = schema.indexOf(timestampAO
				.getStartTimestampMinute());
		int second = schema.indexOf(timestampAO
				.getStartTimestampSecond());
		int millisecond = schema.indexOf(timestampAO
				.getStartTimestampMillisecond());
		int factor = timestampAO.getFactor();
		
		StringTemplate relationalTimestampAttributeTimeIntervalMFactoryTemplate = new StringTemplate("utils","applicationTimestamp");
		
		relationalTimestampAttributeTimeIntervalMFactoryTemplate.getSt().add("operatorVariable", operatorVariable);			
		relationalTimestampAttributeTimeIntervalMFactoryTemplate.getSt().add("year", year);	
		relationalTimestampAttributeTimeIntervalMFactoryTemplate.getSt().add("month", month);	
		relationalTimestampAttributeTimeIntervalMFactoryTemplate.getSt().add("day", day);	
		relationalTimestampAttributeTimeIntervalMFactoryTemplate.getSt().add("hour", hour);	
		relationalTimestampAttributeTimeIntervalMFactoryTemplate.getSt().add("minute", minute);	
		relationalTimestampAttributeTimeIntervalMFactoryTemplate.getSt().add("second", second);	
		relationalTimestampAttributeTimeIntervalMFactoryTemplate.getSt().add("millisecond", millisecond);	
		relationalTimestampAttributeTimeIntervalMFactoryTemplate.getSt().add("factor", factor);	
		relationalTimestampAttributeTimeIntervalMFactoryTemplate.getSt().add("hour", hour);	
		relationalTimestampAttributeTimeIntervalMFactoryTemplate.getSt().add("clearEnd", clearEnd);	
		relationalTimestampAttributeTimeIntervalMFactoryTemplate.getSt().add("timezone", timestampAO.getTimezone());	
	
		codeFragmentInfo.addImport(RelationalTimestampAttributeTimeIntervalMFactory.class.getName());
		codeFragmentInfo.addImport(IMetadataInitializer.class.getName());
	
		codeFragmentInfo.addCode(relationalTimestampAttributeTimeIntervalMFactoryTemplate.getSt().render());
		
		
	
	
		return codeFragmentInfo;
	}

}
