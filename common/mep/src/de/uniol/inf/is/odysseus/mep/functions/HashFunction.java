/********************************************************************************** 
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.mep.functions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * Hash function to get the hash code of a given value.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class HashFunction extends AbstractFunction<Integer> {

    /**
     * 
     */
    private static final long serialVersionUID = 9002842200815792288L;
    private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { SDFDatatype.SIMPLE_TYPES };

    public HashFunction() {
        super("hash", 1, accTypes, SDFDatatype.INTEGER, false);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public Integer getValue() {
        return new Integer(getInputValue(0).hashCode());
    }

}
