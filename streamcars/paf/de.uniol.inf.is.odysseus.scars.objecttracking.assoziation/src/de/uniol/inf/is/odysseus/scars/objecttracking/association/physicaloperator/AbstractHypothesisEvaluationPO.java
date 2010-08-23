package de.uniol.inf.is.odysseus.scars.objecttracking.association.physicaloperator;

import java.util.ArrayList;
import java.util.HashMap;

import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.scars.objecttracking.association.CorrelationMatrixUtils;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.ConnectionList;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.util.SchemaHelper;
import de.uniol.inf.is.odysseus.scars.util.SchemaIndexPath;
import de.uniol.inf.is.odysseus.scars.util.TupleIndexPath;
import de.uniol.inf.is.odysseus.scars.util.TupleInfo;
import de.uniol.inf.is.odysseus.scars.util.TupleIterator;
import de.uniol.inf.is.odysseus.scars.util.UtilPrinter;
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

  @SuppressWarnings("unchecked")
  private ArrayList<MVRelationalTuple<M>> getObjectList(MVRelationalTuple<M> mvRelationalTuple) {
    ArrayList<MVRelationalTuple<M>> objects = new ArrayList<MVRelationalTuple<M>>();
    for (Object attribute : mvRelationalTuple.getAttributes()) {
      if (attribute instanceof MVRelationalTuple<?>) {
        objects.add((MVRelationalTuple<M>) attribute);
      }
    }
    return objects;
  }

  private int countChildTuple(MVRelationalTuple<M> mvRelationalTuple) {
    int result = 0;
    for (Object attribute : mvRelationalTuple.getAttributes()) {
      if (attribute instanceof MVRelationalTuple<?>) {
        result++;
      }
    }
    return result;
  }

  private double[] getMeasurementValues(MVRelationalTuple<M> tuple, TupleIndexPath tupleIndexPath) {
    ArrayList<Double> values = new ArrayList<Double>();

    for (TupleInfo info : new TupleIterator(tuple, tupleIndexPath)) {
      if (SDFDatatypes.isMeasurementValue(info.attribute.getDatatype())) {
        System.out.println("MV: " + info.attribute.toString() + " - " + info.tupleObject.toString());
        values.add(new Double(info.tupleObject.toString()));
      } else {
        System.out.println(info.attribute.toString() + " - " + info.tupleObject.toString());
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

  public abstract double evaluate(double[][] scannedObjCovariance, double[] scannedObjMesurementValues,
      double[][] predictedObjCovariance, double[] predictedObjMesurementValues);

  @Override
  protected void process_open() throws OpenFailedException {
    super.process_open();
    this.schemaHelper = new SchemaHelper(getOutputSchema());

    this.scannedObjectListPath = this.schemaHelper.getSchemaIndexPath(this.newObjListPath);
    this.predictedObjectListPath = this.schemaHelper.getSchemaIndexPath(this.oldObjListPath);
  }

  @SuppressWarnings("unchecked")
  @Override
  protected void process_next(MVRelationalTuple<M> object, int port) {

    TupleIndexPath scannedTupleIndexPath = this.scannedObjectListPath.toTupleIndexPath(object);
    TupleIndexPath predictedTupleIndexPath = this.predictedObjectListPath.toTupleIndexPath(object);

    double[][] corMatrix = new double[countChildTuple((MVRelationalTuple<M>) scannedTupleIndexPath.getTupleObject())][countChildTuple((MVRelationalTuple<M>) predictedTupleIndexPath
        .getTupleObject())];

    int i = 0;
    int j = 0;
    for (TupleInfo scannedTupleInfo : scannedTupleIndexPath) {
      MVRelationalTuple<M> scannedObject = (MVRelationalTuple<M>) scannedTupleInfo.tupleObject;

      System.out.println("###################################################");
      System.out.println(i + ": " + scannedTupleInfo.tupleIndexPath);
      System.out.println(scannedObject.toString());
      UtilPrinter.printInfo(getMeasurementValues(object, scannedTupleInfo.tupleIndexPath));

      for (TupleInfo predictedTupleInfo : predictedTupleIndexPath) {
        MVRelationalTuple<M> predictedObject = (MVRelationalTuple<M>) predictedTupleInfo.tupleObject;

        System.out.println("----------------------------------------------------");

        System.out.println(j + ": " + predictedTupleInfo.tupleIndexPath);
        System.out.println(predictedObject.toString());
        UtilPrinter.printInfo(getMeasurementValues(object, predictedTupleInfo.tupleIndexPath));

        corMatrix[i][j++] = evaluate(scannedObject.getMetadata().getCovariance(),
            getMeasurementValues(object, scannedTupleInfo.tupleIndexPath), predictedObject.getMetadata()
                .getCovariance(), getMeasurementValues(object, predictedTupleInfo.tupleIndexPath));
      }
      j = 0;
      i++;
    }

    // // 1 - Get the needed data out of the MVRelationalTuple object
    // // 1.1 - Get the list of new objects as an array of MVRelationalTuple
    // ArrayList<MVRelationalTuple<M>> newList =
    // getObjectList((MVRelationalTuple<M>) scannedTupleIndexPath
    // .getTupleObject());
    //
    // // 1.2 - Get the list of old objects (which are predicted to the
    // // timestamp of the new objects) as an array of MVRelationalTuple
    // ArrayList<MVRelationalTuple<M>> oldList =
    // getObjectList((MVRelationalTuple<M>) predictedTupleIndexPath
    // .getTupleObject());
    //
    // // 1.3 - Get the list of connections between old and new objects as an
    // // array of Connection
    // Connection[] objConList =
    // object.getMetadata().getConnectionList().toArray(new Connection[0]);
    //
    // // 2 - Convert the connection list to an matrix of ratings so that even
    // // connections which are NOT in the connections list (so they have a
    // // rating of 0) can be evaluated
    // CorrelationMatrixUtils<M> corUtils = new CorrelationMatrixUtils<M>();
    // corMatrix = corUtils.encodeMatrix(newList.toArray(new
    // MVRelationalTuple[0]),
    // oldList.toArray(new MVRelationalTuple[0]), objConList);
    //
    // // 3 - Evaluate each connection in the matrix
    // //corMatrix = this.evaluateAll(object, corMatrix, newList, oldList);
    //
    // // 4 - Generate a new connection list out of the matrix. only
    // // connections with rating > 0 will be stored so that the connection
    // // list is as small as possible
    // ConnectionList newObjConList = corUtils.decodeMatrix(newList.toArray(new
    // MVRelationalTuple[0]),
    // oldList.toArray(new MVRelationalTuple[0]), corMatrix);
    //
    // // 5 - Replace the old connection list in the metadata with the new
    // // connection list
    // object.getMetadata().setConnectionList(newObjConList);

    // 6 - ready -> transfer to next operator
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
}
