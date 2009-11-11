package de.uniol.inf.is.odysseus.parser.cql.parser.transformation;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.predicate.AndPredicate;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.base.predicate.NotPredicate;
import de.uniol.inf.is.odysseus.base.predicate.OrPredicate;
import de.uniol.inf.is.odysseus.parser.cql.IVisitor;
import de.uniol.inf.is.odysseus.parser.cql.VisitorFactory;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAndPredicate;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTBasicPredicate;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTNotPredicate;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTOrPredicate;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTPredicate;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTProbabilityPredicate;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTQuantificationPredicate;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSimplePredicate;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSpatialPredicate;
import de.uniol.inf.is.odysseus.parser.cql.parser.AbstractQuantificationPredicate;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

@SuppressWarnings("unchecked")
public class CreatePredicateVisitor extends AbstractDefaultVisitor {

	@Override
	public Object visit(ASTPredicate node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTSimplePredicate node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTBasicPredicate node, Object data) {
		SDFExpression expression = new SDFExpression("", node.getPredicate(),
				(IAttributeResolver) data);
		return new RelationalPredicate(expression);
	}

	@Override
	public Object visit(ASTAndPredicate node, Object data) {
		IPredicate<? super RelationalTuple<?>> left = (IPredicate<? super RelationalTuple<?>>) node
				.jjtGetChild(0).jjtAccept(this, data);
		IPredicate<? super RelationalTuple<?>> right = (IPredicate<? super RelationalTuple<?>>) node
				.jjtGetChild(1).jjtAccept(this, data);
		return new AndPredicate<RelationalTuple<?>>(left, right);
	}

	@Override
	public Object visit(ASTOrPredicate node, Object data) {
		IPredicate<? super RelationalTuple<?>> left = (IPredicate<? super RelationalTuple<?>>) node
				.jjtGetChild(0).jjtAccept(this, data);
		IPredicate<? super RelationalTuple<?>> right = (IPredicate<? super RelationalTuple<?>>) node
				.jjtGetChild(1).jjtAccept(this, data);
		return new OrPredicate<RelationalTuple<?>>(left, right);
	}

	@Override
	public Object visit(ASTNotPredicate node, Object data) {
		IPredicate<RelationalTuple<?>> predicate = (IPredicate<RelationalTuple<?>>) node
				.jjtGetChild(0).jjtAccept(this, data);
		return new NotPredicate<RelationalTuple<?>>(predicate);
	}

	@Override
	public Object visit(ASTQuantificationPredicate node, Object data) {
		return new QuantificationPredicate(
				(AbstractQuantificationPredicate) node.jjtGetChild(0));
	}

	@Override
	public Object visit(ASTProbabilityPredicate probPred, Object data) {
		try {
			Class
					.forName("de.uniol.inf.is.odysseus.objecttracking.parser.CreateMVProjectionVisitor");
		} catch (Exception e) {
			throw new RuntimeException(
					"invalid use of probability predicates - missing plugin");
		}

		IVisitor v = VisitorFactory.getInstance().getVisitor(
				"ProbabilityPredicate");
		return (ILogicalOperator) v.visit(probPred, null, null);
	}

	@Override
	public Object visit(ASTSpatialPredicate node, Object data) {
		try {
			// IPredicateBuilder builder = (IPredicateBuilder) Class
			// .forName(
			// "de.uniol.inf.is.querytranslation.parser.transformation.SpatialPredicateBuilder")
			// .newInstance();
			// return builder.createPredicate(node, (IAttributeResolver) data);
			throw new RuntimeException(
					"ohne predicate builder machen, wenn das paket da ist das nehmen und als optionalen import markieren");
		} catch (Exception e) {
			throw new RuntimeException(
					"invalid use of spatial predicates - missing plugin");
		}
	}

	public static IPredicate<RelationalTuple<?>> toPredicate(
			ASTPredicate predicate, IAttributeResolver resolver) {
		return (IPredicate<RelationalTuple<?>>) new CreatePredicateVisitor()
				.visit(predicate, resolver);
	}

}
