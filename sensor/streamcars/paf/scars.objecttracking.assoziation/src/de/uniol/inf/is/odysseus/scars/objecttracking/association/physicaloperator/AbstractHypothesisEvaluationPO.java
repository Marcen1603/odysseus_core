package de.uniol.inf.is.odysseus.scars.objecttracking.association.physicaloperator;

import java.util.ArrayList;
import java.util.HashMap;

import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.ConnectionList;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.util.SchemaHelper;
import de.uniol.inf.is.odysseus.scars.util.SchemaIndexPath;
import de.uniol.inf.is.odysseus.scars.util.TupleIndexPath;
import de.uniol.inf.is.odysseus.scars.util.TupleInfo;
import de.uniol.inf.is.odysseus.scars.util.TupleIterator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFDatatypes;

/**
 * Physical Operator for the rating of connections within the association
 * process.
 *
 * new = left; old = right
 *
 * @author Volker Janz
 *
 * @param <M>
 */
public abstract class AbstractHypothesisEvaluationPO<M extends IProbability & IConnectionContainer> extends
    AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {


  // if debugMode is true the connections will be send to the console
  private boolean debugMode = false;

  private String oldObjListPath;
  private String newObjListPath;
  private HashMap<String, String> algorithmParameter;
  private HashMap<String, String> measurementPairs;
  private SchemaHelper schemaHelper;
  private SchemaIndexPath predictedObjectListPath;
  private SchemaIndexPath scannedObjectListPath;

  public AbstractHypothesisEvaluationPO() {
    super();
  }

  @SuppressWarnings("unchecked")
  public AbstractHypothesisEvaluationPO(AbstractHypothesisEvaluationPO<M> clone) {
    super(clone);

    this.oldObjListPath = clone.getOldObjListPath();
    this.newObjListPath = clone.getNewObjListPath();
    this.algorithmParameter = (HashMap<String, String>) clone.getAlgorithmParameter().clone();
  }

  public void setOldObjListPath(String oldObjListPath) {
    this.oldObjListPath = oldObjListPath;
  }

  public void setNewObjListPath(String newObjListPath) {
    this.newObjListPath = newObjListPath;
  }

  public String getOldObjListPath() {
    return this.oldObjListPath;
  }

  public String getNewObjListPath() {
    return this.newObjListPath;
  }

  public void setAlgorithmParameter(HashMap<String, String> newAlgoParameter) {
    this.algorithmParameter = newAlgoParameter;
    this.initAlgorithmParameter();
  }

  public HashMap<String, String> getAlgorithmParameter() {
    return this.algorithmParameter;
  }

  public void setMeasurementPairs(HashMap<String, String> measurementPairs) {
    this.measurementPairs = measurementPairs;
  }

  public HashMap<String, String> getMeasurementPairs() {
    return measurementPairs;
  }

  protected double[] getMeasurementValues(MVRelationalTuple<M> tuple, TupleIndexPath tupleIndexPath) {
    ArrayList<Double> values = new ArrayList<Double>();

    // TODO:
    // Kann noch optimiert werden, da jedesmal das Schema durchlaufen wird
    for (TupleInfo info : new TupleIterator(tuple, tupleIndexPath)) {
      if (SDFDatatypes.isMeasurementValue(info.attribute.getDatatype())) {
        values.add(new Double(info.tupleObject.toString()));
      }
    }

    double[] result = new double[values.size()];
    for (int i = 0; i < values.size(); i++) {
      result[i] = values.get(i);
    }

    return result;
  }

  /**
   * Inits the algorithm specific parameter. The parameter are stored in the
   * HashMap algorithmParameter.
   */
  public abstract void initAlgorithmParameter();

  // TODO (Wolf):
  // Das muss noch gekapselt werden. Zudem sollte ein Wert, der ggf. noch von alten Berechnungen vorhanden ist übergeben werden.
  // Volker -> Alte Bewertung wird jetzt �bergeben und kann verwendet werden
  public abstract double evaluate(double[][] scannedObjCovariance, double[] scannedObjMesurementValues,
      double[][] predictedObjCovariance, double[] predictedObjMesurementValues, double currentConnectionRating);

  @Override
  protected void process_open() throws OpenFailedException {
    super.process_open();
    this.schemaHelper = new SchemaHelper(getOutputSchema());

    this.setScannedObjectListPath(this.schemaHelper.getSchemaIndexPath(this.newObjListPath));
    this.setPredictedObjectListPath(this.schemaHelper.getSchemaIndexPath(this.oldObjListPath));
  }

  @SuppressWarnings("unchecked")
  @Override
  protected void process_next(MVRelationalTuple<M> object, int port) {
    TupleIndexPath scannedTupleIndexPath = this.getScannedObjectListPath().toTupleIndexPath(object);
    TupleIndexPath predictedTupleIndexPath = this.getPredictedObjectListPath().toTupleIndexPath(object);

    ConnectionList newObjConList = new ConnectionList();

    // TODO (Wolf):
    // Berechnungen vorheriger EvalOps berücksichtigen, d. h. alte connections ermitteln.
    // Volker:
    // Erledigt -> evaluate wird alter connection wert �bergeben

    for (TupleInfo scannedTupleInfo : scannedTupleIndexPath) {
      MVRelationalTuple<M> scannedObject = (MVRelationalTuple<M>) scannedTupleInfo.tupleObject;

      for (TupleInfo predictedTupleInfo : predictedTupleIndexPath) {
        MVRelationalTuple<M> predictedObject = (MVRelationalTuple<M>) predictedTupleInfo.tupleObject;

        double value = evaluate(scannedObject.getMetadata().getCovariance(),
            getMeasurementValues(object, scannedTupleInfo.tupleIndexPath), predictedObject.getMetadata()
                .getCovariance(), getMeasurementValues(object, predictedTupleInfo.tupleIndexPath), object.getMetadata().getConnectionList().getRatingForElementPair(scannedTupleInfo.tupleIndexPath.toArray(), predictedTupleInfo.tupleIndexPath.toArray()));

        if(value > 0) {
          newObjConList.add(new Connection(scannedTupleInfo.tupleIndexPath.toArray(), predictedTupleInfo.tupleIndexPath.toArray(), value));
        }
      }
    }

    object.getMetadata().setConnectionList(newObjConList);

    if(debugMode) {
    	System.out.println("######################### CONNECTIONS: #########################");
    	for(Connection con : object.getMetadata().getConnectionList()) {
    		System.out.println("CON: [" + con.getLeftPath() + "] <-> [" + con.getRightPath() + "] RATING: [" + con.getRating() + "]");
    	}
    }

    transfer(object);
  }

  @Override
  public void processPunctuation(PointInTime timestamp, int port) {
    this.sendPunctuation(timestamp);
  }

  @Override
  public OutputMode getOutputMode() {
    return OutputMode.MODIFIED_INPUT;
  }

public void setScannedObjectListPath(SchemaIndexPath scannedObjectListPath) {
	this.scannedObjectListPath = scannedObjectListPath;
}

public SchemaIndexPath getScannedObjectListPath() {
	return scannedObjectListPath;
}

public void setPredictedObjectListPath(SchemaIndexPath predictedObjectListPath) {
	this.predictedObjectListPath = predictedObjectListPath;
}

public SchemaIndexPath getPredictedObjectListPath() {
	return predictedObjectListPath;
}
}
