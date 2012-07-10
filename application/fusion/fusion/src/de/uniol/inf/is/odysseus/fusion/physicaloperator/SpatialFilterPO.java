/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.fusion.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.collection.Tuple;

public class SpatialFilterPO  extends AbstractPipe<Tuple<? extends IMetaAttribute>, Tuple<? extends IMetaAttribute>> {

	public SpatialFilterPO(SDFSchema outputSchema) { 
		super();
		this.setOutputSchema(outputSchema);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(Tuple<? extends IMetaAttribute> object, int port) {
		
		// compute the distance between Predicted State and Measurement 
		
		
		
		// TODO Auto-generated method stub
		// System.out.println("Inputport: " + port + " Tuple: " + object.toString());
		System.out.println("Start: " + ((ITimeInterval)object.getMetadata()).getStart() + " Ende: " + ((ITimeInterval)object.getMetadata()).getEnd() + " Interval: " + (((ITimeInterval)object.getMetadata()).getEnd().minus(((ITimeInterval)object.getMetadata()).getStart())));
		if(port == 0)
			transfer(object);
		
		System.out.println(object.getAttribute(3));
		
		process_done();
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AbstractPipe<Tuple<? extends IMetaAttribute>, Tuple<? extends IMetaAttribute>> clone() {
		return this.clone();
	}

}
