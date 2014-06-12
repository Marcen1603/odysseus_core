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

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFConstraint;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype.KindOfDatatype;
import de.uniol.inf.is.odysseus.core.sdf.unit.SDFUnit;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;

public class CreateSDFAttributeParameter extends
		AbstractParameter<SDFAttribute> {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(CreateSDFAttributeParameter.class);

	private static final long serialVersionUID = -544787040358885000L;

	public CreateSDFAttributeParameter() {
	}

	public CreateSDFAttributeParameter(String name, REQUIREMENT requirement,
			USAGE usage) {
		super(name, requirement, usage);
	}

	public CreateSDFAttributeParameter(String name, REQUIREMENT requirement) {
		this(name, requirement, USAGE.RECENT);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void internalAssignment() {
		List<?> l1 = (List<?>) inputValue;
		List<List<String>> constraintList = null;
		if (l1.get(l1.size() - 1) instanceof List) {
			constraintList = (List<List<String>>) l1.get(l1.size() - 1);
			l1.remove(l1.size() - 1);
		}

		List<String> list = (List<String>) l1;

		if (list.size() == 3) {
			setValue(determineAttribute(list.get(0), list.get(1), list.get(2),
					constraintList));
		} else if (list.size() == 2) {
			setValue(determineAttribute(null, list.get(0), list.get(1),
					constraintList));
		} else {
			throw new IllegalArgumentException(
					"Wrong number of inputs for SDFAttribute. Expecting [sourcename] attributename and datatype [constraintlist].");
		}
	}

	@Override
	protected String getPQLStringInternal() {
		String attributeFullName = getValue().getAttributeName();
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		if (!Strings.isNullOrEmpty(getValue().getSourceName())) {
			sb.append("'").append(getValue().getSourceName()).append("',");
		}
		sb.append("'").append(attributeFullName).append("','")
				.append(getValue().getDatatype().getURI()).append("'");
		if (getValue().getUnit() != null || getValue().getDtConstraints().size() > 0){
			sb.append(",[");
			if (getValue().getUnit() != null ){
				sb.append("['Unit','").append(getValue().getUnit()).append("']");
				if (getValue().getDtConstraints().size() > 0){
					sb.append(",");
				}
			}
			if (getValue().getDtConstraints().size() > 0){
				Iterator<SDFConstraint> iter = getValue().getDtConstraints().iterator();
				for (int i=0; i<getValue().getDtConstraints().size();i++){
					SDFConstraint cs = iter.next();
					sb.append("['").append(cs.getQualName()).append("','").append(cs.getValue()).append("']");
					if (i<getValue().getDtConstraints().size()-1){
						sb.append(",");
				}
				
			}
			}
			sb.append("]");
		}
		
		sb.append("]");
		return sb.toString();
	}

	private SDFAttribute determineAttribute(String sourcename,
			String attributeName, String dataTypeName,
			List<List<String>> constraintList) {
		try {
			SDFUnit unit = null;
			List<SDFConstraint> dtList = new LinkedList<>();
			if (constraintList != null && constraintList.size() > 0) {
				for (List<String> pair : constraintList) {
					if (pair.size() != 2) {
						throw new IllegalArgumentException(
								"Wrong Constraint definition part. Use ['type', 'value']");
					}
					if (pair.get(0).equalsIgnoreCase(
							SDFUnit.class.getSimpleName())
							|| pair.get(0).equalsIgnoreCase("Unit")) {
						unit = new SDFUnit(pair.get(1));
					} else {
						dtList.add(new SDFConstraint(pair.get(0)
								.toLowerCase(), pair.get(1)));
					}
				}
			}
			
			if (dataTypeName.toLowerCase().startsWith("list(")) {
				String subType = dataTypeName.substring(5, dataTypeName.length() - 1);
				//TODO tranfer to DataDictionary (reuse same Datatypes)
				SDFDatatype dataType = new SDFDatatype("List", KindOfDatatype.LIST, getDataDictionary().getDatatype(subType));
				return new SDFAttribute(sourcename, attributeName, dataType, unit, dtList);
			} else {
				return new SDFAttribute(sourcename, attributeName,
						getDataDictionary().getDatatype(dataTypeName), unit, dtList);
			}
		} catch (DataDictionaryException e) {
			throw new QueryParseException(e);
		}
	}

}
