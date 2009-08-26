package de.uniol.inf.is.odysseus.sourcedescription.sdf.schema;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

/**
 * @author Jonas Jacobi
 *
 */
public interface IAttributeResolver extends Serializable {

	public SDFAttribute getAttribute(String name);

}