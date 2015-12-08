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
package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.PredicateParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasPredicate;

/**
 * This class represents an operator that buffers elements on port 0 for
 * a distinct time. 
 * A predicate is evaluated over input port 1. If the predicate is positive evaluated, 
 * buffered elements are merged with the elements that causes the true evaluation and is
 * send to the next operator for some time. 
 * 
 * @author Marco Grawunder
 *
 */
@LogicalOperator(maxInputPorts=2, minInputPorts=2, name="BUFFEREDFILTER", doc="This operator can be used to reduce data rate. It buffers incoming elements on port 0 (left) for bufferTime and evaluates a predicate over the elements on port 1 (right). If the predicate for the current element e evaluates to true, all elements from port 0 that are younger than e.startTimeStamp()-bufferTime will be enriched with e and delivered for deliverTime. Each time the predicate evaluates to true, the deliverTime will be increased.", category={LogicalOperatorCategory.ADVANCED})
public class BufferedFilterAO extends BinaryLogicalOp implements IHasPredicate{

	private static final long serialVersionUID = 5312945034141719894L;
	private TimeValueItem bufferTime;
	private TimeValueItem deliverTime;
		
	private IPredicate<?> predicate;

	public BufferedFilterAO() {
		super();
	}

	public BufferedFilterAO(BufferedFilterAO po) {
		super(po);
		this.bufferTime = po.bufferTime;
		this.deliverTime = po.deliverTime;
		if (po.predicate != null){
			this.predicate = po.predicate.clone();
		}
	}

	@Override
	public BufferedFilterAO clone() {
		return new BufferedFilterAO(this);
	}
	
	@SuppressWarnings("rawtypes")
	@Parameter(type=PredicateParameter.class)
	public void setPredicate(IPredicate predicate) {
			this.predicate = predicate;
	}
	
	@Override
	public IPredicate<?> getPredicate() {
		return predicate;
	}

	public TimeValueItem getBufferTime() {
		return bufferTime;
	}

	@Parameter(type=TimeParameter.class, name = "BUFFERTIME")
	public void setBufferTime(TimeValueItem bufferTime) {
		this.bufferTime = bufferTime;
	}

	public TimeValueItem getDeliverTime() {
		return deliverTime;
	}

	@Parameter(type=TimeParameter.class, name = "DELIVERTIME")
	public void setDeliverTime(TimeValueItem deliverTime) {
		this.deliverTime = deliverTime;
	}
	
	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		SDFSchema outputSchemna = null;
		if (pos == 0){
			SDFSchema tupelSchema = getInputSchema(0);
			SDFSchema predSchema = getInputSchema(RIGHT);
			if (tupelSchema != null && predSchema != null){
				// TODO: Schemata must be different!!
				outputSchemna = SDFSchema.union(predSchema, tupelSchema);
			}
 		}
		return outputSchemna;
		
	}

}
