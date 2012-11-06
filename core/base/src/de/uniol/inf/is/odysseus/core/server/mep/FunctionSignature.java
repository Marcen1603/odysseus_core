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
package de.uniol.inf.is.odysseus.core.server.mep;

import java.util.Arrays;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class FunctionSignature {
    private String              symbol;
    private List<SDFDatatype[]> parameter;

    public FunctionSignature(String symbol, List<SDFDatatype[]> parameter) {
        this.symbol = symbol.toUpperCase();
        this.parameter = parameter;
    }

    public String getSymbol() {
        return symbol;
    }

    public boolean contains(List<SDFDatatype> parameter) {
        boolean contains = true;
        for (int i = 0; i < parameter.size(); i++) {
            SDFDatatype[] datatypes = this.parameter.get(i);
            if (!Arrays.asList(datatypes).contains(parameter.get(i))) {
                contains = false;
                break;
            }
        }
        return contains;
    }

    @Override
    public String toString() {
        return "FunctionSignature [symbol=" + symbol + ", parameter=" + parameter + "]";
    }

}
