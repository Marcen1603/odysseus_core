package de.uniol.inf.is.odysseus.spatial.predicate;

import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTIdentifier;
import de.uniol.inf.is.odysseus.parser.cql.parser.AbstractPredicate;
import de.uniol.inf.is.odysseus.parser.cql.parser.transformation.IPredicateBuilder;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.IAttributeResolver;


/**
 * @author Jonas Jacobi
 */
public class SpatialPredicateBuilder implements IPredicateBuilder {

	@Override
	public IPredicate<RelationalTuple<?>> createPredicate(
			AbstractPredicate spChild, IAttributeResolver attributeResolver) {
		String opStr = spChild.jjtGetChild(1).toString();
		String left = ((ASTIdentifier) spChild.jjtGetChild(0)).toString();
		String right = ((ASTIdentifier) spChild.jjtGetChild(2)).toString();
		SpatialPredicateType op;

		if (opStr.equalsIgnoreCase("SPATIAL_INTERSECTS")) {
			op = SpatialPredicateType.SPATIAL_INTERSECTS;
		} else if (opStr.equalsIgnoreCase("SPATIAL_MEETS")) {
			op = SpatialPredicateType.SPATIAL_MEETS;
		} else if (opStr.equalsIgnoreCase("SPATIAL_TOUCHES")) {
			op = SpatialPredicateType.SPATIAL_TOUCHES;
		} else if (opStr.equalsIgnoreCase("SPATIAL_ON")) {
			op = SpatialPredicateType.SPATIAL_ON;
		} else if (opStr.equalsIgnoreCase("SPATIAL_IN")) {
			op = SpatialPredicateType.SPATIAL_IN;
		} else if (opStr.equalsIgnoreCase("SPATIAL_OUT")) {
			op = SpatialPredicateType.SPATIAL_OUT;
		} else {
			throw new RuntimeException("no such operator: " + opStr);
		}

		return new SpatialPredicate(left, right, attributeResolver, op);
	}

}
