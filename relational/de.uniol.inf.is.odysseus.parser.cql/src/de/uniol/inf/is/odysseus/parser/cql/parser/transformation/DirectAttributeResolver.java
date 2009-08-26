package de.uniol.inf.is.odysseus.parser.cql.parser.transformation;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author Jonas Jacobi
 */
public class DirectAttributeResolver implements IAttributeResolver {

	private static final long serialVersionUID = 692060392529144987L;
	private SDFAttributeList schema;

	public DirectAttributeResolver(SDFAttributeList schema) {
		this.schema = schema;
	}

	public SDFAttribute getAttribute(String name) {
		for(SDFAttribute attr : schema){
			if (attr.getURI(false).equals(name)){
				return attr;
			}
		}
		return null;
	}

}
