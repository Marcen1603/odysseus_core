package de.uniol.inf.is.odysseus.logicaloperator.builder;

import de.uniol.inf.is.odysseus.base.AggregateFunction;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

public class AggregateItem {
	public final SDFAttribute outAttribute;
	public AggregateItem(String function, SDFAttribute attribute, SDFAttribute outAttr) {
		this.aggregateFunction = new AggregateFunction(function);
		this.inAttribute = attribute;
		this.outAttribute = outAttr;
	}
	public final AggregateFunction aggregateFunction;
	public final SDFAttribute inAttribute;
}
