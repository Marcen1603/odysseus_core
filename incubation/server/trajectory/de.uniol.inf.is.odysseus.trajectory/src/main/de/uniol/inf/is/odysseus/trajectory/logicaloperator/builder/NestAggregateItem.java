package de.uniol.inf.is.odysseus.trajectory.logicaloperator.builder;

import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AggregateItem;

public class NestAggregateItem extends AggregateItem {

	public NestAggregateItem(List<SDFAttribute> attributes, SDFAttribute outAttr) {
		super("NEST", attributes, outAttr);
	}

	public NestAggregateItem(SDFAttribute attribute,
			SDFAttribute outAttr) {
		super("NEST", attribute, outAttr);
	}	
}
