/*
 * Copyright 2013 The Odysseus Team
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

package de.uniol.inf.is.odysseus.datatype.interval;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.datatype.interval.function.IntervalDifferenceFunction;
import de.uniol.inf.is.odysseus.datatype.interval.function.IntervalDivisionOperator;
import de.uniol.inf.is.odysseus.datatype.interval.function.IntervalIntersectionFunction;
import de.uniol.inf.is.odysseus.datatype.interval.function.IntervalMinusOperator;
import de.uniol.inf.is.odysseus.datatype.interval.function.IntervalMultiplicationOperator;
import de.uniol.inf.is.odysseus.datatype.interval.function.IntervalPlusOperator;
import de.uniol.inf.is.odysseus.datatype.interval.function.IntervalPowerOperator;
import de.uniol.inf.is.odysseus.datatype.interval.function.IntervalUnionFunction;
import de.uniol.inf.is.odysseus.datatype.interval.function.ToIntervalFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class IntervalFunctionProvider implements IFunctionProvider {
    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(IntervalFunctionProvider.class);

    public IntervalFunctionProvider() {

    }

    @Override
    public List<IMepFunction<?>> getFunctions() {

        final List<IMepFunction<?>> functions = new ArrayList<IMepFunction<?>>();

        functions.add(new IntervalMinusOperator());
        functions.add(new IntervalPlusOperator());
        functions.add(new IntervalMultiplicationOperator());
        functions.add(new IntervalDivisionOperator());
        functions.add(new IntervalPowerOperator());

        functions.add(new IntervalUnionFunction());
        functions.add(new IntervalIntersectionFunction());
        functions.add(new IntervalDifferenceFunction());

        functions.add(new ToIntervalFunction());
        // ProbabilisticFunctionProvider.LOG.info(String.format(
        // "Register functions: %s", functions));

        return functions;
    }
}
