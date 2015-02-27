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
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.mep.Variable;

/**
 * @author Jonas Jacobi
 */
public class DirectAttributeResolver implements IAttributeResolver, IClone {

	private static final long serialVersionUID = 692060392529144987L;
	private final List<SDFSchema> schemas = new LinkedList<SDFSchema>();

	public DirectAttributeResolver(SDFSchema schema) {
		this.schemas.clear();
		this.schemas.add(schema);
	}

	public DirectAttributeResolver(List<SDFSchema> schemaList) {
		this.schemas.clear();
		this.schemas.addAll(schemaList);
	}

	public DirectAttributeResolver(Set<Variable> vars) {
		List<SDFAttribute> attribs = new ArrayList<SDFAttribute>();
		for (Variable var : vars) {
			SDFAttribute a = new SDFAttribute(null, var.getIdentifier(),
					var.getReturnType(), null, null, null);
			attribs.add(a);
		}
		SDFSchema schema = SDFSchemaFactory.createNewTupleSchema("", attribs);
		this.schemas.clear();
		this.schemas.add(schema);
	}

	public DirectAttributeResolver(
			DirectAttributeResolver directAttributeResolver) {
		schemas.addAll(directAttributeResolver.schemas);
	}

	@Override
	public SDFAttribute getAttribute(String name)
			throws AmbiguousAttributeException, NoSuchAttributeException {
		for (SDFSchema schema : schemas) {
			SDFAttribute attribute = schema.findAttribute(name);
			if (attribute != null) {
				return attribute;
			}
		}
		throw new NoSuchAttributeException("no such attribute: " + name);
	}

	@Override
	public DirectAttributeResolver clone() {
		return new DirectAttributeResolver(this);
	}

	@Override
	public List<SDFSchema> getSchema() {
		return this.schemas;
	}
}
