package de.uniol.inf.is.odysseus.scars.objecttracking.prediction.physicaloperator;

import java.util.ArrayList;
import java.util.Queue;

import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.scars.objecttracking.prediction.sdf.metadata.IPredictionFunction;
import de.uniol.inf.is.odysseus.scars.objecttracking.prediction.sdf.metadata.PredictionFunctionContainer;
import de.uniol.inf.is.odysseus.scars.util.SchemaHelper;
import de.uniol.inf.is.odysseus.scars.util.SchemaIndexPath;
import de.uniol.inf.is.odysseus.scars.util.TupleHelper;
import de.uniol.inf.is.odysseus.scars.util.TupleIndexPath;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class PredictionPO<M extends IProbability & ITimeInterval & IPredictionFunctionKey<IPredicate<MVRelationalTuple<M>>>>
    extends AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {

  private MVRelationalTuple<M> currentTimeTuple;
  private MVRelationalTuple<M> currentScanTuple;

  private int[] objListPath;
  private SchemaIndexPath currentTimeSchemaPath;
  private SchemaIndexPath currentScanTimeSchemaPath;

  private PredictionFunctionContainer<M> predictionFunctions;
  
  private ArrayList<PointInTime> punctuations = new ArrayList<PointInTime>();

  public PredictionPO() {
  }

  public PredictionPO(PredictionPO<M> copy) {
    super(copy);
    this.predictionFunctions = new PredictionFunctionContainer<M>(copy.predictionFunctions);
    objListPath = new int[copy.objListPath.length];
    System.arraycopy(copy.objListPath, 0, this.objListPath, 0, copy.objListPath.length);
    this.currentTimeSchemaPath = copy.currentTimeSchemaPath;
    this.currentScanTimeSchemaPath = copy.currentScanTimeSchemaPath;
  }

  public void setPredictionFunctions(PredictionFunctionContainer<M> predictionFunctions) {
    this.predictionFunctions = predictionFunctions;
  }

  public void setObjectListPath(int[] objListPath) {
    this.objListPath = objListPath;
  }

  @Override
  public OutputMode getOutputMode() {
    return OutputMode.NEW_ELEMENT;
  }

  @Override
  protected void process_open() throws OpenFailedException {
    super.process_open();
    SDFAttributeList sourceTimeSchema = this.getSubscribedToSource(0).getTarget().getOutputSchema();
    SDFAttributeList scanSchema = this.getSubscribedToSource(1).getTarget().getOutputSchema();
    SchemaHelper helper1 = new SchemaHelper(sourceTimeSchema);
    currentTimeSchemaPath = helper1.getSchemaIndexPath(helper1.getStartTimestampFullAttributeName());
    SchemaHelper helper2 = new SchemaHelper(scanSchema);
    currentScanTimeSchemaPath = helper2.getSchemaIndexPath(helper2.getStartTimestampFullAttributeName());
  }

  @Override
  protected void process_next(MVRelationalTuple<M> object, int port) {
    synchronized (this) {
    	Long tupleTime = (Long)currentScanTimeSchemaPath.toTupleIndexPath(object).getTupleObject();
      if (port == 0) {
        currentTimeTuple = object.clone();
        printOutput("Timetuple on port 0: " + tupleTime);
      } else if (port == 1) {
        currentScanTuple = object.clone();
        printOutput("Datatuple on port 1: " + tupleTime);
     }

      if (currentTimeTuple != null && currentScanTuple != null) {
        predictData();
        MVRelationalTuple<M> tmp = currentScanTuple;
        currentScanTuple = null;
        currentTimeTuple = null;
        
        Long time = (Long)currentScanTimeSchemaPath.toTupleIndexPath(tmp).getTupleObject();
        
        // Send old punctuations
        while( !punctuations.isEmpty() && punctuations.get(0).before(new PointInTime(time))) {
        	printOutput("Send Punctuation out of Prediction : " + punctuations.get(0));
        	sendPunctuation(punctuations.get(0));
        	punctuations.remove(0);
        	printPunctuationList();
        }
        
        printOutput("Send Data of timestamp " + time);
        transfer(tmp);
        
      }
    }
  }

  // @SuppressWarnings("unchecked")
  private MVRelationalTuple<M> predictData() {

    if (this.currentScanTuple != null) {
      TupleHelper helper = new TupleHelper(this.currentScanTuple);
      MVRelationalTuple<?> list = (MVRelationalTuple<?>) helper.getObject(objListPath);
      for (int index = 0; index < list.getAttributeCount(); index++) {
        MVRelationalTuple<M> obj = list.getAttribute(index);
        IPredictionFunction<M> pf = predictionFunctions.get(obj.getMetadata().getPredictionFunctionKey());
        if (pf != null) {
          pf.predictData(this.currentScanTuple, this.currentTimeTuple, index);
          M metadata = obj.getMetadata();
          pf.predictMetadata(metadata, this.currentScanTuple, this.currentTimeTuple, index);
        } else {
          System.err.println("No PredictionFunction assigned (NO DEFAULT PREDICTION_FUNCTION)");
        }

      }
      TupleIndexPath scanTimeTPath = currentTimeSchemaPath.toTupleIndexPath(this.currentTimeTuple);
      Long currentTimeValue = (Long) scanTimeTPath.getTupleObject();

      this.currentScanTuple.getMetadata().getStart().setMainPoint(currentTimeValue);
      TupleIndexPath currentScanTimeTPath = currentScanTimeSchemaPath.toTupleIndexPath(this.currentScanTuple);
      currentScanTimeTPath.setTupleObject(currentTimeValue);
    } else {
      System.err.println("Prediction: currentScanTuple was null!!");
    }
    return this.currentScanTuple;
  }

  @Override
  public void processPunctuation(PointInTime timestamp, int port) {
	  if( port == 0 ) {
		  printOutput("Process Punctuation of time " + timestamp + " on port 0");
		  
		  if( !punctuations.contains(timestamp)) {
			  
			  boolean found = false;
			  for( int i = 0; i < punctuations.size(); i++ ) {
				  if( punctuations.get(i).after(timestamp) ) {
					  punctuations.add(i, timestamp);
					  found = true;
					  break;
				  }
			  }
			  if( !found )
				  punctuations.add(timestamp);
			  
			  printPunctuationList();
	
		  }
	  }
  }
  
  private void printPunctuationList() {
	  StringBuilder sb = new StringBuilder();
	  sb.append("PunctuationList:");
	  if( punctuations.isEmpty())
		  sb.append("[Empty]");
	  else
		  for( int i = 0; i < punctuations.size(); i++ ) {
			  sb.append(punctuations.get(i)).append(", ");
		  }
	  printOutput(sb.toString());
  }
  
  private void printOutput( String txt ) {
	  System.out.println("PREDICTION(" + hashCode() + "):" + txt);
  }

  @Override
  public PredictionPO<M> clone() {
    return new PredictionPO<M>(this);
  }
}
