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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFConstraint;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.sdf.unit.SDFUnit;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;

public class CreateSDFMetaSchemaParameter extends AbstractParameter<SDFMetaSchema> {

	private static final Logger LOG = LoggerFactory.getLogger(CreateSDFMetaSchemaParameter.class);

	private static final long serialVersionUID = -544787040358885000L;

	public CreateSDFMetaSchemaParameter() {
	}

	public CreateSDFMetaSchemaParameter(String name, REQUIREMENT requirement, USAGE usage) {
		super(name, requirement, usage);
	}

	public CreateSDFMetaSchemaParameter(String name, REQUIREMENT requirement) {
		this(name, requirement, USAGE.RECENT);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void internalAssignment() {
		if (inputValue instanceof SDFMetaSchema) {
			setValue((SDFMetaSchema) inputValue);
			return;
		}

		List<?> l1 = (List<?>) inputValue;
		
		if(l1.size()==4) {
			List<List<?>> attributeList;
			attributeList = (List<List<?>>) l1.get(l1.size() - 1);
			List<SDFAttribute> attributes = resolveAttributeList(attributeList);
		
			List<String> l1AsStrings = (List<String>)l1.subList(0, 3);
			
			
			try {
				String uri = l1AsStrings.get(0);
				String metaAttributeName = l1AsStrings.get(1);
				Class metaAttributeClass;
				metaAttributeClass = Class.forName(metaAttributeName);
				String metaElementDataType = l1AsStrings.get(2);
				Class metaElementDataTypeClass;
				metaElementDataTypeClass = Class.forName(metaElementDataType);
				SDFMetaSchema metaSchema = SDFSchemaFactory.createNewMetaSchema(uri, metaElementDataTypeClass, attributes, metaAttributeClass);
				setValue(metaSchema);
			} catch (ClassNotFoundException e) {
				LOG.error("Class for meta Element DataType or Meta Attribute not found: {}",e);
				e.printStackTrace();
			}
			
			
		} else {
			throw new IllegalArgumentException("Wrong number of inputs for SDFMetaSchema.");
		}
		
	}

	@SuppressWarnings("unchecked")
	private List<SDFAttribute> resolveAttributeList(List<List<?>> attributeList) {
		
		List<SDFAttribute> resolvedAttributes = Lists.newArrayList();
		
		for(List<?> singleAttribute : attributeList) {
			
			List<List<String>> constraintList = null;
			
			if (singleAttribute.get(singleAttribute.size() - 1) instanceof List) {
				constraintList = (List<List<String>>) singleAttribute.get(singleAttribute.size() - 1);
				singleAttribute.remove(singleAttribute.size() - 1);
			}
			
			List<String> attribute = (List<String>) singleAttribute;
			
				if(attribute.size()!=3) {
					LOG.error("At least one Attribute could not resolved: {}",attribute.toString());
					continue;
				}
				String sourceName = attribute.get(0);
				String attributeName = attribute.get(1);
				String dataTypename = attribute.get(2);
				resolvedAttributes.add(determineAttribute(sourceName, attributeName, dataTypename, constraintList));
				
			}
			
		return resolvedAttributes;
	}

	@Override
	protected String getPQLStringInternal() {
		String uri = getValue().getURI();
		String metaelement = getValue().getMetaAttribute().getName();
		String metaElementDataType = getValue().getType().getName();
		
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append("'");
		sb.append(uri);
		sb.append("','");
		sb.append(metaelement);
		sb.append("','");
		sb.append(metaElementDataType);
		sb.append("',[");
		
		Iterator<SDFAttribute> iter = getValue().iterator();
		while(iter.hasNext()) {
			SDFAttribute nextAttribute = iter.next();
			sb.append(getPQLForAttribute(nextAttribute));
			if(iter.hasNext()) {
				sb.append(",");
			}
		}
		
		sb.append("]]");
		
		
		return sb.toString();
	}
	
	private String getPQLForAttribute(SDFAttribute attribute) {
		String attributeFullName = attribute.getAttributeName();
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		
		if (!Strings.isNullOrEmpty(attribute.getSourceName())) {
			sb.append("'").append(attribute.getSourceName()).append("',");
		}
		
		sb.append("'").append(attributeFullName).append("','").append(attribute.getDatatype().getURI()).append("'");
		if (attribute.getUnit() != null || attribute.getDtConstraints().size() > 0) {
			sb.append(",[");
			if (attribute.getUnit() != null) {
				sb.append("['Unit','").append(attribute.getUnit()).append("']");
				if (attribute.getDtConstraints().size() > 0) {
					sb.append(",");
				}
			}
			if (attribute.getDtConstraints().size() > 0) {
				Iterator<SDFConstraint> iter = attribute.getDtConstraints().iterator();
				for (int i = 0; i < attribute.getDtConstraints().size(); i++) {
					SDFConstraint cs = iter.next();
					sb.append("['").append(cs.getURIWithoutQualName()).append("','").append(cs.getValue()).append("']");
					if (i < attribute.getDtConstraints().size() - 1) {
						sb.append(",");
					}
				}
			}
			sb.append("]");
		}

		sb.append("]");
		return sb.toString();
	}
	

	private SDFAttribute determineAttribute(String sourcename, String attributeName, String dataTypeName, List<List<String>> constraintList) {
		try {
			SDFUnit unit = null;
			List<SDFConstraint> dtList = new LinkedList<>();
			if (constraintList != null && constraintList.size() > 0) {
				for (List<String> pair : constraintList) {
					if (pair.size() != 2) {
						throw new IllegalArgumentException("Wrong Constraint definition part. Use ['type', 'value']");
					}
					if (pair.get(0).equalsIgnoreCase(SDFUnit.class.getSimpleName()) || pair.get(0).equalsIgnoreCase("Unit")) {
						unit = new SDFUnit(pair.get(1));
					} else {
						dtList.add(new SDFConstraint(pair.get(0).toLowerCase(), pair.get(1)));
					}
				}
			}

			int pos = dataTypeName.indexOf('(');
			if (pos == -1) {
				pos = dataTypeName.indexOf('<');
			}
			if (pos > 0) {
				String upperTypeStr = dataTypeName.substring(0, pos);
				SDFDatatype upperType = getDataDictionary().getDatatype(upperTypeStr);
				String subTypeListStr = dataTypeName.substring(pos + 1, dataTypeName.length() - 1);
				final SDFDatatype dataType;
				List<SDFDatatype> subTypeList = new LinkedList<SDFDatatype>();
				String[] subtypes = subTypeListStr.split(",");
				if (subtypes.length > 0) {
					for (String t : subtypes) {
						subTypeList.add(getDataDictionary().getDatatype(t.trim()));
					}
				} else {
					subTypeList.add(getDataDictionary().getDatatype(subTypeListStr));
				}
				dataType = getDataDictionary().getDatatype(upperType, subTypeList);
				return new SDFAttribute(sourcename, attributeName, dataType, unit, dtList);
			}
			return new SDFAttribute(sourcename, attributeName, getDataDictionary().getDatatype(dataTypeName), unit, dtList);
		} catch (DataDictionaryException e) {
			throw new QueryParseException(e);
		}
	}

}
