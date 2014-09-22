/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.AttributeResolver;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * 
 * @author Dennis Geesen Created at: 06.02.2012
 */
public class ContextStoreFunction extends AbstractFunction<Object> {

	private static final long serialVersionUID = 8083562782642549093L;
	private static Logger LOG = LoggerFactory
			.getLogger(ContextStoreFunction.class);
	static final SDFDatatype accTypes[][] = new SDFDatatype[][] { {
			SDFDatatype.STRING, SDFDatatype.INTEGER } };

	public ContextStoreFunction() {
		super("ContextStore", 1, accTypes, SDFDatatype.TUPLE, false);
	}

	@Override
	public Object getValue() {
		String storeName = resolveStoreName();
        if (storeName != null) {
            List<Tuple<? extends ITimeInterval>> values = ContextStoreManager.getStore(storeName).getLastValues();
            if (values == null || values.size() == 0) {
                return "<empty>";
            }
            if (values.size() > 1) {
                LOG.warn("The context store delivered more than one context state, but a function can only handle one! Use enrich instead!");
            }
            return values.get(0);
        }
        return null;
	}

	@Override
	protected SDFDatatype determineReturnType() {
		SDFSchema schema = ContextStoreManager.getStore(resolveStoreName())
				.getSchema();
		AttributeResolver resolver = new AttributeResolver();
		resolver.addAttributes(schema);
		SDFAttribute attribute = resolver.getAttribute(resolveAttributeName());
		return attribute.getDatatype();
	}

	private String resolveStoreName() {
        String inputValue = getInputValue(0);
        if (inputValue != null) {
            String[] symbols = inputValue.split("\\.");
            if (symbols.length >= 2) {
                return symbols[0];
            }
            throw new IllegalArgumentException("for context access you have to define store and attribute like \"thestore.theattribute\"");
        }
        return null;
	}

	private String resolveAttributeName() {
		String inputValue = getInputValue(0);
		String[] symbols = inputValue.split("\\.");
		if (symbols.length >= 2) {
			return symbols[1];
		}
		throw new IllegalArgumentException(
				"for context access you have to define store and attribute like \"thestore.theattribute\"");
	}

}
