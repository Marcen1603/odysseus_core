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
 * Created on 07.12.2004
 *
 */
package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasPredicate;

/**
 * @author Marco Grawunder
 *
 */
@LogicalOperator(name = "DIFFERENCE", minInputPorts = 2, maxInputPorts = 2, doc = "This operator calculates the difference between two input sets.", url = "http://odysseus.offis.uni-oldenburg.de:8090/display/ODYSSEUS/Difference+operator", category = {
        LogicalOperatorCategory.BASE, LogicalOperatorCategory.SET })
public class DifferenceAO extends BinaryLogicalOp implements IHasPredicate {

    private static final long serialVersionUID = 4518770628909423647L;
    private IPredicate<?> predicate;

    public DifferenceAO(DifferenceAO differenceAO) {
        super(differenceAO);
        if (differenceAO.predicate != null) {
            this.predicate = differenceAO.predicate;
        }
    }

    public DifferenceAO() {
        super();
    }

    public synchronized void setPredicate(IPredicate<?> predicate) {
        this.predicate = predicate;
    }

    @Override
    public IPredicate<?> getPredicate() {
        return predicate;
    }

    public @Override DifferenceAO clone() {
        return new DifferenceAO(this);
    }

    @Override
    public SDFSchema getOutputSchemaIntern(int pos) {
        return getInputSchema(LEFT);
    }

}
