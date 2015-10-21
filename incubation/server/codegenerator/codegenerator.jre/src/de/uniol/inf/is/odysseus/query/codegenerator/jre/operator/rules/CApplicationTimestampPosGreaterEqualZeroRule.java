package de.uniol.inf.is.odysseus.query.codegenerator.jre.operator.rules;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimestampAO;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetadataInitializer;
import de.uniol.inf.is.odysseus.query.codegenerator.jre.utils.StringTemplate;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.query.codegenerator.operator.rule.AbstractCApplicationTimestampPosGreaterEqualZeroRule;
import de.uniol.inf.is.odysseus.query.codegenerator.utils.DefaultCodegeneratorStatus;
import de.uniol.inf.is.odysseus.relational_interval.RelationalTimestampAttributeTimeIntervalMFactory;

public class CApplicationTimestampPosGreaterEqualZeroRule extends AbstractCApplicationTimestampPosGreaterEqualZeroRule<TimestampAO>{
	
	
	public CApplicationTimestampPosGreaterEqualZeroRule() {
		super(CApplicationTimestampPosGreaterEqualZeroRule.class.getName());
	}

	@Override
	public CodeFragmentInfo getCode(TimestampAO operator) {
	CodeFragmentInfo codeFragmentInfo = new CodeFragmentInfo();
		
		String operatorVariable = DefaultCodegeneratorStatus.getInstance().getVariable(operator.getSubscribedToSource().iterator().next().getTarget());
		
		TimestampAO timestampAO = (TimestampAO)operator;
		
		SDFSchema schema = timestampAO.getInputSchema();
		boolean clearEnd = timestampAO.isClearEnd();
		int pos = schema.indexOf(timestampAO.getStartTimestamp());

		int posEnd = timestampAO.hasEndTimestamp() ? timestampAO
				.getInputSchema()
						.indexOf(timestampAO.getEndTimestamp()) : -1;	
		
			StringTemplate relationalTimestampAttributeTimeIntervalMFactoryTemplate = new StringTemplate("utils","applicationTimestampPosGreaterEqualZero");
			
			relationalTimestampAttributeTimeIntervalMFactoryTemplate.getSt().add("operatorVariable", operatorVariable);			
			relationalTimestampAttributeTimeIntervalMFactoryTemplate.getSt().add("pos", pos);	
			relationalTimestampAttributeTimeIntervalMFactoryTemplate.getSt().add("posEnd", posEnd);	
			relationalTimestampAttributeTimeIntervalMFactoryTemplate.getSt().add("clearEnd", clearEnd);	
			relationalTimestampAttributeTimeIntervalMFactoryTemplate.getSt().add("timestampAO", timestampAO);	
			
			codeFragmentInfo.addFrameworkImport(RelationalTimestampAttributeTimeIntervalMFactory.class.getName());
			codeFragmentInfo.addFrameworkImport(IMetadataInitializer.class.getName());
			
			codeFragmentInfo.addCode(relationalTimestampAttributeTimeIntervalMFactoryTemplate.getSt().render());
			
		
		
	
		return codeFragmentInfo;
	}

}
