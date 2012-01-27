/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.logicaloperator.benchmark;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.logicaloperator.benchmark.BatchParameter.BatchItem;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.usermanagement.client.GlobalState;

@LogicalOperator(name="TESTPRODUCER", minInputPorts=0, maxInputPorts=0)
public class TestProducerAO extends AbstractLogicalOperator {
	private static final long serialVersionUID = -6067355249297203590L;
	private ArrayList<Integer> elementCounts = new ArrayList<Integer>();
	private ArrayList<Long> frequencies = new ArrayList<Long>();
	private int invertedPriorityRatio = 0;
	
	final private SDFAttributeList outputSchema;


	@Override
	public SDFAttributeList getOutputSchema() {
		return outputSchema;
	}
	
	public TestProducerAO() {
		this.outputSchema = new SDFAttributeList("Dummy");
		SDFAttribute a = new SDFAttribute(null,"Dummy", SDFDatatype.LONG);
		outputSchema.add(a);
	}

	@SuppressWarnings("unchecked")
	public TestProducerAO(TestProducerAO testProducerAO) {
		this.invertedPriorityRatio = testProducerAO.invertedPriorityRatio;
		this.elementCounts = (ArrayList<Integer>) testProducerAO.elementCounts
				.clone();
		this.frequencies = (ArrayList<Long>) testProducerAO.frequencies.clone();
		this.outputSchema = testProducerAO.outputSchema;
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

	@Parameter(type=IntegerParameter.class)
	public void setInvertedPriorityRatio(int invertedPriorityRatio) {
		this.invertedPriorityRatio = invertedPriorityRatio;
	}
}
