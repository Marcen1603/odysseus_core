package de.uniol.inf.is.odysseus.scars.objecttracking.association.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.BinaryLogicalOp;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.objecttracking.sdf.SDFAttributeListExtended;
import de.uniol.inf.is.odysseus.scars.util.SchemaHelper;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

/**
 * The Hypothesis Generation has two inputstreams: 1 - predicted objects from
 * the prediction operator 2 - the new detected objects Both list now have the
 * same timestamp. The Hypothesis Generation Operator initiates the connection
 * list in the metadata and changes the schema so that the next operator gets
 * both lists (new and old) as input.
 * 
 * @author Volker Janz
 * 
 */
public class HypothesisGenerationAO<M extends IProbability> extends BinaryLogicalOp {

  private static final long serialVersionUID = 1L;

  private static final String ASSOCIATION_SOURCE_NAME = "association";
  private static final String ASSOCIATION_RECORD_NAME = "scan";
  private static final String SCANNED_OBJECTS_NAME = "scannedObjects";
  private static final String PREDICTED_OBJECTS_NAME = "predictedObjects";

  private String oldObjListPath;
  private String newObjListPath;

  public HypothesisGenerationAO() {
    super();
  }

  public HypothesisGenerationAO(HypothesisGenerationAO<M> copy) {
    super(copy);
    this.oldObjListPath = copy.oldObjListPath;
    this.newObjListPath = copy.newObjListPath;
  }

  // LEFT -> SOURCE (neu erkannte objekte)
  // RIGHT -> PREDICTION (alte, pr�dizierte objekte)

  // Schema von PREDICTION wird erweitert um Schema der Liste von SOURCE
  // (analog zur �nderung des Tupels im PO)

  @Override
  public SDFAttributeList getOutputSchema() {
    SchemaHelper helper = null;

    // copy scanned Objects
    helper = new SchemaHelper(((SDFAttributeListExtended) this.getSubscribedToSource(LEFT).getSchema().clone()));
    SDFAttribute scannedObjects = helper.getAttribute(this.newObjListPath).clone();
    // set new list name
    scannedObjects.setAttributeName(SCANNED_OBJECTS_NAME);

    // get timestamp from scanned data
    String timeStampName = helper.getStartTimestampFullAttributeName();
    SDFAttribute timestamp = helper.getAttribute(timeStampName);

    // copy scanned Objects
    helper = new SchemaHelper(((SDFAttributeListExtended) this.getSubscribedToSource(RIGHT).getSchema().clone()));
    SDFAttribute predictedObjects = helper.getAttribute(this.oldObjListPath).clone();
    // set new list name
    predictedObjects.setAttributeName(PREDICTED_OBJECTS_NAME);

    // create new record
    SDFAttribute association = new SDFAttribute(ASSOCIATION_RECORD_NAME);
    association.setDatatype(SDFDatatypeFactory.getDatatype("Record"));
    // add timestamp
    association.addSubattribute(timestamp);
    // add scanned objects to record
    association.addSubattribute(scannedObjects);
    // add predicted objects to record
    association.addSubattribute(predictedObjects);

    // set source name
    setSourceName(association, ASSOCIATION_SOURCE_NAME);

    SDFAttributeList newSchema = new SDFAttributeList();
    newSchema.addAttribute(association);

    return newSchema;
  }

  private SDFAttribute setSourceName(SDFAttribute attribute, String sourceName) {
    attribute.setSourceName(sourceName);
    for (SDFAttribute attSdfAttribute : attribute.getSubattributes()) {
      setSourceName(attSdfAttribute, sourceName);
    }
    return attribute;
  }

  @Override
  public AbstractLogicalOperator clone() {
    return new HypothesisGenerationAO<M>(this);
  }

  public void initPaths(String oldObjListPath, String newObjListPath) {
    this.oldObjListPath = oldObjListPath;
    this.newObjListPath = newObjListPath;
  }

  public String getNewObjListPath() {
    return this.newObjListPath;
  }

  public String getOldObjListPath() {
    return this.oldObjListPath;
  }

  public SDFAttributeList getLeftSchema() {
    if (this.getSubscribedToSource(LEFT) != null) {
      return this.getSubscribedToSource(LEFT).getSchema();
    } else {
      return null;
    }
  }

  public SDFAttributeList getRightSchema() {
    if (this.getSubscribedToSource(RIGHT) != null) {
      return this.getSubscribedToSource(RIGHT).getSchema();
    } else {
      return null;
    }
  }

}
