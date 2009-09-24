package de.uniol.inf.is.odysseus.relational.base.predicate;

import java.util.List;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author Jonas Jacobi
 */
public interface IRelationalPredicate {
	public void init(SDFAttributeList leftSchema, SDFAttributeList rightSchema);
	public List<SDFAttribute> getAttributes();
}
