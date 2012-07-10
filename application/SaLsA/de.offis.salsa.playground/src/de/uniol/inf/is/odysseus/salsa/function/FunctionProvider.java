/*******************************************************************************
 * Copyright 2012 The Odysseus Team
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
package de.uniol.inf.is.odysseus.salsa.function;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.core.server.mep.IFunctionProvider;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class FunctionProvider implements IFunctionProvider {
    public FunctionProvider() {
        System.out.println("+++++++++++++++++++++ adding SaLsA functions. ++++++++++++++++++++++");
    }

    @Override
    public List<IFunction<?>> getFunctions() {

        final List<IFunction<?>> functions = new ArrayList<IFunction<?>>();
        functions.add(new ExtractSegments());
        functions.add(new IEPF());

        return functions;
    }
}
