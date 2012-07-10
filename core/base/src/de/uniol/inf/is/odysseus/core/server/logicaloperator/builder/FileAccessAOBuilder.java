/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.core.server.logicaloperator.builder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.FileAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;

@Deprecated
public class FileAccessAOBuilder extends AbstractOperatorBuilder {
	private static final long serialVersionUID = 3631495716228164185L;

	private final StringParameter sourceName = new StringParameter("SOURCE",
			REQUIREMENT.MANDATORY);

	private final StringParameter path = new StringParameter("PATH",
			REQUIREMENT.MANDATORY);

	private final StringParameter fileType = new StringParameter("FILETYPE",
			REQUIREMENT.MANDATORY);

	private final StringParameter type = new StringParameter("TYPE",
			REQUIREMENT.MANDATORY);

	private final ListParameter<SDFAttribute> attributes = new ListParameter<SDFAttribute>(
			"SCHEMA", REQUIREMENT.MANDATORY, new CreateSDFAttributeParameter(
					"ATTRIBUTE", REQUIREMENT.MANDATORY, getDataDictionary()));
	
	private final StringParameter separator = new StringParameter("SEPARATOR", REQUIREMENT.OPTIONAL);

	static Logger logger = LoggerFactory.getLogger(FileAccessAOBuilder.class);

	public FileAccessAOBuilder() {
		super("FILE", 0, 0);
		addParameters(sourceName, path, fileType, type, attributes, separator);
	}

	@Override
	protected ILogicalOperator createOperatorInternal() {
		String sourceName = this.sourceName.getValue();
		FileAccessAO ao;

		try {
			if (getDataDictionary().containsViewOrStream(sourceName,
					getCaller())) {
				return getDataDictionary().getViewOrStream(sourceName,
						getCaller());
			}
			ao = createNewFileAccessAO(sourceName);
			getDataDictionary().setView(sourceName, ao, getCaller());
		} catch (Exception e) {
			throw new QueryParseException(e.getMessage());
		}
		return ao;
	}

	private FileAccessAO createNewFileAccessAO(String sourceName) {
		SDFSchema schema = new SDFSchema(sourceName, attributes.getValue());

		
		getDataDictionary().addSourceType(sourceName, "RelationalStreaming");
		getDataDictionary().addEntitySchema(sourceName, schema, getCaller());

		FileAccessAO ao = new FileAccessAO(sourceName, type.getValue(), null);
		ao.setPath(path.getValue());
		ao.setFileType(fileType.getValue());
		ao.setSeparator(separator.getValue());

		ao.setOutputSchema(schema);
		return ao;
	}

	@Override
	protected boolean internalValidation() {
		String sourceName = this.sourceName.getValue();

		if (separator.getValue() == null)
			separator.setInputValue(";");

		if (!(path.hasValue() && type.hasValue() && fileType.hasValue() && attributes
				.hasValue())) {
			addError(new IllegalArgumentException(
					"missing information for the creation of source "
							+ sourceName
							+ ". expecting path, fileType, type and attributes."));
			return false;
		}
		return true;
	}
	
	@Override
	public FileAccessAOBuilder cleanCopy() {
		return new FileAccessAOBuilder();
	}
	
}
