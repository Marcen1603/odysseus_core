package de.uniol.inf.is.odysseus.parser.cql.parser.transformation;

import java.util.ArrayList;

import org.nfunk.jep.ParseException;

import de.uniol.inf.is.odysseus.base.predicate.AndPredicate;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.base.predicate.NotPredicate;
import de.uniol.inf.is.odysseus.base.predicate.OrPredicate;
import de.uniol.inf.is.odysseus.objecttracking.predicate.ProbabilityPredicate;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAndPredicate;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTBasicPredicate;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTIdentifier;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTMatrixExpression;
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
		SDFExpression expression;
		try {
			expression = new SDFExpression("", node.getPredicate(),
					(IAttributeResolver) data);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		return new RelationalPredicate(expression);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object visit(ASTAndPredicate node, Object data) {
		IPredicate<? super RelationalTuple<?>> left = (IPredicate<? super RelationalTuple<?>>) node
				.jjtGetChild(0).jjtAccept(this, data);
		IPredicate<? super RelationalTuple<?>> right = (IPredicate<? super RelationalTuple<?>>) node
				.jjtGetChild(1).jjtAccept(this, data);
		return new AndPredicate<RelationalTuple<?>>(left, right);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object visit(ASTOrPredicate node, Object data) {
		IPredicate<? super RelationalTuple<?>> left = (IPredicate<? super RelationalTuple<?>>) node
				.jjtGetChild(0).jjtAccept(this, data);
		IPredicate<? super RelationalTuple<?>> right = (IPredicate<? super RelationalTuple<?>>) node
				.jjtGetChild(1).jjtAccept(this, data);
		return new OrPredicate<RelationalTuple<?>>(left, right);
	}

	@SuppressWarnings("unchecked")
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
			if (probPred.isJoinPredicate()) {
				// the first child will be an matrix expression
				ASTMatrixExpression leftMatrix = (ASTMatrixExpression) probPred
						.jjtGetChild(0);
				ArrayList leftRows = leftMatrix.getMatrix();

				String leftSource = ((ASTIdentifier) probPred.jjtGetChild(1))
						.getName();

				ASTMatrixExpression leftVector = (ASTMatrixExpression) probPred
						.jjtGetChild(2);
				// it is a column vector n x 1
				ArrayList leftVRows = leftVector.getMatrix();

				double[][] leftM = new double[leftRows.size()][((ArrayList) leftRows
						.get(0)).size()];
				for (int i = 0; i < leftRows.size(); i++) {
					ArrayList row = (ArrayList) leftRows.get(i);
					for (int u = 0; u < row.size(); u++) {
						leftM[i][u] = (Double) row.get(u);
					}
				}

				double[] leftV = new double[leftVRows.size()];
				// i should only be 0 and then the loop should be
				// finished
				for (int i = 0; i < leftVRows.size(); i++) {
					leftV[i] = (Double) ((ArrayList) leftVRows.get(i)).get(0);
				}

				ASTMatrixExpression rightMatrix = (ASTMatrixExpression) probPred
						.jjtGetChild(3);
				ArrayList rightRows = rightMatrix.getMatrix();

				String rightSource = ((ASTIdentifier) probPred.jjtGetChild(4))
						.getName();

				ASTMatrixExpression rightVector = (ASTMatrixExpression) probPred
						.jjtGetChild(5);
				ArrayList rightVRows = rightVector.getMatrix();

				double[][] rightM = new double[rightRows.size()][((ArrayList) rightRows
						.get(0)).size()];
				for (int i = 0; i < rightRows.size(); i++) {
					ArrayList row = (ArrayList) rightRows.get(i);
					for (int u = 0; u < row.size(); u++) {
						leftM[i][u] = (Double) row.get(u);
					}
				}

				double[] rightV = new double[rightRows.size()];
				for (int i = 0; i < rightRows.size(); i++) {
					rightV[i] = (Double) ((ArrayList) rightRows.get(i)).get(0);
				}

				ArrayList xLow = probPred.getXLow();
				double[] xL = new double[xLow.size()];
				for (int i = 0; i < xLow.size(); i++) {
					xL[i] = (Double) xLow.get(i);
				}

				ArrayList xUp = probPred.getXUp();
				double[] xU = new double[xUp.size()];
				for (int i = 0; i < xUp.size(); i++) {
					xU[i] = (Double) xUp.get(i);
				}

				double prob = probPred.getProb();

				return new ProbabilityPredicate(leftSource, leftM, leftV,
						rightSource, rightM, rightV, xL, xU, prob, probPred
								.getCovOperandType(), probPred.getCompareOperator());
			} else {
				// the first child will be an matrix expression
				ASTMatrixExpression leftMatrix = (ASTMatrixExpression) probPred
						.jjtGetChild(0);
				ArrayList leftRows = leftMatrix.getMatrix();

				String leftSource = ((ASTIdentifier) probPred.jjtGetChild(1))
						.getName();

				ASTMatrixExpression leftVector = (ASTMatrixExpression) probPred
						.jjtGetChild(2);
				ArrayList leftVRows = leftVector.getMatrix();

				double[][] leftM = new double[leftRows.size()][((ArrayList) leftRows
						.get(0)).size()];
				for (int i = 0; i < leftRows.size(); i++) {
					ArrayList row = (ArrayList) leftRows.get(i);
					for (int u = 0; u < row.size(); u++) {
						leftM[i][u] = (Double) row.get(u);
					}
				}

				double[] leftV = new double[leftVRows.size()];
				// i should only be 0 and then the loop should be
				// finished
				for (int i = 0; i < leftVRows.size(); i++) {
					leftV[i] = (Double) ((ArrayList) leftVRows.get(i)).get(0);
				}

				ArrayList xLow = probPred.getXLow();
				double[] xL = new double[xLow.size()];
				for (int i = 0; i < xLow.size(); i++) {
					xL[i] = (Double) xLow.get(i);
				}

				ArrayList xUp = probPred.getXUp();
				double[] xU = new double[xUp.size()];
				for (int i = 0; i < xUp.size(); i++) {
					xU[i] = (Double) xUp.get(i);
				}

				double prob = probPred.getProb();

				return new ProbabilityPredicate(leftSource, leftM, leftV, xL, xU,
						prob, probPred.getCompareOperator());
			}
		} catch (Exception e) {
			throw new RuntimeException(
					"invalid use of probability predicates - missing plugin");
		}
	}

	@Override
	public Object visit(ASTSpatialPredicate node, Object data) {
		try {
//			IPredicateBuilder builder = (IPredicateBuilder) Class
//					.forName(
//							"de.uniol.inf.is.querytranslation.parser.transformation.SpatialPredicateBuilder")
//					.newInstance();
//			return builder.createPredicate(node, (IAttributeResolver) data);
			throw new RuntimeException("ohne predicate builder machen, wenn das paket da ist das nehmen und als optionalen import markieren");
		} catch (Exception e) {
			throw new RuntimeException(
					"invalid use of spatial predicates - missing plugin");
		}
	}
	
	@SuppressWarnings("unchecked")
	public static IPredicate<RelationalTuple<?>> toPredicate(ASTPredicate predicate, IAttributeResolver resolver) {
		return (IPredicate<RelationalTuple<?>>) new CreatePredicateVisitor()
				.visit(predicate, resolver);
	}

}
