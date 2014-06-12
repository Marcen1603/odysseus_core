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

package de.uniol.inf.is.odysseus.probabilistic.functions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 * @param <T>
 */
public abstract class AbstractProbabilisticBinaryBooleanOperator<T> extends AbstractProbabilisticBinaryOperator<T> {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2604513567977149416L;

    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { new SDFDatatype[] { SDFProbabilisticDatatype.PROBABILISTIC_RESULT },
            new SDFDatatype[] { SDFProbabilisticDatatype.PROBABILISTIC_RESULT } };

    public AbstractProbabilisticBinaryBooleanOperator(final String symbol) {
        super(symbol, AbstractProbabilisticBinaryBooleanOperator.accTypes, SDFProbabilisticDatatype.PROBABILISTIC_RESULT);
    }

}
