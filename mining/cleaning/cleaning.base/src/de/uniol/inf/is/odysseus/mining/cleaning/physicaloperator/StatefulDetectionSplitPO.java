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

package de.uniol.inf.is.odysseus.mining.cleaning.physicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.mining.cleaning.detection.stateful.IBinaryDetection;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * 
 * @author Dennis Geesen
 * Created at: 07.07.2011
 */
public class StatefulDetectionSplitPO<T extends IMetaAttributeContainer<ITimeInterval>> extends AbstractDetectionSplitPO<T, IBinaryDetection<T>>{

	private T lastCompareValue;
	private static final int LEFT = 0;
	private static final int RIGHT = 1;
	private SDFAttributeList[] inputSchemas = new SDFAttributeList[2];
	
	public StatefulDetectionSplitPO(List<IBinaryDetection<T>> detections) {
		super(detections);		
	}
	
	public StatefulDetectionSplitPO(StatefulDetectionSplitPO<T> detectionSplitPO) {
		super(detectionSplitPO.detections);		
	}

	@Override
	public void process_open() throws OpenFailedException {	
		super.process_open();	
		for(IBinaryDetection<T> d : this.detections){
			d.init(getInputSchema(LEFT), getInputSchema(RIGHT));
		}
	}
	
	private SDFAttributeList getInputSchema(int port) {
		if(port>1 || port<0){
			throw new IllegalArgumentException("there are only two ports!");
		}
		
		return this.inputSchemas[port];
	}

	public void setInputSchemas(SDFAttributeList leftSchema, SDFAttributeList rightSchema){
		this.inputSchemas[0] = leftSchema;
		this.inputSchemas[1] = rightSchema;
	}
	
	@Override
	protected void process_next(T object, int port) {
		if(port==0){			
			if(this.lastCompareValue!=null){
				super.process_next(object, lastCompareValue, port);
			}						
		}else{
			if(port==1){
				this.lastCompareValue = object;
			}else{
				throw new IllegalArgumentException("Port "+port+" not allowed in this operator ("+this+")");
			}
		}		
	}

	@Override
	public StatefulDetectionSplitPO<T> clone() {
		return new StatefulDetectionSplitPO<T>(this);
	}

	@Override
	protected void process_next_failed(T object, int port, IBinaryDetection<T> detection) {
		System.out.println("FEHLER GEFUNDEN!");
		System.out.println("--> "+object);
		
	}

	@Override
	protected void process_next_passed(T object, int port) {
		System.out.println("KEIN FEHLER GEFUNDEN!");
		System.out.println("--> "+object);
		
	}

}
