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
/*
 * Created on 10.12.2004
 *
 */
package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;

/**
 * @author Marco Grawunder
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
@LogicalOperator(minInputPorts = 1, maxInputPorts = 1, name = "SORT", category = { LogicalOperatorCategory.BASE }, doc = "Sort operator", url = "http://wiki.odysseus.informatik.uni-oldenburg.de/display/ODYSSEUS/Sort")
public class SortAO extends UnaryLogicalOp {

    private static final long serialVersionUID = 3251466434501011289L;

    /**
     * tells you if the attribute at the
     * specified index has to be in ascending
     * (true) or descending (false) order
     */
    private List<Boolean> ascending;
    /** List of sort attributes. */
    private List<SDFAttribute> attributes;

    /**
     * 
     */
    public SortAO() {
        super();
        this.attributes = new ArrayList<>();
        this.ascending = new ArrayList<>();
    }

    /**
     * @param sortPO
     */
    public SortAO(SortAO sortPO) {
        super(sortPO);
        this.attributes = new ArrayList<>(sortPO.attributes);
        this.ascending = new ArrayList<>(sortPO.ascending);

    }

    @Parameter(type = ResolvedSDFAttributeParameter.class, name = "ATTRIBUTES", optional = false, isList = true, doc = "A list of attributes that should be used.")
    public void setAttributes(List<SDFAttribute> attributes) {
        this.attributes = attributes;
    }

    public List<SDFAttribute> getAttributes() {
        return this.attributes;
    }

    @Parameter(type = BooleanParameter.class, name = "ASCENDING", optional = true, isList = true, doc = "The sort of each attribute")
    public void setAscending(List<Boolean> ascending) {
        this.ascending = ascending;

    }

    public List<Boolean> getAscending() {
        return this.ascending;
    }

    public boolean[] getAscendingArray() {
        boolean[] ascendingArray = new boolean[this.attributes.size()];
        for (int i = 0; i < this.attributes.size(); i++) {
            if (this.ascending.size() > i) {
                ascendingArray[i] = this.ascending.get(i).booleanValue();
            }
        }
        return ascendingArray;
    }

    /**
     * @return
     */
    public int[] getSortAttributePos() {
        return determineSortList();
    }

    public @Override SortAO clone() {
        return new SortAO(this);
    }

    private int[] determineSortList() {
        return this.getInputSchema().indexesOf(this.attributes);
    }

}
