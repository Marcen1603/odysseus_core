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
package de.uniol.inf.is.odysseus.relational_interval;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.server.intervalapproach.TimestampToPayloadPO;

public class RelationalTimestampToPayloadPO extends
		TimestampToPayloadPO<ITimeInterval, Tuple<ITimeInterval>> {

	public RelationalTimestampToPayloadPO(
			RelationalTimestampToPayloadPO relationalTimestampToPayloadPO) {
		super(relationalTimestampToPayloadPO);
	}

	public RelationalTimestampToPayloadPO() {
	}

	@Override
	protected void process_next(Tuple<ITimeInterval> object, int port) {
		int inputSize = object.size();
		Tuple<ITimeInterval> out = new Tuple<ITimeInterval>(
				object.size()+2, false);
		
		System.arraycopy(object.getAttributes(), 0, out.getAttributes(), 0,
				inputSize);
		
		if (object.getMetadata().getStart().isInfinite()) {
			out.setAttribute(inputSize,-1L);
		} else {
			out.setAttribute(inputSize, object.getMetadata().getStart().getMainPoint());
		}
		if (object.getMetadata().getEnd().isInfinite()) {
			out.setAttribute(inputSize+1,-1L);
		} else {
			out.setAttribute(inputSize+1,object.getMetadata().getEnd().getMainPoint());
		}
		out.setMetadata((ITimeInterval)object.getMetadata().clone());
		out.setRequiresDeepClone(object.requiresDeepClone());
		transfer(out);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}
	
	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof RelationalTimestampToPayloadPO)){
			return false;
		}
		RelationalTimestampToPayloadPO po = (RelationalTimestampToPayloadPO) ipo;
		
		if (!po.getOutputSchema().equals(getOutputSchema())){
			return false;
		}
		
		return true;
	}
}
