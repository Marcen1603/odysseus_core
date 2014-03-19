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
import de.uniol.inf.is.odysseus.mep.AbstractBinaryOperator;
import de.uniol.inf.is.odysseus.mep.IOperator;
import de.uniol.inf.is.odysseus.mep.functions.bool.OrOperator;
import de.uniol.inf.is.odysseus.probabilistic.base.common.ProbabilisticBooleanResult;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.IMultivariateDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class ProbabilisticAndOperator     extends AbstractBinaryOperator<ProbabilisticBooleanResult> {
// Assume input is a mixture with a scale factor and return the distribution with the lower scale factor
    // I need the diff between the scale factors!!!!!
    
    // A new class called "intermediate result" can hold the integration result and all boolean prob. operator just work on that class
    // The class holds the resulting distribution and the integration result.




        /**
     * 
     */
    private static final long serialVersionUID = -3269321870083257533L;

        @Override
        public boolean isCommutative() {
            return true;
        }

        @Override
        public boolean isAssociative() {
            return true;
        }

        @Override
        public boolean isLeftDistributiveWith(IOperator<ProbabilisticBooleanResult> operator) {
            return operator.getClass() == OrOperator.class;
        }

        @Override
        public boolean isRightDistributiveWith(IOperator<ProbabilisticBooleanResult> operator) {
            return operator.getClass() == OrOperator.class;
        }

        @Override
        public int getPrecedence() {
            return 13;
        }

        @Override
        public de.uniol.inf.is.odysseus.mep.IOperator.ASSOCIATIVITY getAssociativity() {
            return ASSOCIATIVITY.LEFT_TO_RIGHT;
        }

        @Override
        public String getSymbol() {
            return "&&";
        }

        @Override
        public ProbabilisticBooleanResult getValue() {
            ProbabilisticBooleanResult left = (ProbabilisticBooleanResult) getInputValue(0);
            ProbabilisticBooleanResult right = (ProbabilisticBooleanResult) getInputValue(1);    
                return new ProbabilisticBooleanResult(new IMultivariateDistribution[]{left.getDistribution(),right.getDistribution()}, left.getProbability()*right.getProbability());  

        }

        @Override
        public SDFDatatype getReturnType() {
            return SDFProbabilisticDatatype.PROBABILISTIC_BOOLEAN;
        }

        public static final SDFDatatype[] accTypes = new SDFDatatype[]{ SDFProbabilisticDatatype.PROBABILISTIC_BOOLEAN };
        
        @Override
        public SDFDatatype[] getAcceptedTypes(int argPos){
            if(argPos < 0){
                throw new IllegalArgumentException("negative argument index not allowed");
            }
            if(argPos > this.getArity()-1){
                throw new IllegalArgumentException(this.getSymbol() + " has only " +this.getArity() + " argument(s).");
            }
            return accTypes;
        }
}
