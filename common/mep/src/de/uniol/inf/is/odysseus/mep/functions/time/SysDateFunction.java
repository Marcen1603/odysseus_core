/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.functions.time;

import java.util.Calendar;
import java.util.Date;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * Returns the application time
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * @deprecated Use {@link CurDateFunction curDate}
 */
@Deprecated
public class SysDateFunction extends AbstractFunction<Date> {

    private static final long serialVersionUID = -3364300386518966013L;

    public SysDateFunction() {
        super("sysdate", 0, null, SDFDatatype.DATE, false);
    }

    @Override
    public Date getValue() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

}
