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
import java.util.List;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryException;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunctionBuilderRegistry;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IAggregateFunctionBuilder;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;

public class AggregateItemParameter extends AbstractParameter<AggregateItem> {

	private static final long serialVersionUID = 579333420134666505L;

	public AggregateItemParameter() {
		super();
	}

	public AggregateItemParameter(String name, REQUIREMENT requirement, USAGE usage) {
		super(name, requirement, usage);
	}

	public AggregateItemParameter(String name, REQUIREMENT requirement) {
		super(name, requirement, USAGE.RECENT);
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	protected void internalAssignment() {
		if (inputValue instanceof AggregateItem) {
			setValue((AggregateItem) inputValue);
			return;
		}

		List<String> value = (List<String>) inputValue;
		if ((value.size() < 3) || (value.size() > 4)) {
			throw new IllegalParameterException("illegal value for aggregation");
		}

		String funcStr = value.get(0);
		List<SDFAttribute> attributes = new ArrayList<>();
		if (((Object) value.get(1)) instanceof List) {
			List<String> attributeList = (List<String>) ((Object) value.get(1));
			for (String attr : attributeList) {
				attributes.add(getAttributeResolver().getAttribute(attr));
			}
		} else {
			String attributeStr = value.get(1);
			if (attributeStr.equals("*")) {
				attributes.addAll(getAttributeResolver().getSchema().get(0).getAttributes());
			} else {
				attributes.add(getAttributeResolver().getAttribute(attributeStr));
			}
		}
		String outputName = value.get(2);
		SDFAttribute outAttr = null;

		try {
			if (value.size() == 4) {
				IDataDictionary dd = getDataDictionary();
				SDFDatatype type;

				type = dd.getDatatype(value.get(3));

				outAttr = new SDFAttribute(null, outputName, type, null, null, null);
			} else {

				@SuppressWarnings("rawtypes")
				Class<? extends IStreamObject> datamodell = getAttributeResolver().getSchema().get(0).getType();
				IAggregateFunctionBuilder builder = AggregateFunctionBuilderRegistry.getBuilder(datamodell, funcStr);
				if (builder == null) {
					throw new QueryParseException("Cannot find aggregate function " + funcStr + " for " + datamodell);
				}
				
				SDFDatatype type = builder.getDatatype(funcStr, attributes);

				if (type == null) {
					// Fallback to old DOUBLE value for aggregation results
					IDataDictionary dd = getDataDictionary();
					type = dd.getDatatype("double");
				}
				outAttr = new SDFAttribute(null, outputName, type, null, null, null);
			}
		} catch (DataDictionaryException e) {
			throw new QueryParseException(e.getMessage());
		}
		setValue(new AggregateItem(funcStr, attributes, outAttr));
	}

	@SuppressWarnings("unchecked")
	@Override
	protected String getPQLStringInternal() {
		if (inputValue instanceof AggregateItem) {

			AggregateItem a = (AggregateItem) inputValue;
			StringBuffer pql = new StringBuffer();

			List<String> value = Lists.newArrayList(a.aggregateFunction.getName());
			if (a.inAttributes.size() == 1) {
				value.add(a.inAttributes.get(0).getURI());

			} else {
				String attributes = "[";
				for (SDFAttribute inAttribute : a.inAttributes) {
					attributes += "'" + inAttribute.getURI() + "',";
				}
				attributes = attributes.substring(0, attributes.length() - 1);
				attributes += "]";

				value.add(attributes);
			}
			value.add(a.outAttribute.getURI());
			value.add(a.outAttribute.getDatatype().getURI());

			pql.append(AggregateItemParameter.getPQLString(value));
			pql.append(",");

			return pql.substring(0, pql.length() - 1);
		}
		return getPQLString((List<String>) inputValue);
	}

	static public String getPQLString(List<String> value) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append("'").append(value.get(0)).append("','").append((Object) value.get(1)).append("','")
				.append(value.get(2)).append("'");
		if (value.size() == 4) {
			sb.append(",'").append(value.get(3)).append("'");
		}
		sb.append("]");
		return sb.toString();
	}
}
