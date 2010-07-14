package de.uniol.inf.is.odysseus.assoziation.physicaloperator;

import java.util.ArrayList;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;

import de.uniol.inf.is.odysseus.assoziation.logicaloperator.HypothesisEvaluationAO;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;

public class MahalanobisDistanceEvaluationPO<M extends IProbability & IPredictionFunctionKey<IPredicate<MVRelationalTuple<M>>> & IConnectionContainer<MVRelationalTuple<M>, MVRelationalTuple<M>, Double>> extends AbstractHypothesisEvaluationPO<M> {
	
	private double treshhold;
	private String operator;
	
	public double evaluate(MVRelationalTuple<M> tupleNew, MVRelationalTuple<M> tupleOld, ArrayList<int[]> mesurementValuePathsNew, ArrayList<int[]> mesurementValuePathsOld) {
		// TODO: Mahalanobis distance implementieren
		return 0;
	}
	
	public void setTreshhold(double treshhold) {
		this.treshhold = treshhold;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

}