/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.benchmark.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.benchmark.logicaloperator.BatchParameter.BatchItem;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.LongParameter;


@LogicalOperator(name="TESTPRODUCER", minInputPorts=0, maxInputPorts=0, doc="Create dummy data for benchmarking.",category={LogicalOperatorCategory.BENCHMARK})
public class TestProducerAO extends AbstractLogicalOperator {
	private static final long serialVersionUID = -6067355249297203590L;
	private ArrayList<Integer> elementCounts = new ArrayList<Integer>();
	private ArrayList<Long> frequencies = new ArrayList<Long>();
	private int invertedPriorityRatio = 0;
	private long delayMillis = 0;
	
	final private SDFSchema outputSchema;


	@Override
	public SDFSchema getOutputSchemaIntern(int pos) {
		return outputSchema;
	}
	
	public TestProducerAO() {
		this.outputSchema = new SDFSchema("Dummy",Tuple.class,new SDFAttribute(null,"Dummy", SDFDatatype.LONG, null, null, null));
	}

	@SuppressWarnings("unchecked")
	public TestProducerAO(TestProducerAO testProducerAO) {
		this.invertedPriorityRatio = testProducerAO.invertedPriorityRatio;
		this.elementCounts = (ArrayList<Integer>) testProducerAO.elementCounts
				.clone();
		this.frequencies = (ArrayList<Long>) testProducerAO.frequencies.clone();
		this.outputSchema = testProducerAO.outputSchema;
		this.delayMillis = testProducerAO.delayMillis;
	}
	
	public void addTestPart(int size, long wait) {
		this.elementCounts.add(size);
		this.frequencies.add(wait);
	}
	
	@Parameter(name="PARTS", type=BatchParameter.class, isList=true)
	public void setTestParts(List<BatchItem> parts){
		this.elementCounts.clear();
		this.frequencies.clear();
		for (BatchItem item : parts){
			addTestPart(item.size, item.wait);
		}
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new TestProducerAO(this);
	}

	public List<Integer> getElementCounts() {
		return elementCounts;
	}

	public List<Long> getFrequencies() {
		return frequencies;
	}

	public int getInvertedPriorityRatio() {
		return invertedPriorityRatio;
	}
	
	public long getDelayMillis() {
		return delayMillis;
	}

	@Parameter(type=IntegerParameter.class, optional = true)
	public void setInvertedPriorityRatio(int invertedPriorityRatio) {
		this.invertedPriorityRatio = invertedPriorityRatio;
	}
	
	@Parameter(type=LongParameter.class, optional=true)
	public void setDelay( long delay ) {
		this.delayMillis = delay;
	}
	
	@Override
	public boolean isSinkOperator() {
		return false;
	}
}
