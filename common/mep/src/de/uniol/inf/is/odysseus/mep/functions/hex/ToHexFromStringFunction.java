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
package de.uniol.inf.is.odysseus.mep.functions.hex;

import java.util.Formatter;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * Converts a given value to its hex representation.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class ToHexFromStringFunction extends AbstractFunction<String> {

    /**
     * 
     */
    private static final long serialVersionUID = 9004186949865153913L;
    private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFDatatype.STRING } };

    public ToHexFromStringFunction() {
        super("toHex", 1, accTypes, SDFDatatype.HEXSTRING);
    }

    @Override
    public String getValue() {
        String s = getInputValue(0).toString();
        byte[] bytes = s.getBytes();
        StringBuilder sb = new StringBuilder(bytes.length * 2);

        try (Formatter formatter = new Formatter(sb)) {
            for (byte b : bytes) {
                formatter.format("%02x", new Byte(b));
            }
        }
        return sb.toString();
    }

}
