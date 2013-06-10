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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryException;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;

public class CreateSDFAttributeParameter extends AbstractParameter<SDFAttribute> {
	
	private static final Logger LOG = LoggerFactory.getLogger(CreateSDFAttributeParameter.class);
	
	private static final long serialVersionUID = -544787040358885000L;
	
	private static IDataDictionary dataDictionary;

	public CreateSDFAttributeParameter() {
	}

	public CreateSDFAttributeParameter(String name, REQUIREMENT requirement, USAGE usage) {
		super(name, requirement, usage);
	}

	public CreateSDFAttributeParameter(String name, REQUIREMENT requirement) {
		this(name, requirement, USAGE.RECENT);
	}
	
	// called by OSGi-DS
	public void bindDataDictionary( IDataDictionary dd ) {
		dataDictionary = dd;
		
		LOG.debug("Data Dictionary bound");
	}
	
	// called by OSGi-DS
	public void unbindDataDictionary( IDataDictionary dd ) {
		if( dataDictionary == dd ) {
			dataDictionary = null;
			LOG.debug("Data Dictionary bound");
		}
	}	

	@SuppressWarnings("unchecked")
	@Override
	protected void internalAssignment() {
		List<String> list = (List<String>) inputValue;
		if (list.size() != 2) {
			throw new IllegalArgumentException("Wrong number of inputs for SDFAttribute. Expecting id and datatype.");
		}
		setValue(determineAttribute(list.get(0), list.get(1)));
	}

	@Override
	protected String getPQLStringInternal() {
		String attributeFullName = getValue().getAttributeName();
		if (!Strings.isNullOrEmpty(getValue().getSourceName())) {
			attributeFullName = getValue().getSourceName() + "." + attributeFullName;
		}

		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append("'").append(attributeFullName).append("','").append(getValue().getDatatype().getURI()).append("'");
		sb.append("]");
		return sb.toString();
	}

	private SDFAttribute determineAttribute(String attributeName, String dataTypeName) {
		try {
			final int pos = attributeName.indexOf(".");
			if (pos != -1) {
				final String prefix = attributeName.substring(0, pos);
				return new SDFAttribute(prefix, attributeName.substring(pos + 1), dataDictionary.getDatatype(dataTypeName));
			}
			return new SDFAttribute(null, attributeName, dataDictionary.getDatatype(dataTypeName));
		} catch (DataDictionaryException e) {
			throw new QueryParseException(e);
		}
	}

}
