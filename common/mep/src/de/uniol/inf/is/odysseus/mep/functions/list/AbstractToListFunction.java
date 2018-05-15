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
package de.uniol.inf.is.odysseus.mep.functions.list;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
abstract public class AbstractToListFunction extends AbstractFunction<List<?>> {

	@SuppressWarnings("unused")
	private static Logger LOG = LoggerFactory.getLogger(AbstractToListFunction.class);
    /**
     *
     */
    private static final long serialVersionUID = 3868126801758929823L;

    public AbstractToListFunction(int size) {
        super("toList", size, null, SDFDatatype.LIST);

    }

    @Override
    public List<?> getValue() {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < getArity(); i++) {
            list.add(getInputValue(i));
        }
        return list;
    }

    /* (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.mep.AbstractFunction#determineTypeFromInput()
     */
    @Override
    public boolean determineTypeFromInput() {
    	return true;
    }

    /* (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.mep.AbstractFunction#determineType(de.uniol.inf.is.odysseus.core.mep.IExpression[])
     */
    @Override
    public SDFDatatype determineType(IMepExpression<?>[] args) {
    	SDFDatatype subtype = args[0].getReturnType();

//    	for(SDFDatatype datatype : SDFDatatype.getLists()) {
//			if(datatype.getSubType().equals(subtype)) {
//				return datatype;
//			}
//    	}

    	return new SDFDatatype("LIST",SDFDatatype.KindOfDatatype.LIST,subtype);
    }
}
