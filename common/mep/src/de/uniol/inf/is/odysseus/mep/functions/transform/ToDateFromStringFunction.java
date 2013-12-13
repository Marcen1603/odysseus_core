package de.uniol.inf.is.odysseus.mep.functions.transform;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ToDateFromStringFunction extends AbstractFunction<Date> {

    /**
     * 
     */
    private static final long serialVersionUID = 6255887477026357429L;
    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { new SDFDatatype[] { SDFDatatype.STRING }, new SDFDatatype[] { SDFDatatype.STRING } };

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
            throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity() + " argument(s): a date string and a date format string");
        }
        return accTypes[argPos];
    }

    @Override
    public String getSymbol() {
        return "toDate";
    }

    @Override
    public Date getValue() {
        String dateString = getInputValue(0).toString();
        String dateFormatString = getInputValue(1).toString();
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatString);
        try {
            return dateFormat.parse(dateString);
        }
        catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFDatatype.DATE;
    }

}
