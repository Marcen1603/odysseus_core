//package de.uniol.inf.is.odysseus.parser.cql.parser.transformation;
//
//import java.util.ArrayList;
//
//import de.uniol.inf.is.odysseus.predicate.IPredicate;
//import de.uniol.inf.is.odysseus.parser.cql.parser.ASTIdentifier;
//import de.uniol.inf.is.odysseus.parser.cql.parser.ASTMatrixExpression;
//import de.uniol.inf.is.odysseus.parser.cql.parser.ASTProbabilityPredicate;
//import de.uniol.inf.is.odysseus.parser.cql.parser.AbstractPredicate;
//import de.uniol.inf.is.odysseus.parser.cql.parser.transformation.IPredicateBuilder;
//import de.uniol.inf.is.odysseus.probability.predicate.ProbabilityPredicate;
//import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
//import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.IAttributeResolver;
//
//public class ProbabilityPredicateBuilder implements IPredicateBuilder {
//
//	@Override
//	public IPredicate<RelationalTuple<?>> createPredicate(
//			AbstractPredicate predicate, IAttributeResolver resolver) {
//		ASTProbabilityPredicate probPred = (ASTProbabilityPredicate) predicate;
//
//		if (probPred.isJoinPredicate()) {
//			// the first child will be an matrix expression
//			ASTMatrixExpression leftMatrix = (ASTMatrixExpression) probPred
//					.jjtGetChild(0);
//			ArrayList leftRows = leftMatrix.getMatrix();
//
//			String leftSource = ((ASTIdentifier) probPred.jjtGetChild(1))
//					.getName();
//
//			ASTMatrixExpression leftVector = (ASTMatrixExpression) probPred
//					.jjtGetChild(2);
//			// it is a column vector n x 1
//			ArrayList leftVRows = leftVector.getMatrix();
//
//			double[][] leftM = new double[leftRows.size()][((ArrayList) leftRows
//					.get(0)).size()];
//			for (int i = 0; i < leftRows.size(); i++) {
//				ArrayList row = (ArrayList) leftRows.get(i);
//				for (int u = 0; u < row.size(); u++) {
//					leftM[i][u] = (Double) row.get(u);
//				}
//			}
//
//			double[] leftV = new double[leftVRows.size()];
//			// i should only be 0 and then the loop should be
//			// finished
//			for (int i = 0; i < leftVRows.size(); i++) {
//				leftV[i] = (Double) ((ArrayList) leftVRows.get(i)).get(0);
//			}
//
//			ASTMatrixExpression rightMatrix = (ASTMatrixExpression) probPred
//					.jjtGetChild(3);
//			ArrayList rightRows = rightMatrix.getMatrix();
//
//			String rightSource = ((ASTIdentifier) probPred.jjtGetChild(4))
//					.getName();
//
//			ASTMatrixExpression rightVector = (ASTMatrixExpression) probPred
//					.jjtGetChild(5);
//			ArrayList rightVRows = rightVector.getMatrix();
//
//			double[][] rightM = new double[rightRows.size()][((ArrayList) rightRows
//					.get(0)).size()];
//			for (int i = 0; i < rightRows.size(); i++) {
//				ArrayList row = (ArrayList) rightRows.get(i);
//				for (int u = 0; u < row.size(); u++) {
//					leftM[i][u] = (Double) row.get(u);
//				}
//			}
//
//			double[] rightV = new double[rightRows.size()];
//			for (int i = 0; i < rightRows.size(); i++) {
//				rightV[i] = (Double) ((ArrayList) rightRows.get(i)).get(0);
//			}
//
//			ArrayList xLow = probPred.getXLow();
//			double[] xL = new double[xLow.size()];
//			for (int i = 0; i < xLow.size(); i++) {
//				xL[i] = (Double) xLow.get(i);
//			}
//
//			ArrayList xUp = probPred.getXUp();
//			double[] xU = new double[xUp.size()];
//			for (int i = 0; i < xUp.size(); i++) {
//				xU[i] = (Double) xUp.get(i);
//			}
//
//			double prob = probPred.getProb();
//
//			return new ProbabilityPredicate(leftSource, leftM, leftV,
//					rightSource, rightM, rightV, xL, xU, prob, probPred
//							.getCovOperandType(), probPred.getCompareOperator());
//		} else {
//			// the first child will be an matrix expression
//			ASTMatrixExpression leftMatrix = (ASTMatrixExpression) probPred
//					.jjtGetChild(0);
//			ArrayList leftRows = leftMatrix.getMatrix();
//
//			String leftSource = ((ASTIdentifier) probPred.jjtGetChild(1))
//					.getName();
//
//			ASTMatrixExpression leftVector = (ASTMatrixExpression) probPred
//					.jjtGetChild(2);
//			ArrayList leftVRows = leftVector.getMatrix();
//
//			double[][] leftM = new double[leftRows.size()][((ArrayList) leftRows
//					.get(0)).size()];
//			for (int i = 0; i < leftRows.size(); i++) {
//				ArrayList row = (ArrayList) leftRows.get(i);
//				for (int u = 0; u < row.size(); u++) {
//					leftM[i][u] = (Double) row.get(u);
//				}
//			}
//
//			double[] leftV = new double[leftVRows.size()];
//			// i should only be 0 and then the loop should be
//			// finished
//			for (int i = 0; i < leftVRows.size(); i++) {
//				leftV[i] = (Double) ((ArrayList) leftVRows.get(i)).get(0);
//			}
//
//			ArrayList xLow = probPred.getXLow();
//			double[] xL = new double[xLow.size()];
//			for (int i = 0; i < xLow.size(); i++) {
//				xL[i] = (Double) xLow.get(i);
//			}
//
//			ArrayList xUp = probPred.getXUp();
//			double[] xU = new double[xUp.size()];
//			for (int i = 0; i < xUp.size(); i++) {
//				xU[i] = (Double) xUp.get(i);
//			}
//
//			double prob = probPred.getProb();
//
//			return new ProbabilityPredicate(leftSource, leftM, leftV, xL, xU,
//					prob, probPred.getCompareOperator());
//		}
//	}
//
//}
