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
package de.uniol.inf.is.odysseus.wrapper.opcda;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;
import de.uniol.inf.is.odysseus.wrapper.opcda.function.ErrorFunction;
import de.uniol.inf.is.odysseus.wrapper.opcda.function.QualityFunction;
import de.uniol.inf.is.odysseus.wrapper.opcda.function.TimestampFunction;
import de.uniol.inf.is.odysseus.wrapper.opcda.function.ValueFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class OPCDAFunctionProvider implements IFunctionProvider {
    private static final Logger LOG = LoggerFactory.getLogger(OPCDAFunctionProvider.class);

    public OPCDAFunctionProvider() {

    }

    /**
     *
     * {@inheritDoc}
     */
    @SuppressWarnings("rawtypes")
	@Override
    public List<IMepFunction<?>> getFunctions() {

        final List<IMepFunction<?>> functions = new ArrayList<>();
        try {
            functions.add(new ErrorFunction());
            functions.add(new QualityFunction());
            functions.add(new ValueFunction());
            functions.add(new TimestampFunction());

            // Needs some work, currently only creates OpcValue<Double>
            // removed
            //functions.add(new ToOPCValueFunction());
        }
        catch (final Exception e) {
            OPCDAFunctionProvider.LOG.error(e.getMessage(), e);
        }
        return functions;
    }

}
