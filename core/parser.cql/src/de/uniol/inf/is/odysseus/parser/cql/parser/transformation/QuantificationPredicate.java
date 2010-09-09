package de.uniol.inf.is.odysseus.parser.cql.parser.transformation;

import de.uniol.inf.is.odysseus.base.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.parser.cql.parser.AbstractQuantificationPredicate;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

/**
 * This is a dummy class for the conversion of the AST to real predicates. In a
 * later stage the QuantificationPredicates get converted to Existenceoperators,
 * so there is no need for evaluation methods.
 * 
 * @author Jonas Jacobi
 */
public class QuantificationPredicate extends
		AbstractPredicate<RelationalTuple<?>> {

	private static final long serialVersionUID = -125391059150938434L;
	private AbstractQuantificationPredicate astPredicate;

	public QuantificationPredicate(AbstractQuantificationPredicate astPredicate) {
		super();
		this.astPredicate = astPredicate;
	}

	public QuantificationPredicate(QuantificationPredicate pred) {
		this.astPredicate = pred.astPredicate;
	}

	public boolean evaluate(RelationalTuple<?> input) {
		throw new RuntimeException("Not implemented");
	}

	public boolean evaluate(RelationalTuple<?> left, RelationalTuple<?> right) {
		throw new RuntimeException("Not implemented");
	}

	public AbstractQuantificationPredicate getAstPredicate() {
		return astPredicate;
	}

	public void setAstPredicate(AbstractQuantificationPredicate astPredicate) {
		this.astPredicate = astPredicate;
	}

	@Override
	public QuantificationPredicate clone() {
		return new QuantificationPredicate(this);
	}
}
