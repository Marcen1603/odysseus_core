/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.scars.operator.association.ao;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.BinaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SchemaHelper;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.objecttracking.sdf.SDFSchemaExtended;

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

	// private static final String ASSOCIATION_SOURCE_NAME = "association";
	private String ASSOCIATION_RECORD_NAME = "scan";
	private String SCANNED_OBJECTS_NAME = "scannedObjects";
	private String PREDICTED_OBJECTS_NAME = "predictedObjects";

	private String sourcePredictedObjListPath;
	private String sourceScannedObjListPath;
	
	private String outputPredictedObjListPath;
	private String outputScannedObjListPath;

	public HypothesisGenerationAO() {
		super();
	}

	public HypothesisGenerationAO(HypothesisGenerationAO<M> copy) {
		super(copy);
		this.sourcePredictedObjListPath = copy.sourcePredictedObjListPath;
		this.sourceScannedObjListPath = copy.sourceScannedObjListPath;
		this.ASSOCIATION_RECORD_NAME = copy.ASSOCIATION_RECORD_NAME;
		this.SCANNED_OBJECTS_NAME = copy.SCANNED_OBJECTS_NAME;
		this.PREDICTED_OBJECTS_NAME = copy.PREDICTED_OBJECTS_NAME;
		this.outputPredictedObjListPath = copy.outputPredictedObjListPath;
		this.outputScannedObjListPath = copy.outputScannedObjListPath;
	}

	// LEFT -> SOURCE (neu erkannte objekte)
	// RIGHT -> PREDICTION (alte, pr�dizierte objekte)

	// Schema von PREDICTION wird erweitert um Schema der Liste von SOURCE
	// (analog zur �nderung des Tupels im PO)

	@Override
	public SDFSchema getOutputSchemaIntern() {
		SchemaHelper helper = null;

		// copy scanned Objects

		// Hier wird AUCH das rechte Schema genutzt damit die neu erkannten
		// Objekte das gleiche
		// Schema haben wie die pr�dizierten Objekte, auch wenn sie nicht alle
		// Attribute davon
		// besitzen --> alles aufs globale Schema mappen
		helper = new SchemaHelper(this.getSubscribedToSource(RIGHT).getSchema().clone());
		SDFAttribute scannedObjects = helper.getAttribute(this.sourcePredictedObjListPath).clone();
		this.outputScannedObjListPath = this.sourceScannedObjListPath.replace(scannedObjects.getAttributeName(), SCANNED_OBJECTS_NAME);
		// set new list name
		// FIXME: scannedObjects.setAttributeName(SCANNED_OBJECTS_NAME);

		// get timestamp from scanned data
		String timeStampName = helper.getStartTimestampFullAttributeName();
		SDFAttribute timestamp = helper.getAttribute(timeStampName);

		// copy scanned Objects
		SchemaHelper helper2 = new SchemaHelper(this.getSubscribedToSource(RIGHT).getSchema().clone());
		SDFAttribute predictedObjects = helper2.getAttribute(this.sourcePredictedObjListPath).clone();
		this.outputPredictedObjListPath = this.sourcePredictedObjListPath.replace(predictedObjects.getAttributeName(), PREDICTED_OBJECTS_NAME);
		String oldSource = predictedObjects.getSourceName();
		
		// set new list name
		// FIXME: predictedObjects.setAttributeName(PREDICTED_OBJECTS_NAME);

		// create new record
		
		SDFSchema subschema = new SDFSchema("",timestamp,scannedObjects,predictedObjects);
		
		SDFDatatype recordType = new SDFDatatype(null, SDFDatatype.KindOfDatatype.TUPLE, subschema);
		SDFAttribute association = new SDFAttribute("",ASSOCIATION_RECORD_NAME,recordType);
		

		// set source name
		helper = new SchemaHelper(this.getSubscribedToSource(LEFT).getSchema().clone());
		setSourceName(association, helper.getSourceName());

		// TODO: die metadaten aus dem inputschema mitnehmen
		List<SDFAttribute> newSchemaAttributes = new ArrayList<SDFAttribute>();
		newSchemaAttributes.add(association);
		SDFSchemaExtended newSchema = new SDFSchemaExtended(newSchemaAttributes);

		
		this.outputPredictedObjListPath = this.outputPredictedObjListPath.replace(oldSource, helper.getSourceName());
	

		return newSchema;
	}

	private SDFAttribute setSourceName(SDFAttribute attribute, String sourceName) {
		// FIXME: attribute.setSourceName(sourceName);
		
		if(attribute.getDatatype().getSchema() != null){
			for (SDFAttribute attSdfAttribute : attribute.getDatatype().getSchema()) {
				setSourceName(attSdfAttribute, sourceName);
			}
		}
		return attribute;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new HypothesisGenerationAO<M>(this);
	}

	public void initPaths(String oldObjListPath, String newObjListPath) {
		this.sourcePredictedObjListPath = oldObjListPath;
		this.sourceScannedObjListPath = newObjListPath;
	}

	public String getSourceScannedObjListPath() {
		return this.sourceScannedObjListPath;
	}

	public String getSourcePredictedObjListPath() {
		return this.sourcePredictedObjListPath;
	}
	
	public String getOutputScannedObjListPath() {
		return this.outputScannedObjListPath;
	}

	public String getOutputPredictedObjListPath() {
		return this.outputPredictedObjListPath;
	}

	public SDFSchema getLeftSchema() {
		if (this.getSubscribedToSource(LEFT) != null) {
			return this.getSubscribedToSource(LEFT).getSchema();
		}
        return null;
	}

	public SDFSchema getRightSchema() {
		if (this.getSubscribedToSource(RIGHT) != null) {
			return this.getSubscribedToSource(RIGHT).getSchema();
		}
        return null;
	}

	public String getASSOCIATION_RECORD_NAME() {
		return ASSOCIATION_RECORD_NAME;
	}

	public void setASSOCIATION_RECORD_NAME(String aSSOCIATION_RECORD_NAME) {
		ASSOCIATION_RECORD_NAME = aSSOCIATION_RECORD_NAME;
	}

	public String getSCANNED_OBJECTS_NAME() {
		return SCANNED_OBJECTS_NAME;
	}

	public void setSCANNED_OBJECTS_NAME(String sCANNED_OBJECTS_NAME) {
		SCANNED_OBJECTS_NAME = sCANNED_OBJECTS_NAME;
	}

	public String getPREDICTED_OBJECTS_NAME() {
		return PREDICTED_OBJECTS_NAME;
	}

	public void setPREDICTED_OBJECTS_NAME(String pREDICTED_OBJECTS_NAME) {
		PREDICTED_OBJECTS_NAME = pREDICTED_OBJECTS_NAME;
	}


}
