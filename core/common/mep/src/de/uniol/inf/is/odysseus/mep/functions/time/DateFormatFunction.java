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
package de.uniol.inf.is.odysseus.mep.functions.time;

import java.text.SimpleDateFormat;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * 
 * @author Marco Grawunder
 */
public class DateFormatFunction extends AbstractFunction<String> {

    private static final long serialVersionUID = -3960501264856271045L;

    private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { { SDFDatatype.DATE },{SDFDatatype.STRING} };

    private SimpleDateFormat formatter;
    
    public DateFormatFunction() {
        super("format", 2, ACC_TYPES, SDFDatatype.STRING, false);
    }

    @Override
    public String getValue() {
    	if (formatter == null){
    		formatter = new SimpleDateFormat(String.valueOf(getInputValue(1)));
    	}
        return formatter.format(getInputValue(0));
    }
}
