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
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "JS", doc = "This operator supports JavaScript scripts for data processing", category = { LogicalOperatorCategory.BASE })
public class JavaScriptAO extends AbstractScriptAO {

    /**
     *
     */
    private static final long serialVersionUID = 180912782907319180L;

    /**
     * Class constructor.
     *
     */
    public JavaScriptAO() {
    }

    /**
     * Class constructor.
     *
     * @param operator
     */
    public JavaScriptAO(final JavaScriptAO operator) {
        this.path = operator.path;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JavaScriptAO clone() {
        return new JavaScriptAO(this);
    }

}
