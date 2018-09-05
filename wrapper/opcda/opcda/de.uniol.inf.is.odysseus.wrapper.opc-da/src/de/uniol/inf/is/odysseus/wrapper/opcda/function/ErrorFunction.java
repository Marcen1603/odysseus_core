/*******************************************************************************
 * Copyright 2014 The Odysseus Team
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
package de.uniol.inf.is.odysseus.wrapper.opcda.function;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.wrapper.opcda.datatype.OPCValue;
import de.uniol.inf.is.odysseus.wrapper.opcda.sdf.schema.SDFOPCDADatatype;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class ErrorFunction extends AbstractFunction<Integer> {

    /**
     * 
     */
    private static final long serialVersionUID = 1381227821374591747L;
    private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { SDFOPCDADatatype.types };

    public ErrorFunction() {
        super("Error", 1, accTypes, SDFDatatype.INTEGER);
    }

    @Override
    public Integer getValue() {
        OPCValue<?> value = getInputValue(0);
        return value.getError();
    }
}
