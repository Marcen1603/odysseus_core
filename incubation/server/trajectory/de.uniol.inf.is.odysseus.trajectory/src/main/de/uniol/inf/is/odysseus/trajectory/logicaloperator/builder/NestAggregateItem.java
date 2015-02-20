package de.uniol.inf.is.odysseus.trajectory.logicaloperator.builder;

import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AggregateItem;

/**
 * Specialization of <tt>AggregateItem</tt> which only allows <i>NEST</i> aggregations.
 * 
 * @author marcus
 *
 */
public class NestAggregateItem extends AggregateItem {

	public NestAggregateItem(final List<SDFAttribute> attributes, final SDFAttribute outAttr) {
		super("NEST", attributes, outAttr);
	}

	public NestAggregateItem(final SDFAttribute attribute,
			final SDFAttribute outAttr) {
		super("NEST", attribute, outAttr);
	}	
}
