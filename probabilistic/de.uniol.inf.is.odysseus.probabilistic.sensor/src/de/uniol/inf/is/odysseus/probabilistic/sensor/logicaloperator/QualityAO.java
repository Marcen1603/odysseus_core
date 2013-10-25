/**
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
package de.uniol.inf.is.odysseus.probabilistic.sensor.logicaloperator;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, doc = "Append quality information to the incoming stream object.", name = "Probabilistic", category = { LogicalOperatorCategory.ONTOLOGY })
public class QualityAO extends UnaryLogicalOp {

    /**
	 * 
	 */
    private static final long serialVersionUID = 7153504084002972374L;

    /**
     * Class constructor.
     * 
     */
    public QualityAO() {
        // TODO Auto-generated constructor stub
    }

    /**
     * Clone constructor.
     * 
     * @param qualityAO
     *            The instance to clone from
     */
    public QualityAO(final QualityAO qualityAO) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractLogicalOperator clone() {
        return new QualityAO(this);
    }

}
