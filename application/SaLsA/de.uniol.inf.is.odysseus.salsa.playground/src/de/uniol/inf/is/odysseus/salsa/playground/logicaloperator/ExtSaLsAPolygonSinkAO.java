/*******************************************************************************
 * Copyright 2012 The Odysseus Team
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
package de.uniol.inf.is.odysseus.salsa.playground.logicaloperator;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "ExtSaLsAPolygonSink")
public class ExtSaLsAPolygonSinkAO extends AbstractLogicalOperator  {

	private static final long serialVersionUID = 3958945913160253013L;

	public ExtSaLsAPolygonSinkAO() {
        super();
    }

    public ExtSaLsAPolygonSinkAO(final ExtSaLsAPolygonSinkAO ao) {
        super(ao);

    }

    @Override
    public ExtSaLsAPolygonSinkAO clone() {
        return new ExtSaLsAPolygonSinkAO(this);
    }


}

