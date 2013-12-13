/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.functions.transform;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class DateToStringFunction extends AbstractFunction<String> {

    /**
     * 
     */
    private static final long serialVersionUID = -4863774536996054256L;
    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { new SDFDatatype[] { SDFDatatype.DATE }, new SDFDatatype[] { SDFDatatype.STRING } };

    @Override
    public int getArity() {
        return 2;
    }

    @Override
    public SDFDatatype[] getAcceptedTypes(int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        if (argPos >= this.getArity()) {
            throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity() + " argument(s): a date and a date format");
        }
        return accTypes[argPos];
    }

    @Override
    public String getSymbol() {
        return "toString";
    }

    @Override
    public String getValue() {
        Date date = (Date) getInputValue(0);
        String dateFormatString = (String) getInputValue(1);
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatString);
        return dateFormat.format(date);
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFDatatype.STRING;
    }

}
