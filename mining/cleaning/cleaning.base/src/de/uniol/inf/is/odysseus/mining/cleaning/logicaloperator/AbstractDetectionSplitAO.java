/** Copyright 2011 The Odysseus Team
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

package de.uniol.inf.is.odysseus.mining.cleaning.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.logicaloperator.BinaryLogicalOp;
import de.uniol.inf.is.odysseus.logicaloperator.OutputSchemaSettable;
import de.uniol.inf.is.odysseus.mining.cleaning.detection.IDetection;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * 
 * @author Dennis Geesen Created at: 06.07.2011
 */
public abstract class AbstractDetectionSplitAO<T, D extends IDetection<T>> extends BinaryLogicalOp implements OutputSchemaSettable {

	private static final long serialVersionUID = -2193273482190920976L;
	private SDFAttributeList outputschema;
	private List<D> detections = new ArrayList<D>();

	public AbstractDetectionSplitAO() {

	}

	public AbstractDetectionSplitAO(AbstractDetectionSplitAO<T,D> detectionAO) {
		super(detectionAO);
		this.outputschema = detectionAO.outputschema.clone();
		this.detections = detectionAO.detections;
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return this.outputschema;
	}

	public void addDetection(D detection) {
		this.detections.add(detection);		
	}


	@Override
	public void setOutputSchema(SDFAttributeList outputSchema) {
		this.outputschema = outputSchema;		
	}

	@Override
	public void setOutputSchema(SDFAttributeList outputSchema, int port) {
		if(port>0){
			throw new UnsupportedOperationException("no ports greater than 0 supported");
		}
		setOutputSchema(outputSchema);	

	}
	
	public List<D> getDetections(){
		return this.detections;
	}

}
