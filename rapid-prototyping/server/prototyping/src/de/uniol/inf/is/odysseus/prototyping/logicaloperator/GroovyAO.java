/*******************************************************************************
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.prototyping.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "GROOVY", doc = "This operator supports Groovy scripts for data processing", category = { LogicalOperatorCategory.BASE })
public class GroovyAO extends AbstractScriptAO {

    /**
     *
     */
    private static final long serialVersionUID = -2439741955932412466L;

    /**
     * Class constructor.
     *
     */
    public GroovyAO() {
    }

    /**
     * Class constructor.
     *
     * @param operator
     */
    public GroovyAO(final GroovyAO operator) {
        this.path = operator.path;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GroovyAO clone() {
        return new GroovyAO(this);
    }

}
