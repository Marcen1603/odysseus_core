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
import java.util.Objects;

import com.google.common.collect.ImmutableSet;

import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class FunctionStore {
    private static FunctionStore instance;
    private final Map<String, List<FunctionSignature>> symbols = new HashMap<String, List<FunctionSignature>>();
    private final Map<FunctionSignature, IMepFunction<?>> signatures = new HashMap<FunctionSignature, IMepFunction<?>>();
    private final List<Class<?>> deprecated = new ArrayList<Class<?>>();
    
    public static FunctionStore getInstance() {
        if (FunctionStore.instance == null) {
            FunctionStore.instance = new FunctionStore();
        }
        return FunctionStore.instance;
    }

    public void clear() {
        this.symbols.clear();
        this.signatures.clear();
    }

    public boolean containsSymbol(final String symbol) {
        if ((symbol != null) && (!symbol.isEmpty())) {
            return this.symbols.containsKey(symbol.toUpperCase());
        }
        return false;
    }

    public boolean containsSignature(final FunctionSignature signature) {
        return this.signatures.containsKey(signature);
    }

    public List<IMepFunction<?>> getFunctions(final String symbol) {
        final List<IMepFunction<?>> functions = new ArrayList<IMepFunction<?>>();
        if ((symbol != null) && (!symbol.isEmpty())) {
            final List<FunctionSignature> signatures = this.symbols.get(symbol.toUpperCase());
            for (final FunctionSignature signature : signatures) {
                functions.add(this.signatures.get(signature));
            }
        }
        return functions;
    }

    public IMepFunction<?> getFunction(final FunctionSignature signature) {
        return this.signatures.get(signature);
    }

    public IMepFunction<?> getFunction(final String symbol, final List<SDFDatatype> parameter) {
        IMepFunction<?> function = null;
        synchronized (symbols) {
            if ((symbol != null) && (!symbol.isEmpty())) {
                final List<FunctionSignature> signatureList = this.symbols.get(symbol.toUpperCase());
                for (final FunctionSignature signature : signatureList) {
                    if (signature.contains(parameter)) {
                        function = this.signatures.get(signature);
                        break;
                    }
                }
            }
        }
        return function;
    }

    public boolean isEmpty() {
        return this.symbols.isEmpty();
    }

    public IMepFunction<?> put(final FunctionSignature signature, final IMepFunction<?> function) {
        Objects.requireNonNull(signature);
        Objects.requireNonNull(function);
        Objects.requireNonNull(signature.getSymbol());
        synchronized (symbols) {
            if (this.symbols.containsKey(signature.getSymbol())) {
                this.symbols.get(signature.getSymbol()).add(signature);
            }
            else {
                final List<FunctionSignature> signatures = new ArrayList<FunctionSignature>();
                signatures.add(signature);
                this.symbols.put(signature.getSymbol(), signatures);
            }
            this.signatures.put(signature, function);
        }
        return function;
    }
    
    public void setDeprecated(final IMepFunction<?> function){
    	this.deprecated.add(function.getClass());
    }
    
    public boolean isDeprecated(final IMepFunction<?> function){
    	return deprecated.contains(function.getClass());
    }

    public synchronized IMepFunction<?> remove(final FunctionSignature signature) {
        Objects.requireNonNull(signature);
        IMepFunction<?> function = null;
        synchronized (symbols) {
            function = this.signatures.remove(signature);
            final List<FunctionSignature> signaturesList = this.symbols.get(signature.getSymbol());
            if (signaturesList.isEmpty()) {
                this.symbols.remove(signature.getSymbol());
            }
            else {
                this.symbols.get(signature.getSymbol()).remove(signature);
            }
        }
        deprecated.remove(function.getClass());
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
