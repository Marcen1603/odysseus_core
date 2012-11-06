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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableSet;

import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class FunctionStore {
    Map<String, List<FunctionSignature>> symbols    = new HashMap<String, List<FunctionSignature>>();
    Map<FunctionSignature, IFunction<?>> signatures = new HashMap<FunctionSignature, IFunction<?>>();

    public void clear() {
        symbols.clear();
        signatures.clear();
    }

    public boolean containsSymbol(String symbol) {
        return symbols.containsKey(symbol.toUpperCase());
    }

    public boolean containsSignature(FunctionSignature signature) {
        return signatures.containsKey(signature);
    }

    public List<IFunction<?>> getFunctions(String symbol) {
        List<IFunction<?>> functions = new ArrayList<IFunction<?>>();
        List<FunctionSignature> signatures = symbols.get(symbol.toUpperCase());
        for (FunctionSignature signature : signatures) {
            functions.add(this.signatures.get(signature));
        }
        return functions;
    }

    public IFunction<?> getFunction(FunctionSignature signature) {
        return this.signatures.get(signature);
    }

    public IFunction<?> getFunction(String symbol, List<SDFDatatype> parameter) {
        List<FunctionSignature> signatureList = this.symbols.get(symbol.toUpperCase());
        IFunction<?> function = null;
        for (FunctionSignature signature : signatureList) {
            if (signature.contains(parameter)) {
                function = this.signatures.get(signature);
                break;
            }
        }
        return function;
    }

    public boolean isEmpty() {
        return symbols.isEmpty();
    }

    public IFunction<?> put(FunctionSignature signature, IFunction<?> function) {
        this.signatures.put(signature, function);
        List<FunctionSignature> signatures = new ArrayList<FunctionSignature>();
        signatures.add(signature);
        this.symbols.put(signature.getSymbol(), signatures);
        return function;
    }

    public IFunction<?> remove(FunctionSignature signature) {
        IFunction<?> function = this.signatures.remove(signature);
        List<FunctionSignature> signaturesList = this.symbols.get(signature.getSymbol());
        if (signaturesList.isEmpty()) {
            this.symbols.remove(signature.getSymbol());
        }
        else {
            this.symbols.get(signature.getSymbol()).remove(signature);
        }
        return function;
    }

    public int size() {
        return this.signatures.size();
    }

    public ImmutableSet<FunctionSignature> getSignatures() {
        return ImmutableSet.copyOf(this.signatures.keySet());
    }

}
