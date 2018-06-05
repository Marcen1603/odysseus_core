/********************************************************************************** 
 * Copyright 2016 The Odysseus Team
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
package de.uniol.inf.is.odysseus.mep.functions.transform;

import de.uniol.inf.is.odysseus.core.IHasAlias;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class ToStringFunction extends AbstractFunction<String> implements IHasAlias{

    private static final long serialVersionUID = -3960501264856271045L;

    private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { { SDFDatatype.OBJECT, SDFDatatype.DATE, SDFDatatype.DOUBLE, SDFDatatype.END_TIMESTAMP, SDFDatatype.END_TIMESTAMP_STRING,
            SDFDatatype.FLOAT, SDFDatatype.INTEGER, SDFDatatype.LONG, SDFDatatype.START_TIMESTAMP, SDFDatatype.START_TIMESTAMP_STRING, SDFDatatype.SHORT, SDFDatatype.BYTE, SDFDatatype.LIST,
            SDFDatatype.STRING, SDFDatatype.DOCUMENT, SDFDatatype.TIMESTAMP, SDFDatatype.BOOLEAN, SDFDatatype.PARTIAL_AGGREGATE, SDFDatatype.AVG_SUM_PARTIAL_AGGREGATE,
            SDFDatatype.COUNT_PARTIAL_AGGREGATE, SDFDatatype.RELATIONAL_ELEMENT_PARTIAL_AGGREGATE, SDFDatatype.LIST_PARTIAL_AGGREGATE } };

    public ToStringFunction() {
        super("toString", 1, ACC_TYPES, SDFDatatype.STRING);
    }

    @Override
	public String getValue() {
		Object input = getInputValue(0);
		if (input == null) {
			return "";
		}
		return input.toString();
	}
    
    @Override
    public String getAliasName() {
    	return "str";
    }
}
