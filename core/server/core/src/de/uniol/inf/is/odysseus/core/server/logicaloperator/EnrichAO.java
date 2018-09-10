/********************************************************************************** 
  * Copyright 2013 The Odysseus Team
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
package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.Iterator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.PredicateParameter;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasPredicate;

/**
 * @author Dennis Geesen
 *
 */
@LogicalOperator(name = "ENRICH", minInputPorts = 2, maxInputPorts = 2, doc ="This operator enriches tuples with data that is cached, e.g. to enrich a stream with a list of categories. The first input stream, therefore, should be only stream limited data to avoid buffer overflows. The second input is the data stream that should be enriched.", category={LogicalOperatorCategory.ENRICH})
public class EnrichAO extends BinaryLogicalOp implements IHasPredicate {
	
	private static final long serialVersionUID = -4221371391163499952L;
	private int minimumSize = 0;
	private IPredicate<?> predicate;

	public EnrichAO(){
		
	}
	
	public EnrichAO(EnrichAO enrichAO) {
		super(enrichAO);
		this.minimumSize = enrichAO.minimumSize;
		if (enrichAO.predicate != null){
			this.predicate = enrichAO.predicate.clone();
		}
	}

	@Override
	public EnrichAO clone() {
		return new EnrichAO(this);
	}
	
	
	@Parameter(name = "minimumSize", type = IntegerParameter.class, optional = true, doc = "Blocks all until there are at least minimumSize elements in the chache")
	public synchronized void setMinimumSize(int i) {
		this.minimumSize   = i;
	}
	
	public int getMinimumSize(){
		return this.minimumSize;
	}
	
	@Parameter(name = "predicate", type = PredicateParameter.class, doc = "Predicate to filter combinations")
	public synchronized void setPredicate(IPredicate<?> joinPredicate) {
		this.predicate = joinPredicate;
	}
	
	@Override
	public IPredicate<?> getPredicate() {
		return predicate;
	}
	
	@Override
	public synchronized SDFSchema getOutputSchemaIntern(int pos) {
		// The Sum of all InputSchema
		Iterator<LogicalSubscription> iter = getSubscribedToSource().iterator();
		SDFSchema left = iter.next().getSchema();
		SDFSchema right = iter.next().getSchema();
		SDFSchema outputSchema = SDFSchema.join(left,right);
		
		setOutputSchema(outputSchema);
		return outputSchema;
	}

}
