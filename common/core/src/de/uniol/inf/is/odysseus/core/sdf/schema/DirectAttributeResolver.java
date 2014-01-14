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
package de.uniol.inf.is.odysseus.core.sdf.schema;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.mep.Variable;
import de.uniol.inf.is.odysseus.core.sdf.schema.AmbiguousAttributeException;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.NoSuchAttributeException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * @author Jonas Jacobi
 */
public class DirectAttributeResolver implements IAttributeResolver, IClone {

	private static final long serialVersionUID = 692060392529144987L;
	protected final SDFSchema schema;

	public DirectAttributeResolver(SDFSchema schema) {
		this.schema = schema;
	}

	public DirectAttributeResolver(Set<Variable> vars) {
		List<SDFAttribute> attribs = new ArrayList<SDFAttribute>();
		for (Variable var : vars) {
			SDFAttribute a = new SDFAttribute(null, var.getIdentifier(),
					var.getReturnType());
			attribs.add(a);
		}
		schema = new SDFSchema("", Tuple.class, attribs);
	}

	public DirectAttributeResolver(
			DirectAttributeResolver directAttributeResolver) {
		this.schema = directAttributeResolver.schema.clone();
	}

	@Override
	public SDFAttribute getAttribute(String name)
			throws AmbiguousAttributeException, NoSuchAttributeException {

		SDFAttribute attribute = this.schema.findAttribute(name);
		if(attribute==null){
			throw new IllegalArgumentException("no such attribute: " + name);
		}else{
			return attribute;
		}
	}

	

	@Override
	public DirectAttributeResolver clone() {
		return new DirectAttributeResolver(this);
	}

	@Override
	public SDFSchema getSchema() {
		return this.schema;
	}
}
