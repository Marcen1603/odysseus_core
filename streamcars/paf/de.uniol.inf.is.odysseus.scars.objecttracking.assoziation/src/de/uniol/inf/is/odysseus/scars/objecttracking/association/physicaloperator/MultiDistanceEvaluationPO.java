package de.uniol.inf.is.odysseus.scars.objecttracking.association.physicaloperator;

import weka.core.Attribute;
import weka.core.ChebyshevDistance;
import weka.core.DenseInstance;
import weka.core.DistanceFunction;
import weka.core.EuclideanDistance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.ManhattanDistance;
import weka.core.MinkowskiDistance;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;

@SuppressWarnings("deprecation")
public class MultiDistanceEvaluationPO<M extends IProbability & IConnectionContainer> extends
    AbstractHypothesisEvaluationPO<M> {

  private static final String DISTANCE_ID = "distanceFunction";

  // Available Functions: ChebyshevDistance, EuclideanDistance,
  // ManhattanDistance, MinkowskiDistance
  private static final String CHEBYSHEV = "CHEBYSHEV";
  private static final String EUCLIDEAN = "EUCLIDEAN";
  private static final String MANHATTEN = "MANHATTEN";
  private static final String MINKOWSKI = "MINKOWSKI";

  private DistanceFunction df;

  public MultiDistanceEvaluationPO() {
	  super();
  }

  public MultiDistanceEvaluationPO(MultiDistanceEvaluationPO<M> clone) {
    super(clone);
    this.df = clone.df;
  }

  public void initAlgorithmParameter() {
	// try - catch so that the operator does not crash if there is no parameter given
	try {
	    if (this.getAlgorithmParameter().containsKey(DISTANCE_ID)) {
	      String algoName = this.getAlgorithmParameter().get(DISTANCE_ID);
	      if (algoName.equals(CHEBYSHEV)) {
	        this.df = new ChebyshevDistance();
	      } else if (algoName.equals(EUCLIDEAN)) {
	        this.df = new EuclideanDistance();
	      } else if (algoName.equals(MANHATTEN)) {
	        this.df = new ManhattanDistance();
	      } else if (algoName.equals(MINKOWSKI)) {
	        this.df = new MinkowskiDistance();
	      } else {
	        this.df = new EuclideanDistance();
	        System.out
	            .println("### MultiDistanceEvaluationPO: ### No valid Distancefunction ### Default function was selected (EuclideanDistance) ##");
	      }
	    }
	} catch(Exception e) {
		this.df = new EuclideanDistance();
        System.out
            .println("### MultiDistanceEvaluationPO: ### Can not access distance parameter ### Default function was selected (EuclideanDistance) ##");
	}
  }

  @SuppressWarnings({ "unchecked", "deprecation", "rawtypes" })
  @Override
  public double evaluate(double[][] scannedObjCovariance, double[] scannedObjMesurementValues,
      double[][] predictedObjCovariance, double[] predictedObjMesurementValues, double currentRating) {

	FastVector atts = new FastVector();

    // Create instances
//    Instance scannedObjInstance = new DenseInstance(scannedObjMesurementValues.length);
//    Instance predictedObjInstance = new DenseInstance(predictedObjMesurementValues.length);

    // Set values
//    for (int i = 0; i < scannedObjMesurementValues.length; i++) {
//      scannedObjInstance.setValue(i, scannedObjMesurementValues[i]);
//    }
//    for (int i = 0; i < predictedObjMesurementValues.length; i++) {
//      predictedObjInstance.setValue(i, predictedObjMesurementValues[i]);
//    }

    for (int i = 0; i < scannedObjMesurementValues.length; i++) {
    	atts.addElement(new Attribute(String.valueOf(i)));
    }

    Instances data = new Instances("Instances", atts, 0);

    // 1st instance

    double[] vals = new double[data.numAttributes()];

    for (int i = 0; i < scannedObjMesurementValues.length; i++) {
    	vals[i] = scannedObjMesurementValues[i];
    }

    Instance scannedObjInstance = new DenseInstance(scannedObjMesurementValues.length, vals);

    data.add(scannedObjInstance);

    // 2nd instance

    vals = new double[data.numAttributes()];

    for (int i = 0; i < predictedObjMesurementValues.length; i++) {
    	vals[i] = predictedObjMesurementValues[i];
    }

    Instance predictedObjInstance = new DenseInstance(predictedObjMesurementValues.length, vals);

    data.add(predictedObjInstance);

    df.setInstances(data);

    // VJ:
    // What about the covariance matrix? How to combine the weka distance function with the cov.?

    // Return Distance
    // Available Functions: ChebyshevDistance, EuclideanDistance,
    // ManhattanDistance, MinkowskiDistance, NormalizableDistance
    return currentRating + this.df.distance(scannedObjInstance, predictedObjInstance)*(-1);
  }

  @Override
  public AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> clone() {
    return new MultiDistanceEvaluationPO<M>(this);
  }
}