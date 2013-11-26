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
	private static FunctionStore instance;
    Map<String, List<FunctionSignature>> symbols    = new HashMap<String, List<FunctionSignature>>();
    Map<FunctionSignature, IFunction<?>> signatures = new HashMap<FunctionSignature, IFunction<?>>();
 
	public static FunctionStore getInstance() {
		if (instance == null) {
			instance = new FunctionStore();
		}
		return instance;
	}

    public void clear() {
        symbols.clear();
        signatures.clear();
    }

    public boolean containsSymbol(String symbol) {
		if ((symbol != null) && (!symbol.isEmpty())) {
			return symbols.containsKey(symbol.toUpperCase());
		} else {
			return false;
		}
    }

    public boolean containsSignature(FunctionSignature signature) {
        return signatures.containsKey(signature);
    }

    public List<IFunction<?>> getFunctions(String symbol) {
        List<IFunction<?>> functions = new ArrayList<IFunction<?>>();
		if ((symbol != null) && (!symbol.isEmpty())) {
			List<FunctionSignature> signatures = symbols.get(symbol.toUpperCase());
			for (FunctionSignature signature : signatures) {
				functions.add(this.signatures.get(signature));
			}
		}
        return functions;
    }

    public IFunction<?> getFunction(FunctionSignature signature) {
        return this.signatures.get(signature);
    }

    public IFunction<?> getFunction(String symbol, List<SDFDatatype> parameter) {
        IFunction<?> function = null;
		if ((symbol != null) && (!symbol.isEmpty())) {
			List<FunctionSignature> signatureList = this.symbols.get(symbol.toUpperCase());
			for (FunctionSignature signature : signatureList) {
				if (signature.contains(parameter)) {
					function = this.signatures.get(signature);
					break;
				}
			}
		}
        return function;
    }

    public boolean isEmpty() {
        return symbols.isEmpty();
    }

    public IFunction<?> put(FunctionSignature signature, IFunction<?> function) {
		if (this.symbols.containsKey(signature.getSymbol())) {
			this.symbols.get(signature.getSymbol()).add(signature);
		} else {
			List<FunctionSignature> signatures = new ArrayList<FunctionSignature>();
			signatures.add(signature);
			this.symbols.put(signature.getSymbol(), signatures);
		}
	    this.signatures.put(signature, function);
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

    private FunctionStore() {

    }
}
