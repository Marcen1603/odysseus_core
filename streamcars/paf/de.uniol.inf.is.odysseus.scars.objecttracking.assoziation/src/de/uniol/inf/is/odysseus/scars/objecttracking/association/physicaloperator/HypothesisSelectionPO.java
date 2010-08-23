package de.uniol.inf.is.odysseus.scars.objecttracking.association.physicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.ConnectionList;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.util.SchemaHelper;
import de.uniol.inf.is.odysseus.scars.util.SchemaIndexPath;
import de.uniol.inf.is.odysseus.scars.util.TupleIndexPath;
import de.uniol.inf.is.odysseus.scars.util.TupleInfo;

public class HypothesisSelectionPO<M extends IProbability & ITimeInterval & IConnectionContainer> extends
    AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {

  private String oldObjListPath;
  private String newObjListPath;
  private SchemaHelper schemaHelper;
  private SchemaIndexPath predictedObjectListPath;
  private SchemaIndexPath scannedObjectListPath;

  private boolean havingData = false;

  public HypothesisSelectionPO() {
    super();
  }

  public HypothesisSelectionPO(HypothesisSelectionPO<M> copy) {
    super(copy);

    this.oldObjListPath = copy.getOldObjListPath();
    this.newObjListPath = copy.getNewObjListPath();
  }

  // ----- SETTER AND GETTER -----

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
  
  private ConnectionList matchObjects(ConnectionList connectionList) {
    ConnectionList result = new ConnectionList();
    
    for (Connection connection : connectionList) {
      
    }
    
    return result;
  }
  
  private List<Object> getDifferenceSet(TupleIndexPath baseObjects, List<?> matchedObjects) {
    List<Object> result = new ArrayList<Object>();
    
    for (TupleInfo scannedTupleInfo : baseObjects) {
      if(!matchedObjects.contains(scannedTupleInfo.tupleObject)) {
        result.add(scannedTupleInfo.tupleObject);
      }
    }
    
    return result;
  }
  
  @Override
  protected void process_open() throws OpenFailedException {
    super.process_open();
    this.schemaHelper = new SchemaHelper(getOutputSchema());

    this.scannedObjectListPath = this.schemaHelper.getSchemaIndexPath(this.newObjListPath);
    this.predictedObjectListPath = this.schemaHelper.getSchemaIndexPath(this.oldObjListPath);
  }

  @Override
  public void processPunctuation(PointInTime timestamp, int port) {
    if (!havingData) {
      this.sendPunctuation(timestamp);
    } else {
      this.sendPunctuation(timestamp, 0);
      this.sendPunctuation(timestamp, 1);
    }
    this.havingData = false;
  }

  @Override
  protected void process_next(MVRelationalTuple<M> object, int port) {
    this.havingData = true;
    
    // PORT: 1, get matched objects
    ConnectionList matchedObjects = matchObjects(object.getMetadata().getConnectionList());
    object.getMetadata().setConnectionList(matchedObjects);
    transfer(object, 1);
    
    // PORT: 0, get new not matching objects
    List<Object> scannedNotMatchedObjects = getDifferenceSet(this.scannedObjectListPath.toTupleIndexPath(object), matchedObjects);
    MVRelationalTuple<M> scannedNotMatchedTuple = new MVRelationalTuple<M>(object);
    TupleIndexPath scannedObjectList = this.scannedObjectListPath.toTupleIndexPath(scannedNotMatchedTuple);
    scannedObjectList.setTupleObject(scannedNotMatchedObjects);
    transfer(scannedNotMatchedTuple, 0);
    
    // PORT: 2, get predicted not matching objects
    List<Object> predictedNotMatchedObjects = getDifferenceSet(this.predictedObjectListPath.toTupleIndexPath(object), matchedObjects);
    if (predictedNotMatchedObjects.size() > 0) {
      MVRelationalTuple<M> predictedNotMatchedTuple = new MVRelationalTuple<M>(object);
      TupleIndexPath predictedObjectList = this.predictedObjectListPath.toTupleIndexPath(predictedNotMatchedTuple);
      predictedObjectList.setTupleObject(predictedNotMatchedObjects);
      transfer(predictedNotMatchedTuple, 2);
    } else {
      this.sendPunctuation(new PointInTime(object.getMetadata().getStart()), 2);
    }
  }

  @Override
  public OutputMode getOutputMode() {
    return OutputMode.MODIFIED_INPUT;
  }

  @Override
  public AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> clone() {
    return new HypothesisSelectionPO<M>(this);
  }

  /**
   * Diese Funktion sorgt daf�r, dass nur noch eindeutige Zuordnungen vorhanden
   * sind.
   */
  public double[][] singleMatchingEvaluation(double[][] matchingMatrix) {
    double[][] singleMatchingMatrix = null;
    int index = -1;
    if (matchingMatrix.length > 0) {
      singleMatchingMatrix = new double[matchingMatrix.length][matchingMatrix[0].length];
    }

    for (int i = 0; i < matchingMatrix.length; i++) {
      index = getMaxRowIndex(matchingMatrix[i]);
      if (index != -1) {
        singleMatchingMatrix[i][index] = matchingMatrix[i][index];
      }
      index = -1;
    }

    double maxValue = -1;
    int maxValueIndex = -1;
    for (int i = 0; i < singleMatchingMatrix[0].length; i++) {
      for (int j = 0; j < singleMatchingMatrix.length; j++) {
        if (singleMatchingMatrix[j][i] != 0.0d && maxValue < singleMatchingMatrix[j][i]) {
          // Wenn der Wert gr��er als der vorher errechnete Wert ist
          // wird dieser als neuer Wert �bernommen
          maxValue = matchingMatrix[j][i];
          maxValueIndex = j;
          singleMatchingMatrix[j][i] = 0;
        }
        if (maxValue >= singleMatchingMatrix[j][i]) {
          // Falls der Wert kleiner oder gleich dem vorher errechneten
          // Wert ist wird dieser ignoriert
          singleMatchingMatrix[j][i] = 0;
        }
      }
      if (maxValue != -1) {
        singleMatchingMatrix[maxValueIndex][i] = maxValue;
      }
      maxValue = -1;
      maxValueIndex = -1;
    }

    return singleMatchingMatrix;
  }

  /**
   * 
   * @param row
   *          aktuelle Zeiler der Matrix
   * @return Index des maximalen Wertes der Zeile
   */
  private int getMaxRowIndex(double[] row) {
    int index = -1;
    double maxValue = 0.0d;
    for (int i = 0; i < row.length; i++) {
      if (maxValue < row[i]) {
        maxValue = row[i];
        index = i;
      }
    }
    return index;
  }
}
