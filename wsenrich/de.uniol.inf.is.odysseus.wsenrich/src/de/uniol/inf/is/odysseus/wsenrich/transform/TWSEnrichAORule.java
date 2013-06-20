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
import de.uniol.inf.is.odysseus.wsenrich.util.ConnectionForWebservicesRegistry;
import de.uniol.inf.is.odysseus.wsenrich.util.HttpEntityToStringConverter;
import de.uniol.inf.is.odysseus.wsenrich.util.IConnectionForWebservices;
import de.uniol.inf.is.odysseus.wsenrich.util.IKeyFinder;
import de.uniol.inf.is.odysseus.wsenrich.util.IRequestBuilder;
import de.uniol.inf.is.odysseus.wsenrich.util.KeyFinderRegistry;
import de.uniol.inf.is.odysseus.wsenrich.util.RequestBuilderRegistry;


public class TWSEnrichAORule extends AbstractTransformationRule<WSEnrichAO> {

	@Override
	public int getPriority() {
		return 0; // No priorization
	}

	@Override
	public void execute(WSEnrichAO logical, TransformationConfiguration transformConfig) {
		
		IDataMergeFunction<Tuple<ITimeInterval>, ITimeInterval> dataMergeFunction = 
				new RelationalMergeFunction<ITimeInterval>(logical.getOutputSchema().size());
			
				IConnectionForWebservices connection = ConnectionForWebservicesRegistry.getInstance(logical.getGetOrPost());
				IRequestBuilder requestBuilder = RequestBuilderRegistry.getInstance(logical.getMethod());
				HttpEntityToStringConverter converter = new HttpEntityToStringConverter(logical.getCharset());
				IKeyFinder keyFinder = KeyFinderRegistry.getInstance(logical.getReturnType());
			
		
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
			dataMergeFunction,
			connection,
			requestBuilder,
			converter,
			keyFinder);
		
		defaultExecute(logical, physical, transformConfig, true, true);
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
