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
package de.uniol.inf.is.odysseus.core.server.logicaloperator.builder;

import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryException;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;

public class AggregateItemParameter extends AbstractParameter<AggregateItem> {

	private static final long serialVersionUID = 579333420134666505L;

	public AggregateItemParameter() {
		super();
	}

	public AggregateItemParameter(String name, REQUIREMENT requirement) {
		super(name, requirement);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void internalAssignment() {
		List<String> value = (List<String>) inputValue;
		if ((value.size() < 3) || (value.size() > 4)) {
			throw new IllegalParameterException("illegal value for aggregation");
		}

		String funcStr = value.get(0);
		String attributeStr = value.get(1);
		SDFAttribute attribute = getAttributeResolver().getAttribute(
				attributeStr);
		String outputName = value.get(2);
		SDFAttribute outAttr = null;

		try {
			if (value.size() == 4) {
				IDataDictionary dd = getDataDictionary();
				SDFDatatype type;

				type = dd.getDatatype(value.get(3));

				outAttr = new SDFAttribute(null, outputName, type);
			} else {
				// Fallback to old DOUBLE value for aggregation results
				IDataDictionary dd = getDataDictionary();
				SDFDatatype type = dd.getDatatype("double");
				outAttr = new SDFAttribute(null, outputName, type);
			}
		} catch (DataDictionaryException e) {
			throw new QueryParseException(e.getMessage());
		}
		setValue(new AggregateItem(funcStr, attribute, outAttr));
	}

}
