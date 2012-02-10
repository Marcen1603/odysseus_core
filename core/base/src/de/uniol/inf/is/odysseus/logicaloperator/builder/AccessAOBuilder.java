/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.logicaloperator.builder;

import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.IParameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.logicaloperator.TimestampAO;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

/**
 * @author Jonas Jacobi
 */
public class AccessAOBuilder extends AbstractOperatorBuilder {

	private static final long serialVersionUID = 2682090172449918821L;
	private final StringParameter sourceName = new StringParameter(
			"SOURCE", REQUIREMENT.MANDATORY);
	private final IntegerParameter port = new IntegerParameter("PORT",
			REQUIREMENT.OPTIONAL);
	private final StringParameter type = new StringParameter(
			"TYPE", REQUIREMENT.OPTIONAL);
	private final StringParameter host = new StringParameter(
			"HOST", REQUIREMENT.OPTIONAL);
	private final ListParameter<SDFAttribute> attributes = new ListParameter<SDFAttribute>(
			"SCHEMA", REQUIREMENT.OPTIONAL, new CreateSDFAttributeParameter(
					"ATTRIBUTE", REQUIREMENT.MANDATORY, getDataDictionary()));

	public AccessAOBuilder() {
		super(0, 0);
		addParameters(sourceName, host, port, attributes, type);
	}

	@Override
	protected ILogicalOperator createOperatorInternal() {
		String sourceName = this.sourceName.getValue();
		if (getDataDictionary().containsViewOrStream(sourceName, getCaller())) {
			return getDataDictionary().getViewOrStream(sourceName, getCaller());
		}
		ILogicalOperator ao = createNewAccessAO(sourceName);

		return ao;
	}

	private ILogicalOperator createNewAccessAO(String sourceName) {
		SDFSource sdfSource = new SDFSource(sourceName, type.getValue());
		SDFAttributeList schema = new SDFAttributeList(sourceName, attributes.getValue());
		
		getDataDictionary().addSourceType(sourceName,
				"RelationalStreaming");
		getDataDictionary().addEntitySchema(sourceName, schema, getCaller());

		AccessAO ao = new AccessAO(sdfSource);
		ao.setHost(host.getValue());
		ao.setPort(port.getValue());
		ao.setOutputSchema(schema);
		ILogicalOperator op = addTimestampAO(ao);
		return op;
	}

	private ILogicalOperator addTimestampAO(ILogicalOperator operator) {
		TimestampAO timestampAO = new TimestampAO();
		for (SDFAttribute attr : operator.getOutputSchema()) {
			if (SDFDatatype.START_TIMESTAMP.toString().equalsIgnoreCase(attr.getDatatype().getURI())) {
				timestampAO.setStartTimestamp(attr);
			}

			if (SDFDatatype.END_TIMESTAMP.toString().equalsIgnoreCase(attr.getDatatype().getURI())) {
				timestampAO.setEndTimestamp(attr);
			}
		}

		timestampAO.subscribeTo(operator, operator.getOutputSchema());
		return timestampAO;
	}
	
	@Override
	protected boolean internalValidation() {
		String sourceName = this.sourceName.getValue();

		if (getDataDictionary().containsViewOrStream(sourceName, getCaller())) {
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
	
	public AccessAOBuilder cleanCopy() {
		return new AccessAOBuilder();
	}
	
	@Override
	public void setDataDictionary(IDataDictionary dataDictionary) {
		((CreateSDFAttributeParameter)((attributes).getSingleParameter())).setDataDictionary(dataDictionary);
		super.setDataDictionary(dataDictionary);
	}
}
