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

import de.uniol.inf.is.odysseus.intervalapproach.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.mining.cleaning.detection.stateful.IBinaryDetection;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * 
 * @author Dennis Geesen Created at: 07.07.2011
 */
public class StatefulDetectionSplitPO<T extends IMetaAttributeContainer<ITimeInterval>> extends AbstractDetectionSplitPO<T, IBinaryDetection<T>> {
	
	//LEFT is data-port and RIGHT is aggregate-port
	private static final int LEFT = 0;
	private static final int RIGHT = 1;
	private SDFAttributeList[] inputSchemas = new SDFAttributeList[2];
	private PointInTime leftMin = PointInTime.getZeroTime();
	private PointInTime rightMin = PointInTime.getZeroTime();

	private DefaultTISweepArea<T> sweepAreaDetect = new DefaultTISweepArea<T>();
	private DefaultTISweepArea<T> sweepAreaCheck = new DefaultTISweepArea<T>();
	private PointInTime totalMin = PointInTime.getZeroTime();

	public StatefulDetectionSplitPO(List<IBinaryDetection<T>> detections) {
		super(detections);
	}

	public StatefulDetectionSplitPO(StatefulDetectionSplitPO<T> detectionSplitPO) {
		super(detectionSplitPO.detections);
	}

	private DefaultTISweepArea<T> getSA(int port){
		if(port==LEFT){
			return sweepAreaDetect;
		}else{
			return sweepAreaCheck;
		}
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
		for (IBinaryDetection<T> d : this.detections) {
			d.init(getInputSchema(LEFT), getInputSchema(RIGHT));
		}
	}

	private SDFAttributeList getInputSchema(int port) {
		if (port > 1 || port < 0) {
			throw new IllegalArgumentException("there are only two ports!");
		}

		return this.inputSchemas[port];
	}

	public void setInputSchemas(SDFAttributeList leftSchema, SDFAttributeList rightSchema) {
		this.inputSchemas[LEFT] = leftSchema;
		this.inputSchemas[RIGHT] = rightSchema;
	}

	@Override
	protected void process_next(T object, int port) {
		setMin(port, object.getMetadata().getStart());
		getSA(port).insert(object);		
		findAndEvaluate(object, port);
	}

	private void findAndEvaluate(T object, int currentInput){
		int otherInput = (currentInput+1)%2;
		
		getSA(currentInput).extractElementsBefore(totalMin);
		
		Iterator<T> iter = getSA(otherInput).extractElementsStartingEquals(object.getMetadata().getStart());
		while(iter.hasNext()){
			T o = iter.next();
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
	public StatefulDetectionSplitPO<T> clone() {
		return new StatefulDetectionSplitPO<T>(this);
	}

	@Override
	protected void process_next_failed(T object, int port, IBinaryDetection<T> detection) {
		System.out.println("FEHLER GEFUNDEN!");
		System.out.println("--> " + object);

	}

	@Override
	protected void process_next_passed(T object, int port) {
		System.out.println("KEIN FEHLER GEFUNDEN!");
		System.out.println("--> " + object);

	}

}
