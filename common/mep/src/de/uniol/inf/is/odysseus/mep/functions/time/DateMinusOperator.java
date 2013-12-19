/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.functions.time;

import java.util.Date;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractBinaryOperator;
import de.uniol.inf.is.odysseus.mep.IOperator;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class DateMinusOperator extends AbstractBinaryOperator<Date> {

    /**
     * 
     */
    private static final long serialVersionUID = -2548802151737120316L;

    @Override
    public int getPrecedence() {
        return 6;
    }

    @Override
    public String getSymbol() {
        return "-";
    }

    @Override
    public Date getValue() {
        Date a = (Date) this.getInputValue(0);
        Date b = (Date) this.getInputValue(1);
        return getValueInternal(a, b);
    }

    protected Date getValueInternal(Date a, Date b) {
        return new Date(a.getTime() - b.getTime());
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFDatatype.DATE;
    }

    @Override
    public de.uniol.inf.is.odysseus.mep.IOperator.ASSOCIATIVITY getAssociativity() {
        return ASSOCIATIVITY.LEFT_TO_RIGHT;
    }

    @Override
    public boolean isCommutative() {
        return false;
    }

    @Override
    public boolean isAssociative() {
        return false;
    }

    @Override
    public boolean isLeftDistributiveWith(IOperator<Date> operator) {
        return false;
    }

    @Override
    public boolean isRightDistributiveWith(IOperator<Date> operator) {
        return false;
    }

    public static final SDFDatatype[] accTypes = new SDFDatatype[] { SDFDatatype.DATE };

    @Override
    public SDFDatatype[] getAcceptedTypes(int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        if (argPos >= this.getArity()) {
            throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity() + " argument(s).");
        }
        return accTypes;
    }
}
