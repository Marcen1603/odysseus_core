package de.uniol.inf.is.odysseus.transform.rules;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.infoservice.InfoService;
import de.uniol.inf.is.odysseus.core.infoservice.InfoServiceFactory;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MetadataAO;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataRegistry;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.MetadataPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TMetadataAOTransformation extends
		AbstractTransformationRule<MetadataAO> {

	private static final InfoService INFO = InfoServiceFactory
			.getInfoService(TMetadataAOTransformation.class);

	@Override
	public void execute(MetadataAO operator, TransformationConfiguration config)
			throws RuleException {
		List<SDFMetaSchema> inputMetaschema = operator.getInputSchema(0)
				.getMetaschema();
		Class<? extends IMetaAttribute>[] c = operator.getLocalMetaAttribute()
				.getClasses();
		List<SDFMetaSchema> outputMetaschema = MetadataRegistry.getMetadataSchema(MetadataRegistry
				.toClassNames(c));

		// Determine mapping from inputMetaschema to outputMetaschema position
		Map<Integer,Integer> mappings = new HashMap<>();
		
		// TODO: is this correct?
		if (inputMetaschema == null || inputMetaschema.size() == 0) {
			INFO.warning("Input operator of Metadata Operator "
					+ operator.getName()
					+ " provides no metadata. Creating new ones.");
		} else {
			for (int i=0;i<inputMetaschema.size();i++){
				SDFSchema s = inputMetaschema.get(i);
				int pos = outputMetaschema.indexOf(s);
				if (pos >= 0){
					mappings.put(i, pos);
				}
			}
		}
		MetadataPO<IStreamObject<IMetaAttribute>> mpo = new MetadataPO<IStreamObject<IMetaAttribute>>(operator.getLocalMetaAttribute(), mappings);
		defaultExecute(operator, mpo, config, true, true);
	}

	@Override
	public boolean isExecutable(MetadataAO operator,
			TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super MetadataAO> getConditionClass() {
		return MetadataAO.class;
	}
	
}
