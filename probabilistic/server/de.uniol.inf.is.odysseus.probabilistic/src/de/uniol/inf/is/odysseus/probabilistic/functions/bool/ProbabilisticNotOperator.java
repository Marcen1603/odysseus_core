/********************************************************************************** 
 * Copyright 2014 The Odysseus Team
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
package de.uniol.inf.is.odysseus.probabilistic.functions.bool;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.probabilistic.base.common.ProbabilisticBooleanResult;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.probabilistic.functions.AbstractProbabilisticUnaryOperator;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticNotOperator extends AbstractProbabilisticUnaryOperator<ProbabilisticBooleanResult> {
    /**
     * 
     */
    private static final long serialVersionUID = -5355975430336339430L;
    public static final SDFDatatype[] accTypes = new SDFDatatype[] { SDFProbabilisticDatatype.PROBABILISTIC_RESULT };

    /**
 * 
 */
    public ProbabilisticNotOperator() {
        super("!", ProbabilisticNotOperator.accTypes, SDFProbabilisticDatatype.PROBABILISTIC_RESULT);
    }

    @Override
    public int getPrecedence() {
        return 3;
    }

    @Override
    public ProbabilisticBooleanResult getValue() {
        final ProbabilisticBooleanResult input = (ProbabilisticBooleanResult) this.getInputValue(0);
        return new ProbabilisticBooleanResult(input.getDistributions(), 1.0 - input.getProbability());
    }

    @Override
    public de.uniol.inf.is.odysseus.mep.IOperator.ASSOCIATIVITY getAssociativity() {
        return ASSOCIATIVITY.RIGHT_TO_LEFT;
    }

}
