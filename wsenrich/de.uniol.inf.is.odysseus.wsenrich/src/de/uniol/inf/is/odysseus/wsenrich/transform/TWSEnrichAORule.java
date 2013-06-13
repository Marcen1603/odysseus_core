package de.uniol.inf.is.odysseus.wsenrich.transform;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalMergeFunction;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;
import de.uniol.inf.is.odysseus.wsenrich.logicaloperator.WSEnrichAO;
import de.uniol.inf.is.odysseus.wsenrich.physicaloperator.WSEnrichPO;


public class TWSEnrichAORule extends AbstractTransformationRule<WSEnrichAO> {

	@Override
	public int getPriority() {
		return 0; // No priorization
	}

	@Override
	public void execute(WSEnrichAO logical, TransformationConfiguration transformConfig) {
		
		IDataMergeFunction<Tuple<ITimeInterval>, ITimeInterval> dataMergeFunction = 
				new RelationalMergeFunction<ITimeInterval>(logical.getOutputSchema().size());
		
		if(logical.getServiceMethod().equals("REST")) {
			if(logical.getMethod().equals("GET")) {
				//IConnectionForWebservices con = ConnectionForWebservicesRegistry.getInstance("GET");
				//IRequestBuilder builder = RequstBuilderRegistry.getInstance("GET");
				
			}
			
		}
		
		WSEnrichPO<ITimeInterval> physical = new WSEnrichPO<ITimeInterval>(
			logical.getServiceMethod(),
			logical.getMethod(),
			logical.getUrl(),
			logical.getUrlSuffix(),
			logical.getArguments(),
			logical.getOpeation(),
			logical.getReceivedData(),
			logical.getCharset(),
			logical.getReturnType(),
			dataMergeFunction);
		
		physical.setOutputSchema(logical.getOutputSchema());
		replace(logical, physical, transformConfig);
		retract(logical);
	}

	@Override
	public boolean isExecutable(WSEnrichAO logical,
			TransformationConfiguration config) {
		return logical.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "WSEnrichAO --> WSEnrichPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
