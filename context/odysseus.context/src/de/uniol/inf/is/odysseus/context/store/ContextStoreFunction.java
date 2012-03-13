/** Copyright 2011 The Odysseus Team
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

package de.uniol.inf.is.odysseus.context.store;

import de.uniol.inf.is.odysseus.context.ContextManagementException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.AttributeResolver;

/**
 * 
 * @author Dennis Geesen Created at: 06.02.2012
 */
public class ContextStoreFunction extends AbstractFunction<Object> {

	private static final long serialVersionUID = 8083562782642549093L;

	@Override
	public int getArity() {
		return 1;
	}

	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos) {
		SDFDatatype types[] = { SDFDatatype.STRING, SDFDatatype.INTEGER };
		return types;
	}

	@Override
	public String getSymbol() {
		return "ContextStore";
	}

	@Override
	public boolean optimizeConstantParameter() {
		return false;
	}

	@Override
	public Object getValue() {
		String storeName = resolveStoreName();
		try {
			Object value = ContextStore.getInstance().getValue(storeName);
			if (value == null) {
				return "<empty>";
			}
            return value;
		} catch (ContextManagementException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public SDFDatatype getReturnType() {
		try {
			SDFSchema schema = ContextStore.getInstance().getStoreSchema(resolveStoreName());
			AttributeResolver resolver = new AttributeResolver();
			resolver.addAttributes(schema);
			SDFAttribute attribute = resolver.getAttribute(resolveAttributeName());
			return attribute.getDatatype();
		} catch (ContextManagementException e) {
			e.printStackTrace();
		}
		return SDFDatatype.OBJECT;
	}

	private String resolveStoreName() {
		String inputValue = getInputValue(0);
		String[] symbols = inputValue.split("\\.");
		if (symbols.length >= 2) {
			return symbols[0];
		}
		throw new IllegalArgumentException("for context access you have to define store and attribute like \"thestore.theattribute\"");
	}

	private String resolveAttributeName() {
		String inputValue = getInputValue(0);
		String[] symbols = inputValue.split("\\.");
		if (symbols.length >= 2) {
			return symbols[1];
		}
		throw new IllegalArgumentException("for context access you have to define store and attribute like \"thestore.theattribute\"");
	}

}
