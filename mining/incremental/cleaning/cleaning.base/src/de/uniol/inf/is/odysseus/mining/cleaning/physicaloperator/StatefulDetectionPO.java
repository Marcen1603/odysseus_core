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

import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.intervalapproach.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.mining.cleaning.detection.stateful.IBinaryDetection;
import de.uniol.inf.is.odysseus.mining.metadata.IMiningMetadata;

/**
 * 
 * @author Dennis Geesen Created at: 07.07.2011
 */
public class StatefulDetectionPO<Meta extends IMiningMetadata, Data  extends IMetaAttributeContainer<Meta>> extends AbstractDetectionPO<Data, IBinaryDetection<Data>> {
	
	//LEFT is data-port and RIGHT is aggregate-port
	private static final int LEFT = 0;
	private static final int RIGHT = 1;
	private SDFSchema[] inputSchemas = new SDFSchema[2];
	private PointInTime leftMin = PointInTime.getZeroTime();
	private PointInTime rightMin = PointInTime.getZeroTime();

	private DefaultTISweepArea<Data> sweepAreaDetect = new DefaultTISweepArea<Data>();
	private DefaultTISweepArea<Data> sweepAreaCheck = new DefaultTISweepArea<Data>();
	private PointInTime totalMin = PointInTime.getZeroTime();

	public StatefulDetectionPO(List<IBinaryDetection<Data>> detections) {
		super(detections);
	}

	public StatefulDetectionPO(StatefulDetectionPO<Meta, Data> detectionSplitPO) {
		super(detectionSplitPO.detections);
	}

	private DefaultTISweepArea<Data> getSA(int port){
		if(port==LEFT){
			return sweepAreaDetect;
		}
        return sweepAreaCheck;
	}
	
	private void setMin(int port, PointInTime time){
		if(port==LEFT){
			if(leftMin.before(time)){
				leftMin=time.clone();
			}
		}else{
			if(rightMin.before(time)){
				rightMin = time.clone();
			}
		}
		if(this.rightMin.before(leftMin)){
			this.totalMin = rightMin.clone(); 
		}else{
			this.totalMin = leftMin.clone();
		}		
	}


	@Override
	public void process_open() throws OpenFailedException {
		super.process_open();
		for (IBinaryDetection<Data> d : this.detections) {
			d.init(getInputSchema(LEFT), getInputSchema(RIGHT));
		}
	}

	private SDFSchema getInputSchema(int port) {
		if (port > 1 || port < 0) {
			throw new IllegalArgumentException("there are only two ports!");
		}

		return this.inputSchemas[port];
	}

	public void setInputSchemas(SDFSchema leftSchema, SDFSchema rightSchema) {
		this.inputSchemas[LEFT] = leftSchema;
		this.inputSchemas[RIGHT] = rightSchema;
	}

	@Override
	protected void process_next(Data object, int port) {
		setMin(port, object.getMetadata().getStart());
		getSA(port).insert(object);		
		findAndEvaluate(object, port);
	}

	private void findAndEvaluate(Data object, int currentInput){
		int otherInput = (currentInput+1)%2;
		
		getSA(currentInput).extractElementsBefore(totalMin);
		
		Iterator<Data> iter = getSA(otherInput).extractElementsStartingEquals(object.getMetadata().getStart());
		while(iter.hasNext()){
			Data o = iter.next();
			System.out.println("vergleiche: ");
			System.out.println("- "+object);
			System.out.println("- "+o);
			if(currentInput==RIGHT){
				super.process_next(o, object, currentInput);
			}else{
				super.process_next(object, o, currentInput);
			}
		}
	}
	
	@Override
	public StatefulDetectionPO<Meta, Data> clone() {
		return new StatefulDetectionPO<Meta, Data>(this);
	}

	@Override
	protected void process_next_failed(Data object, int port, IBinaryDetection<Data> detection) {
		System.out.println("FEHLER GEFUNDEN!");
		System.out.println("--> " + object);
		transfer(object, 0);

	}

	@Override
	protected void process_next_passed(Data object, int port) {
		transfer(object, 0);
	}

}
