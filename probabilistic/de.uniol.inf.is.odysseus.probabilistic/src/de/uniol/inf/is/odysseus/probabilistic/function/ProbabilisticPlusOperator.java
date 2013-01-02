package de.uniol.inf.is.odysseus.probabilistic.function;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractBinaryOperator;
import de.uniol.inf.is.odysseus.core.server.mep.IOperator;
import de.uniol.inf.is.odysseus.probabilistic.datatype.ProbabilisticDouble;

public class ProbabilisticPlusOperator extends AbstractBinaryOperator<ProbabilisticDouble> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4132289779552462641L;

	@Override
    public boolean isCommutative() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isAssociative() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isLeftDistributiveWith(IOperator<ProbabilisticDouble> operator) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isRightDistributiveWith(IOperator<ProbabilisticDouble> operator) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int getPrecedence() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public de.uniol.inf.is.odysseus.core.server.mep.IOperator.ASSOCIATIVITY getAssociativity() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SDFDatatype[] getAcceptedTypes(int argPos) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getSymbol() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ProbabilisticDouble getValue() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SDFDatatype getReturnType() {
        // TODO Auto-generated method stub
        return null;
    }

}
