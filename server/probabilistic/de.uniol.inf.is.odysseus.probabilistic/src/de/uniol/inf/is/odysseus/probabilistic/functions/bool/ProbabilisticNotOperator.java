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

    @Override
    public int getPrecedence() {
        return 3;
    }

    @Override
    public String getSymbol() {
        return "!";
    }

    @Override
    public ProbabilisticBooleanResult getValue() {
        ProbabilisticBooleanResult input = (ProbabilisticBooleanResult) getInputValue(0);
        return new ProbabilisticBooleanResult(input.getDistributions(), 1.0 - input.getProbability());
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFProbabilisticDatatype.PROBABILISTIC_BOOLEAN;
    }

    @Override
    public de.uniol.inf.is.odysseus.mep.IOperator.ASSOCIATIVITY getAssociativity() {
        return ASSOCIATIVITY.RIGHT_TO_LEFT;
    }

    public static final SDFDatatype[] accTypes = new SDFDatatype[] { SDFProbabilisticDatatype.PROBABILISTIC_BOOLEAN };

    @Override
    public SDFDatatype[] getAcceptedTypes(int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        if (argPos > this.getArity() - 1) {
            throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity() + " argument(s).");
        }
        return accTypes;
    }

}
