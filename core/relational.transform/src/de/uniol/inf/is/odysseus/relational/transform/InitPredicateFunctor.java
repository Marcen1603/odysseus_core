package de.uniol.inf.is.odysseus.relational.transform;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.relational.base.predicate.IRelationalPredicate;
import de.uniol.inf.is.odysseus.relational.transform.TInitPredicateRule.IUnaryFunctor;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * Initializes IRelationalal predicates with the input schemas of a given
 * ILogicalOperator.
 * @author Jonas Jacobi
 */
public class InitPredicateFunctor implements IUnaryFunctor<IPredicate<?>> {

	private final SDFAttributeList leftSchema;
	private final SDFAttributeList rightSchema;
	
	public InitPredicateFunctor(ILogicalOperator op) {
		this.leftSchema = op.getInputSchema(0);
		this.rightSchema = op.getNumberOfInputs() > 1 ? op.getInputSchema(1) : null;
	}
	@Override
	public void call(IPredicate<?> parameter) {
		if(parameter instanceof IRelationalPredicate) {
			((IRelationalPredicate)parameter).init(leftSchema, rightSchema);				
		}
	}

}
