package de.uniol.inf.is.odysseus.scars.objecttracking.association.physicaloperator;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;

public class NearestNeighborEvaluationPO<M extends IProbability & IConnectionContainer> extends
    AbstractHypothesisEvaluationPO<M> {

  // private weka.core.neighboursearch.LinearNNSearch nn;

  public void initAlgorithmParameter() {

  }

  @Override
  public AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> clone() {
    return new NearestNeighborEvaluationPO<M>(this);
  }

  public NearestNeighborEvaluationPO() {

  }

  public NearestNeighborEvaluationPO(NearestNeighborEvaluationPO<M> clone) {
    super(clone);
  }

  @Override
  public double evaluate(double[][] scannedObjCovariance, double[] scannedObjMesurementValues,
      double[][] predictedObjCovariance, double[] predictedObjMesurementValues) {
    return 0;
  }
}