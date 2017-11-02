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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFConstraint;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.unit.SDFUnit;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryException;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;

public class CreateSDFAttributeParameter extends AbstractParameter<SDFAttribute> {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(CreateSDFAttributeParameter.class);

	private static final long serialVersionUID = -544787040358885000L;

	public CreateSDFAttributeParameter() {
	}

	public CreateSDFAttributeParameter(String name, REQUIREMENT requirement, USAGE usage) {
		super(name, requirement, usage);
	}

	public CreateSDFAttributeParameter(String name, REQUIREMENT requirement) {
		this(name, requirement, USAGE.RECENT);
	}

	@Override
	protected void internalAssignment() {
		if (inputValue instanceof SDFAttribute) {
			setValue((SDFAttribute) inputValue);
			return;
		}

		SDFAttribute value = determineAttribute((List<?>) inputValue,getDataDictionary());

		setValue(value);
	}

	@Override
	protected String getPQLStringInternal() {
		String attributeFullName = getValue().getAttributeName();
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		if (!Strings.isNullOrEmpty(getValue().getSourceName())) {
			sb.append("'").append(getValue().getSourceName()).append("',");
		}
		sb.append("'").append(attributeFullName).append("','").append(getValue().getDatatype().getURI()).append("'");
		if (getValue().getUnit() != null || getValue().getDtConstraints().size() > 0) {
			sb.append(",[");
			if (getValue().getUnit() != null) {
				sb.append("['Unit','").append(getValue().getUnit()).append("']");
				if (getValue().getDtConstraints().size() > 0) {
					sb.append(",");
				}
			}
			if (getValue().getDtConstraints().size() > 0) {
				Iterator<SDFConstraint> iter = getValue().getDtConstraints().iterator();
				for (int i = 0; i < getValue().getDtConstraints().size(); i++) {
					SDFConstraint cs = iter.next();
					sb.append("['").append(cs.getURIWithoutQualName()).append("','").append(cs.getValue()).append("']");
					if (i < getValue().getDtConstraints().size() - 1) {
						sb.append(",");
					}
				}
			}
			sb.append("]");
		}

		sb.append("]");
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	public static SDFAttribute determineAttribute(List<?> in, IDataDictionary dd){
		List<?> l1 = new ArrayList<>(in);
		
		List<List<String>> constraintList = null;

		if (l1.get(l1.size() - 1) instanceof List) {
			constraintList = (List<List<String>>) l1.get(l1.size() - 1);
			l1.remove(l1.size() - 1);
		}

		List<String> list = (List<String>) l1;

		if (list.size() == 3) {
			return determineAttribute(list.get(0), list.get(1), list.get(2), constraintList, dd);
		} else if (list.size() == 2) {
			return determineAttribute(null, list.get(0), list.get(1), constraintList, dd);
		} else {
			throw new IllegalArgumentException("Wrong number of inputs for SDFAttribute. Expecting [sourcename] attributename and datatype [constraintlist].");
		}

	}

	public static SDFAttribute determineAttribute(String sourcename, String attributeName, String dataTypeName, List<List<String>> constraintList, IDataDictionary dd) {
		try {
			SDFUnit unit = null;
			List<String> addInfo = new LinkedList<>();
			List<SDFConstraint> dtList = new LinkedList<>();
			if (constraintList != null && constraintList.size() > 0) {
				for (List<String> pair : constraintList) {
					if (pair.size() == 1) {
						addInfo.add(pair.get(0));
					} else if (pair.size() != 2) {
						throw new IllegalArgumentException("Wrong Constraint definition part. Use ['type', 'value']");
					} else {
						if (pair.get(0).equalsIgnoreCase(SDFUnit.class.getSimpleName()) || pair.get(0).equalsIgnoreCase("Unit")) {
							unit = new SDFUnit(pair.get(1));
						} else {
							dtList.add(new SDFConstraint(pair.get(0).toLowerCase(), pair.get(1)));
						}
					}
				}
			}

			int pos = dataTypeName.indexOf('(');
			if (pos == -1) {
				pos = dataTypeName.indexOf('<');
			}
			// TODO: Currently, only List<Integer> is allowed
			// what about List<List<Integer>> ??
			if (pos > 0) {
				String upperTypeStr = dataTypeName.substring(0, pos);
				SDFDatatype upperType = dd.getDatatype(upperTypeStr);
				String subTypeListStr = dataTypeName.substring(pos + 1, dataTypeName.length() - 1);
				final SDFDatatype dataType;
				List<SDFDatatype> subTypeList = new LinkedList<SDFDatatype>();
				String[] subtypes = subTypeListStr.split(",");
				if (subtypes.length > 0) {
					for (String t : subtypes) {
						subTypeList.add(dd.getDatatype(t.trim()));
					}
				} else {
					subTypeList.add(dd.getDatatype(subTypeListStr));
				}
				dataType = dd.getDatatype(upperType, subTypeList);
				return new SDFAttribute(sourcename, attributeName, dataType, unit, dtList);
			}
			return new SDFAttribute(sourcename, attributeName, dd.getDatatype(dataTypeName), unit, dtList, addInfo);
		} catch (DataDictionaryException e) {
			throw new QueryParseException(e);
		}
	}

}
