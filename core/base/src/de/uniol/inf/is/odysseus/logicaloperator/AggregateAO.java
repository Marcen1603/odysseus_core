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
package de.uniol.inf.is.odysseus.logicaloperator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.physicaloperator.AggregateFunction;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class AggregateAO extends UnaryLogicalOp {

    private static final long serialVersionUID = 2539966167342852544L;

    private Map<SDFAttributeList, Map<AggregateFunction, SDFAttribute>> aggregations = new HashMap<SDFAttributeList, Map<AggregateFunction, SDFAttribute>>();

    private List<SDFAttribute> groupingAttributes = new ArrayList<SDFAttribute>();

    private SDFAttributeList outputSchema = null;
    
    private boolean dumpOnEveryObject = false;
    
    public AggregateAO() {
        super();
        aggregations = new HashMap<SDFAttributeList, Map<AggregateFunction, SDFAttribute>>();
        groupingAttributes = new ArrayList<SDFAttribute>();
        outputSchema = new SDFAttributeList();
    }

    public AggregateAO(AggregateAO op) {
        super(op);
        aggregations = new HashMap<SDFAttributeList, Map<AggregateFunction, SDFAttribute>>(
                op.aggregations);
        groupingAttributes = new ArrayList<SDFAttribute>(op.groupingAttributes);
        outputSchema = op.outputSchema.clone();
        dumpOnEveryObject = op.dumpOnEveryObject;
    }

    public void addAggregation(SDFAttribute attribute,
            AggregateFunction function, SDFAttribute outAttribute) {
        SDFAttributeList attributes = new SDFAttributeList();
        attributes.add(attribute);
        addAggregation(attributes, function, outAttribute);
    }

    public void addAggregation(SDFAttributeList attributes,
            AggregateFunction function, SDFAttribute outAttribute) {
        if (getOutputSchema().contains(outAttribute)) {
            throw new IllegalArgumentException(
                    "multiple definitions of element " + outAttribute);
        }

        getOutputSchema().add(outAttribute);
        Map<AggregateFunction, SDFAttribute> af = aggregations.get(attributes);
        if (af == null) {
            af = new HashMap<AggregateFunction, SDFAttribute>();
            aggregations.put(attributes, af);
        }
        af.put(function, outAttribute);
    }

    public void addAggregations(
            Map<SDFAttribute, Map<AggregateFunction, SDFAttribute>> aggregations) {
        for (Entry<SDFAttribute, Map<AggregateFunction, SDFAttribute>> attrSet : aggregations.entrySet()) {
        	Map<AggregateFunction, SDFAttribute> aggs = attrSet.getValue();
            for (Entry<AggregateFunction, SDFAttribute> e : aggs.entrySet()) {
                addAggregation(attrSet.getKey(), e.getKey(), e.getValue());
            }
        }
    }


    public Map<SDFAttributeList, Map<AggregateFunction, SDFAttribute>> getAggregations() {
        return this.aggregations;
    }

    public boolean hasAggregations() {
        return !this.aggregations.isEmpty();
    }

    public void addGroupingAttribute(SDFAttribute attribute) {
        if (groupingAttributes.contains(attribute)) {
            return;
        }
        groupingAttributes.add(attribute);
        getOutputSchema().add(attribute);
    }

    public void addGroupingAttributes(SDFAttributeList attributes) {
        for (SDFAttribute a : attributes) {
            addGroupingAttribute(a);
        }
    }

    public List<SDFAttribute> getGroupingAttributes() {
        return Collections.unmodifiableList(groupingAttributes);
    }

    @Override
    public AggregateAO clone() {
        return new AggregateAO(this);
    }

    @Override
    public SDFAttributeList getOutputSchema() {
        return outputSchema;
    }

    public void setDumpOnEveryObject(boolean dumpOnEveryObject) {
		this.dumpOnEveryObject = dumpOnEveryObject;
	}
    
    public boolean isDumpOnEveryObject() {
		return dumpOnEveryObject;
	}

    
}
