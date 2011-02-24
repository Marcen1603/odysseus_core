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
package de.uniol.inf.is.odysseus.logicaloperator.builder;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class AggregateAOBuilder extends AbstractOperatorBuilder {

	private static final long serialVersionUID = -1182294150681714511L;

	private ListParameter<SDFAttribute> groupBy = new ListParameter<SDFAttribute>(
			"GROUP_BY", REQUIREMENT.OPTIONAL,
			new ResolvedSDFAttributeParameter("group_by attribute",
					REQUIREMENT.MANDATORY));

	private ListParameter<AggregateItem> aggregations = new ListParameter<AggregateItem>(
			"AGGREGATIONS", REQUIREMENT.MANDATORY, new AggregateItemParameter(
					"aggregation entry", REQUIREMENT.MANDATORY));
	
	private final IntegerParameter dumpAtValueCount = new IntegerParameter(
			"DumpAtValueCount", REQUIREMENT.OPTIONAL);	

	public AggregateAOBuilder() {
		super(1, 1);
		setParameters(groupBy, aggregations, dumpAtValueCount);
	}

	@Override
	protected ILogicalOperator createOperatorInternal() {
		AggregateAO ao = new AggregateAO();

		if (groupBy.hasValue()) {
			SDFAttributeList groupList = new SDFAttributeList(groupBy
					.getValue());
			ao.addGroupingAttributes(groupList);
		}

		for (AggregateItem item : aggregations.getValue()) {
			ao.addAggregation(item.inAttribute, item.aggregateFunction,
					item.outAttribute);
		}
		
		if (dumpAtValueCount.hasValue()){
			ao.setDumpAtValueCount(dumpAtValueCount.getValue());
		}

		return ao;
	}

	@Override
	protected boolean internalValidation() {
		
		boolean hasErrors = false;
		if( aggregations.hasValue() ) {
			List<AggregateItem> items = aggregations.getValue();
			List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
			
			for(AggregateItem item : items) {
				SDFAttribute attribute = item.outAttribute;
				if( attributes.contains(attribute)) {
					addError(new IllegalParameterException("dublicate attribute name:" + attribute.getAttributeName()));
					hasErrors = true;
				} else {
					attributes.add(attribute);
				}
					
			}
		}
		return !hasErrors;
	}

}
