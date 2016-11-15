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

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.sdf.schema.NoSuchAttributeException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

public class ResolvedSDFAttributeParameter extends
		AbstractParameter<SDFAttribute> {

	private static final long serialVersionUID = -8628692018760465548L;

	public ResolvedSDFAttributeParameter(String name, REQUIREMENT requirement) {
		super(name, requirement, USAGE.RECENT);
	}

	public ResolvedSDFAttributeParameter(String name, REQUIREMENT requirement, USAGE usage) {
		super(name, requirement, usage);
	}

	public ResolvedSDFAttributeParameter() {
	}

	@Override
	protected void internalAssignment() {

		if( inputValue instanceof SDFAttribute ) {
			setValue((SDFAttribute)inputValue);
			return;
		}

		if (getAttributeResolver() == null) {
			throw new RuntimeException("missing attribute resolver");
		}
		try {
			boolean checkInputSchema = false;
			if (getAttributeResolver().getSchema().size() > 0){
				checkInputSchema = !getAttributeResolver().getSchema().get(0).getType().newInstance().isSchemaLess();
			}

			SDFAttribute attribute;
			if (checkInputSchema){
				attribute = getAttributeResolver().getAttribute(
						(String) this.inputValue);
			}else{
				// Create a new Attribute (e.g. in case of key value)
				if (this.inputValue instanceof List){
					attribute = CreateSDFAttributeParameter.determineAttribute((List<?>)this.inputValue, getDataDictionary());
				}else if (this.inputValue instanceof String){
					attribute = new SDFAttribute(null,(String)inputValue, SDFDatatype.STRING);
				}else{
					attribute = null;
				}
			}

			setValue(attribute);
		}catch(NoSuchAttributeException e){
			throw new RuntimeException("Unable to find attribute "+inputValue+" in input.", e);
		} catch (Exception ex) {
			throw new RuntimeException("cannot assign attribute value", ex);
		}
	}

	@Override
	protected String getPQLStringInternal() {
		if( !Strings.isNullOrEmpty(getValue().getSourceName())) {
			return "'" + getValue().getSourceName() + "." + getValue().getAttributeName() + "'";
		}
		return "'" + getValue().getAttributeName() + "'";
	}
}
