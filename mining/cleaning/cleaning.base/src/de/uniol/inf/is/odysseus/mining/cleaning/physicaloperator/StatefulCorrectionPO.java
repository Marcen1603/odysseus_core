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
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.mining.cleaning.correction.stateful.IBinaryCorrection;
import de.uniol.inf.is.odysseus.mining.metadata.IMiningMetadata;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * 
 * @author Dennis Geesen Created at: 11.07.2011
 */
public class StatefulCorrectionPO<Meta extends IMiningMetadata, Data extends IMetaAttributeContainer<Meta>> extends
		AbstractCorrectionPO<Data, IBinaryCorrection<Data>> {

	private static final int LEFT = 0;
	private static final int RIGHT = 1;

	private PointInTime leftMin = PointInTime.getZeroTime();
	private PointInTime rightMin = PointInTime.getZeroTime();

	private DefaultTISweepArea<Data> sweepAreaData = new DefaultTISweepArea<Data>();
	private DefaultTISweepArea<Data> sweepAreaForCorrection = new DefaultTISweepArea<Data>();
	private PointInTime totalMin = PointInTime.getZeroTime();

	private SDFAttributeList[] inputSchemas;
	private Data currentValueForCorrection;

	public StatefulCorrectionPO(List<IBinaryCorrection<Data>> corrections) {
		super(corrections);
	}

	public StatefulCorrectionPO(StatefulCorrectionPO<Meta, Data> statefulCorrectionPO) {
		super(statefulCorrectionPO.corrections);
	}

	@Override
	protected Data getValueForCorrection(Data original, IBinaryCorrection<Data> c) {
		return currentValueForCorrection;
	}

	private DefaultTISweepArea<Data> getSA(int port) {
		if (port == LEFT) {
			return sweepAreaData;
		} else {
			return sweepAreaForCorrection;
		}
	}

	private void setMin(int port, PointInTime time) {
		if (port == LEFT) {
			if (leftMin.before(time)) {
				leftMin = time.clone();
			}
		} else {
			if (rightMin.before(time)) {
				rightMin = time.clone();
			}
		}
		if (this.rightMin.before(leftMin)) {
			this.totalMin = rightMin.clone();
		} else {
			this.totalMin = leftMin.clone();
		}
	}

	@Override
	public void process_open() throws OpenFailedException {
		super.process_open();
		for (IBinaryCorrection<Data> d : this.corrections) {
			d.init(getInputSchema(LEFT), getInputSchema(RIGHT));
		}
	}

	private SDFAttributeList getInputSchema(int port) {
		if (port > 1 || port < 0) {
			throw new IllegalArgumentException("there are only two ports!");
		}

		return this.inputSchemas[port];
	}

	@Override
	protected void process_next(Data object, int port) {
		setMin(port, object.getMetadata().getStart());
		getSA(port).insert(object);
		findAndEvaluate(object, port);
	}

	private void findAndEvaluate(Data object, int currentInput) {
		int otherInput = (currentInput + 1) % 2;

		getSA(currentInput).extractElementsBefore(totalMin);

		Iterator<Data> iter = getSA(otherInput).extractElementsStartingEquals(object.getMetadata().getStart());
		while (iter.hasNext()) {
			Data o = iter.next();			
			if (currentInput == RIGHT) {
				super.process_next(o, object, currentInput);				
			} else {				
				super.process_next(object, o, currentInput);
			}
		}

	}

	@Override
	public StatefulCorrectionPO<Meta, Data> clone() {
		return new StatefulCorrectionPO<Meta, Data>(this);
	}

	public void setInputSchemas(SDFAttributeList leftSchema, SDFAttributeList rightSchema) {
		this.inputSchemas[LEFT] = leftSchema;
		this.inputSchemas[RIGHT] = rightSchema;

	}

}
