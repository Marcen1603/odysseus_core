package de.uniol.inf.is.odysseus.relational.base.predicate;

import java.util.List;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

public interface IRelationalPredicate {
	public List<SDFAttribute> getAttributes();
}
