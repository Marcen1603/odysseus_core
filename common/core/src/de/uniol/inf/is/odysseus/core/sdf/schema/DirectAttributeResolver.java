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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.mep.IMepVariable;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype.KindOfDatatype;

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

	public DirectAttributeResolver(List<SDFSchema> schemaList, List<SDFSchema> schemaList2) {
		this.schemas.clear();
		this.schemas.addAll(schemaList);
		this.schemas.addAll(schemaList2);
	}


	public DirectAttributeResolver(Set<IMepVariable> vars) {
		List<SDFAttribute> attribs = new ArrayList<SDFAttribute>();
		for (IMepVariable var : vars) {
			SDFAttribute a = new SDFAttribute(null, var.getIdentifier(), var.getReturnType(), null, null, null);
			attribs.add(a);
		}
		SDFSchema schema = SDFSchemaFactory.createNewTupleSchema("", attribs);
		this.schemas.clear();
		this.schemas.add(schema);
	}

	public DirectAttributeResolver(DirectAttributeResolver directAttributeResolver) {
		schemas.addAll(directAttributeResolver.schemas);
	}

	@Override
	public SDFAttribute getAttribute(String name) throws AmbiguousAttributeException, NoSuchAttributeException {
		for (SDFSchema schema : schemas) {
			SDFAttribute attribute = schema.findAttribute(name);
			if (attribute != null) {
				return attribute;
			}
		}
		// TODO: more generic
		if (name.equalsIgnoreCase(SDFAttribute.THIS) && schemas.size() == 1) {
			SDFDatatype dt;
			if (schemas.get(0).getType() == Tuple.class) {
				dt = new SDFDatatype(schemas.get(0).getURIWithoutQualName(), KindOfDatatype.TUPLE,
						schemas.get(0));
				return new SDFAttribute(schemas.get(0).getURIWithoutQualName(), SDFAttribute.THIS, dt);
			}

			return new SDFAttribute(schemas.get(0).getURIWithoutQualName(), SDFAttribute.THIS, SDFDatatype.OBJECT);
		}
		throw new NoSuchAttributeException("no such attribute: " + name);
	}

	@Override
	public Set<SDFAttribute> getAllAttributes() {
		Set<SDFAttribute> set = new HashSet<>();
		for (SDFSchema schema: schemas){
			set.addAll(schema.getAttributes());
		}
		return set;
	}

	@Override
	public boolean isEmpty() {
		for (SDFSchema schema: schemas){
			if (schema.size() != 0){
				return false;
			}
		}
		return true;
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
