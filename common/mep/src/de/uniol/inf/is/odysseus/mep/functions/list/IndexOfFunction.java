/********************************************************************************** 
 * Copyright 2014 The Odysseus Team
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
package de.uniol.inf.is.odysseus.mep.functions.list;

import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class IndexOfFunction extends AbstractFunction<Integer> {

    /**
     * 
     */
    private static final long serialVersionUID = 5843632636388251029L;
    private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { { SDFDatatype.LIST }, SDFDatatype.SIMPLE_TYPES };

    public IndexOfFunction() {
        super("IndexOf", 2, ACC_TYPES, SDFDatatype.INTEGER, false);
    }

    @Override
    public Integer getValue() {
        List<?> list = (List<?>) getInputValue(0);
        Object object = getInputValue(1);
        return new Integer(list.indexOf(object));
    }

}
