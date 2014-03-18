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

    private static final long serialVersionUID = -2548802151737120316L;
    private static SDFDatatype[][] accTypes = new SDFDatatype[][]{{SDFDatatype.DATE},{SDFDatatype.DATE}}; 
    
    public DateMinusOperator() {
    	super("-", accTypes, SDFDatatype.DATE);
    }
    
    @Override
    public int getPrecedence() {
        return 6;
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

}
