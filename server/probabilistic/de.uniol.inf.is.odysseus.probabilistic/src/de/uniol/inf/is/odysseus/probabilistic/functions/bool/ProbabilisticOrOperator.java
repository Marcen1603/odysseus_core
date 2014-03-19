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

import de.uniol.inf.is.odysseus.mep.IOperator;
import de.uniol.inf.is.odysseus.mep.functions.bool.AndOperator;
import de.uniol.inf.is.odysseus.probabilistic.base.common.ProbabilisticBooleanResult;
import de.uniol.inf.is.odysseus.probabilistic.functions.AbstractProbabilisticBinaryBooleanOperator;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticOrOperator extends AbstractProbabilisticBinaryBooleanOperator<ProbabilisticBooleanResult> {

    /**
     * 
     */
    private static final long serialVersionUID = -5335479850255317776L;

    /**
 * 
 */
    public ProbabilisticOrOperator() {
        super("||");
    }

    @Override
    public boolean isCommutative() {
        return true;
    }

    @Override
    public boolean isAssociative() {
        return true;
    }

    @Override
    public boolean isLeftDistributiveWith(final IOperator<ProbabilisticBooleanResult> operator) {
        return operator.getClass() == AndOperator.class;
    }

    @Override
    public boolean isRightDistributiveWith(final IOperator<ProbabilisticBooleanResult> operator) {
        return operator.getClass() == AndOperator.class;
    }

    @Override
    public int getPrecedence() {
        return 15;
    }

    @Override
    public de.uniol.inf.is.odysseus.mep.IOperator.ASSOCIATIVITY getAssociativity() {
        return ASSOCIATIVITY.LEFT_TO_RIGHT;
    }

    @Override
    public ProbabilisticBooleanResult getValue() {
        final ProbabilisticBooleanResult left = (ProbabilisticBooleanResult) this.getInputValue(0);
        final ProbabilisticBooleanResult right = (ProbabilisticBooleanResult) this.getInputValue(1);
        if (left.getProbability() >= right.getProbability()) {
            return left;
        }
        else {
            return right;
        }
    }

}
