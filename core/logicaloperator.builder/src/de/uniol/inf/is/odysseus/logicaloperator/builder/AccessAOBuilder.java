package de.uniol.inf.is.odysseus.logicaloperator.builder;

import java.util.List;

import de.uniol.inf.is.odysseus.datadictionary.DataDictionary;
import de.uniol.inf.is.odysseus.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFEntity;

/**
 * @author Jonas Jacobi
 */
public class AccessAOBuilder extends AbstractOperatorBuilder {

	private final DirectParameter<String> sourceName = new DirectParameter<String>(
			"SOURCE", REQUIREMENT.MANDATORY);
	private final IntegerParameter port = new IntegerParameter("PORT",
			REQUIREMENT.OPTIONAL);
	private final DirectParameter<String> type = new DirectParameter<String>(
			"TYPE", REQUIREMENT.OPTIONAL);
	private final DirectParameter<String> host = new DirectParameter<String>(
			"HOST", REQUIREMENT.OPTIONAL);
	private final ListParameter<SDFAttribute> attributes = new ListParameter<SDFAttribute>(
			"SCHEMA", REQUIREMENT.OPTIONAL, new CreateSDFAttributeParameter(
					"ATTRIBUTE", REQUIREMENT.MANDATORY));

	public AccessAOBuilder() {
		super(0, 0);
		setParameters(sourceName, host, port, type, attributes);
	}

	protected ILogicalOperator createOperatorInternal() {
		String sourceName = this.sourceName.getValue();
		if (DataDictionary.getInstance().containsView(sourceName, getCaller())) {
			return DataDictionary.getInstance().getView(sourceName, getCaller());
		}
		AccessAO ao = createNewAccessAO(sourceName);

		return ao;
	}

	private AccessAO createNewAccessAO(String sourceName) {
		SDFSource sdfSource = new SDFSource(sourceName, type.getValue());
		SDFEntity sdfEntity = new SDFEntity(sourceName);
		List<SDFAttribute> attributeList = attributes.getValue();
		SDFAttributeList schema = new SDFAttributeList(attributeList);
		sdfEntity.setAttributes(schema);

		DataDictionary.getInstance().addSourceType(sourceName,
				"RelationalStreaming");
		DataDictionary.getInstance().addEntity(sourceName, sdfEntity, getCaller());

		AccessAO ao = new AccessAO(sdfSource);
		ao.setHost(host.getValue());
		ao.setPort(port.getValue());
		ao.setOutputSchema(schema);
		return ao;
	}

	@Override
	protected boolean internalValidation() {
		String sourceName = this.sourceName.getValue();

		if (DataDictionary.getInstance().containsView(sourceName, getCaller())) {
			if (host.hasValue() || type.hasValue() || port.hasValue()
					|| attributes.hasValue()) {
				addError(new IllegalArgumentException("view " + sourceName
						+ " already exists"));
				return false;
			}
		} else {
			if (!(host.hasValue() && type.hasValue() && port.hasValue() && attributes
					.hasValue())) {
				addError(new IllegalArgumentException(
						"missing information for the creation of source "
								+ sourceName
								+ ". expecting source, host, port, type and schema."));
				return false;
			}
		}
		return true;
	}
}
