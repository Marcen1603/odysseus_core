package de.uniol.inf.is.odysseus.codegenerator.jre.operator.rules;

import de.uniol.inf.is.odysseus.codegenerator.jre.utils.StringTemplate;
import de.uniol.inf.is.odysseus.codegenerator.modell.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.codegenerator.operator.rule.AbstractCApplicationTimestampPosGreaterEqualZeroRule;
import de.uniol.inf.is.odysseus.codegenerator.utils.DefaultCodegeneratorStatus;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimestampAO;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetadataInitializer;
import de.uniol.inf.is.odysseus.relational_interval.RelationalTimestampAttributeTimeIntervalMFactory;

/**
 * This rule generate from a TimestampAO the code for the 
 * applicationTimestampPosGreaterEqualZero. 
 * 
 * template: utils/applicationTimestampPosGreaterEqualZero.st
 * 
 * @author MarcPreuschaft
 *
 */
public class CApplicationTimestampPosGreaterEqualZeroRule extends AbstractCApplicationTimestampPosGreaterEqualZeroRule<TimestampAO>{
	
	
	public CApplicationTimestampPosGreaterEqualZeroRule() {
		super(CApplicationTimestampPosGreaterEqualZeroRule.class.getName());
	}

	@Override
	public CodeFragmentInfo getCode(TimestampAO operator) {
		CodeFragmentInfo codeFragmentInfo = new CodeFragmentInfo();
		
		//get unique operator variable
		String operatorVariable = DefaultCodegeneratorStatus.getInstance().getVariable(operator.getSubscribedToSource().iterator().next().getTarget());
		
		TimestampAO timestampAO = (TimestampAO)operator;
		
		//get inputSchema
		SDFSchema schema = timestampAO.getInputSchema();
		//calc clearEnd, pos and posEnd
		boolean clearEnd = timestampAO.isClearEnd();
		int pos = schema.indexOf(timestampAO.getStartTimestamp());

		int posEnd = timestampAO.hasEndTimestamp() ? timestampAO
				.getInputSchema()
						.indexOf(timestampAO.getEndTimestamp()) : -1;	
		
		//generate code for applicationTimestampPosGreaterEqualZero
		StringTemplate relationalTimestampAttributeTimeIntervalMFactoryTemplate = new StringTemplate("utils","applicationTimestampPosGreaterEqualZero");
		relationalTimestampAttributeTimeIntervalMFactoryTemplate.getSt().add("operatorVariable", operatorVariable);			
		relationalTimestampAttributeTimeIntervalMFactoryTemplate.getSt().add("pos", pos);	
		relationalTimestampAttributeTimeIntervalMFactoryTemplate.getSt().add("posEnd", posEnd);	
		relationalTimestampAttributeTimeIntervalMFactoryTemplate.getSt().add("clearEnd", clearEnd);	
		relationalTimestampAttributeTimeIntervalMFactoryTemplate.getSt().add("timestampAO", timestampAO);	
			
		//render template
		codeFragmentInfo.addCode(relationalTimestampAttributeTimeIntervalMFactoryTemplate.getSt().render());
		
		//add framework imports
		codeFragmentInfo.addFrameworkImport(RelationalTimestampAttributeTimeIntervalMFactory.class.getName());
		codeFragmentInfo.addFrameworkImport(IMetadataInitializer.class.getName());
			
	
		return codeFragmentInfo;
	}

}
