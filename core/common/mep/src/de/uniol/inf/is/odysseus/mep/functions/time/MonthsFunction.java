/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.functions.time;

import java.util.Calendar;
import java.util.Date;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * Computes the month difference between two dates
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class MonthsFunction extends AbstractFunction<Integer> {

	/**
     * 
     */
	private static final long serialVersionUID = -4615281071966679283L;
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			{ SDFDatatype.DATE }, { SDFDatatype.DATE } };

	public MonthsFunction() {
    	super("months",2,accTypes, SDFDatatype.INTEGER);
    }

	@Override
	public Integer getValue() {
		Calendar a = Calendar.getInstance();
		a.setTime((Date) getInputValue(0));
		Calendar b = Calendar.getInstance();
		b.setTime((Date) getInputValue(1));
		int months = 0;
		while (a.compareTo(b) <= 0) {
			months++;
			a.add(Calendar.MONTH, 1);
		}
		if (months > 0) {
			months--;
		}
		return months;
	}


}
