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
package de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.mep.Variable;
import de.uniol.inf.is.odysseus.core.sdf.schema.AmgigiousAttributeException;
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
		schema = new SDFSchema("", attribs);
	}

	public DirectAttributeResolver(
			DirectAttributeResolver directAttributeResolver) {
		this.schema = directAttributeResolver.schema.clone();
	}

	@Override
	public SDFAttribute getAttribute(String name)
			throws AmgigiousAttributeException, NoSuchAttributeException {
		String[] parts = name.split("\\.", 2); // the attribute can have the
												// form a.b:c:d

		// source name available
		String path[] = null;
		String source = null;
		if (parts.length == 2) {
			path = parts[1].split("\\:"); // split b:c:d into {b, c, d}
			source = parts[0];
		}
		// no source name available
		else {
			path = parts[0].split("\\:"); // split b:c:d into {b, c, d}
		}

		// Test if special attribute, starting with %
		if (source != null && source.startsWith("__")) {
			SDFAttribute attribute = findORAttribute(this.schema, null, path, 0);
			if (attribute != null){
				return new SDFAttribute(source+"."+attribute.getSourceName(),attribute.getAttributeName(), attribute);
			}
			
		} else {

			SDFAttribute attribute = findORAttribute(this.schema, source, path,
					0);
			if (attribute != null)
				return attribute;
		}

		throw new IllegalArgumentException("no such attribute: " + name);

		// SDFAttribute found = null;
		// for (SDFAttribute attr : schema) {
		// if (parts.length == 1) {
		// if ((attr).getAttributeName().equals(name)) {
		// if (found != null) {
		// throw new AmgigiousAttributeException(name);
		// }
		// found = attr;
		// }
		// }
		// else {
		// if (attr.getPointURI().equals(name)) {
		// return attr;
		// }
		// }
		// }
		// if (found == null) {
		// throw new NoSuchAttributeException(name);
		// }
		// return found;
	}

	private SDFAttribute findORAttribute(SDFSchema list, String source,
			String[] path, int index) throws AmgigiousAttributeException {
		String toFind = path[index];
		SDFAttribute curRoot = null;
		for (SDFAttribute attr : list) {

			if (attr.getAttributeName().equals(toFind)
					&& (source == null || attr.getSourceName().equals(source))) {
				if (curRoot == null) {
					curRoot = attr;
				} else {
					throw new AmgigiousAttributeException(
							attr.getAttributeName());
				}
			}
		}

		if (index == path.length - 1) {
			return curRoot;
		} else if (curRoot.getDatatype().hasSchema()) {
			// TODO: MG: Is this correct?
			return findORAttribute(curRoot.getDatatype().getSchema(), null,
					path, index + 1);
		}

		return null;
	}

	@Override
	public DirectAttributeResolver clone() {
		return new DirectAttributeResolver(this);
	}

	public SDFSchema getSchema() {
		return this.schema;
	}
}
