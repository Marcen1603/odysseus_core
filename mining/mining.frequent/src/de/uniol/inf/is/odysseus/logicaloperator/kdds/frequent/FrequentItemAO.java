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
package de.uniol.inf.is.odysseus.logicaloperator.kdds.frequent;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.DoubleParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.EnumParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

@LogicalOperator(name = "FREQUENT_ITEM", minInputPorts = 1, maxInputPorts = 1)
public class FrequentItemAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = -3848482519907597388L;

	private double size = 0.0;
	private Strategy strategy = FrequentItemAO.Strategy.Simple;

	private List<SDFAttribute> choosenAttributes = new ArrayList<SDFAttribute>();

	private SDFSchema outputschema;
	private boolean recalc = true;

	public enum Strategy {
		Simple, LossyCounting, SpaceSaving
	}

	public FrequentItemAO() {
		super();
	}

	public FrequentItemAO(double size, Strategy strat, List<SDFAttribute> list) {
		super();
		if (size >= 0) {
			this.size = size;
		} else {
			throw new IllegalArgumentException(
					"Error Margin has to be between 0 and 1");
		}
		this.strategy = strat;
		this.choosenAttributes = list;
	}

	public FrequentItemAO(FrequentItemAO frequentItemAO) {
		super(frequentItemAO);
		this.size = frequentItemAO.size;
		this.strategy = frequentItemAO.strategy;
		this.recalc = frequentItemAO.recalc;
		this.outputschema = frequentItemAO.outputschema;
		this.choosenAttributes = frequentItemAO.choosenAttributes;
	}

	@Override
	public SDFSchema getOutputSchema() {
		if (recalc) {
			SDFSchema list = new SDFSchema("FrequentItem");
			for (SDFAttribute c : this.choosenAttributes) {
				list.add(c.clone());
			}
			SDFAttribute a = new SDFAttribute(null,"itemcount", SDFDatatype.INTEGER);
			list.add(a);
			this.outputschema = list;
			recalc = false;
		}
		return this.outputschema;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new FrequentItemAO(this);
	}

	public Strategy getStrategy() {
		return this.strategy;
	}

	@Parameter(name = "TYPE", type = EnumParameter.class, optional = true)
	public void setStrategy(Strategy strat) {
		this.strategy = strat;
	}

	@Parameter(type = DoubleParameter.class)
	public void setSize(double value) {
		this.size = value;
	}

	@Parameter(type = ResolvedSDFAttributeParameter.class, isList = true)
	public void setAttributes(List<SDFAttribute> attributes) {
		this.choosenAttributes = attributes;
	}

	public double getSize() {
		return this.size;
	}

	public List<SDFAttribute> getChoosenAttributeList() {
		return this.choosenAttributes;
	}

	public int[] getRestrictList() {
		return calcRestrictList(getInputSchema(0),
				new SDFSchema(getInputSchema(0).getURI(),this.getChoosenAttributeList()));
	}

	private static int[] calcRestrictList(SDFSchema in,
			SDFSchema out) {
		int[] ret = new int[out.size()];
		int i = 0;
		for (SDFAttribute a : out) {
			int j = 0;
			int k = i;
			for (SDFAttribute b : in) {
				if (b.equals(a)) {
					ret[i++] = j;
				}
				++j;
			}
			if (k == i) {
				throw new IllegalArgumentException("no such attribute: " + a);
			}
		}
		return ret;
	}

	@Override
	public boolean isValid() {
		if (size < 0 || size > 1) {
			addError(new IllegalArgumentException(
					"Error Margin has to be between 0 and 1"));
			return false;
		}
		return true;
	}

}
