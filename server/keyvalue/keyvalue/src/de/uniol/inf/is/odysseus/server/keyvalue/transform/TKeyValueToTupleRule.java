package de.uniol.inf.is.odysseus.server.keyvalue.transform;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimestampAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ToTupleAO;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetadataInitializer;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataRegistry;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.keyvalue.physicaloperator.KeyValueToTuplePO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TKeyValueToTupleRule extends AbstractTransformationRule<ToTupleAO>{

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(ToTupleAO operator, TransformationConfiguration config) throws RuleException {
		ISource<?> inputPO = new KeyValueToTuplePO<IMetaAttribute>(operator);
		this.processMetaData(operator, config, inputPO);
		defaultExecute(operator, inputPO, config, true, false);
	}

	@Override
	public boolean isExecutable(ToTupleAO operator, TransformationConfiguration config) {
		if ((operator.getInputSchema().getType() == KeyValueObject.class) &&
				operator.isAllPhysicalInputSet()) {
			return true;
		}
		return false;
	}

	private void processMetaData(ToTupleAO operator, TransformationConfiguration config, ISource<?> inputPO) {
		if (inputPO instanceof IMetadataInitializer) {
			// New: do no create meta data creation and update, if operator
			// already read the meta data from the source
			if (!config.hasOption("NO_METADATA") && !operator.readMetaData() && this.checkSchema(operator.getOutputSchema())) {
//
				IMetaAttribute type = operator.getLocalMetaAttribute();
				if (type == null) {
					type = MetadataRegistry.getMetadataType(config.getDefaultMetaTypeSet());
				}
				((IMetadataInitializer<?, ?>) inputPO).setMetadataType(type);

				TimestampAO tsAO = getTimestampAOAsFather(operator);
				Class<? extends IMetaAttribute> toC = ITimeInterval.class;
				if (MetadataRegistry.contains(type.getClasses(), toC) && tsAO == null) {
					tsAO = insertTimestampAO(operator, operator.getDateFormat());
				}

			}
		} else {
			if (this.checkSchema(operator.getOutputSchema())) {
				TimestampAO tsAO = getTimestampAOAsFather(operator);
				if (tsAO == null) {
					tsAO = insertTimestampAO(operator, operator.getDateFormat());
				}
			}
		}
	}

	/**
	 * Checks if the schema contains starttimestamp or endtimestamp and metadata need to be updated.
	 *
	 * @param sdfSchema
	 * @return
	 */
	private boolean checkSchema(SDFSchema sdfSchema) {
		for(SDFAttribute att: sdfSchema.getAttributes()) {
			if(att.getDatatype() == SDFDatatype.START_TIMESTAMP || (att.getDatatype() == SDFDatatype.END_TIMESTAMP)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "ToTupleAO --> KeyValueToTuplePO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super ToTupleAO> getConditionClass() {
		return ToTupleAO.class;
	}
}
