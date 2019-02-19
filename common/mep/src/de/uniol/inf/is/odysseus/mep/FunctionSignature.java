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
package de.uniol.inf.is.odysseus.mep;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.uniol.inf.is.odysseus.core.mep.IFunctionSignatur;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

/**
 * @author Christian Kuka <christian@kuka.cc>
 */
public class FunctionSignature implements IFunctionSignatur{
    private final String symbol;
    private final List<SDFDatatype[]> parameters;

    public FunctionSignature(String symbol, List<SDFDatatype[]> parameters) {
        Objects.requireNonNull(symbol);
        this.symbol = symbol.toUpperCase();
        this.parameters = new ArrayList<SDFDatatype[]>();
        if (parameters != null) {
            for (SDFDatatype[] parameter : parameters) {
                Objects.requireNonNull(parameter);
                this.parameters.add(parameter.clone());
            }
        }
    }

    public FunctionSignature(FunctionSignature functionSignature) {
        this.symbol = functionSignature.symbol;
        this.parameters = new ArrayList<SDFDatatype[]>();
        if (parameters != null) {
            for (SDFDatatype[] parameter : functionSignature.parameters) {
                Objects.requireNonNull(parameter);
                this.parameters.add(parameter.clone());
            }
        }
    }

    @Override
    public String getSymbol() {
        return symbol;
    }

    @Override
    public List<SDFDatatype[]> getParameters() {
    	return parameters;
    }
    
    @Override
    public boolean contains(List<SDFDatatype> parameters) {
        Objects.requireNonNull(parameters);
        boolean contains = true;
        if (parameters.size() != this.parameters.size()) {
            return false;
        }
        for (int i = 0; i < parameters.size(); i++) {
            Objects.requireNonNull(parameters.get(i));
            SDFDatatype[] datatypes = this.parameters.get(i);
            // this makes problems with e.g. LIST or TUPLE with subtypes 
            // that can be ignored here.
//            if (!Arrays.asList(datatypes).contains(parameters.get(i))) {
//                contains = false;
//                break;
//            }

            boolean found = false;
            for (SDFDatatype dt : datatypes) {
            	if (dt.getURI().equalsIgnoreCase(parameters.get(i).getURI())){
            		found = true;
            	}
			}
            if (!found){
            	contains = false;
            	break;
            }
            
        }
        return contains;
    }

    @Override
    public FunctionSignature clone() {
        return new FunctionSignature(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((parameters == null) ? 0 : parameters.hashCode());
        result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        FunctionSignature other = (FunctionSignature) obj;
        if (parameters == null) {
            if (other.parameters != null) {
                return false;
            }
        }
        else if (!parameters.equals(other.parameters)) {
            return false;
        }
        if (symbol == null) {
            if (other.symbol != null) {
                return false;
            }
        }
        else if (!symbol.equals(other.symbol)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(symbol).append("(");
        int i = 0;
        for (SDFDatatype[] parameter : parameters) {
            int j = 0;
            if (i > 0) {
                sb.append(",");
            }
            for (SDFDatatype type : parameter) {
                if (j > 0) {
                    sb.append("|");
                }
                sb.append(type);
                j++;
            }
            i++;
        }
        sb.append(")");

        return sb.toString();
    }

}
