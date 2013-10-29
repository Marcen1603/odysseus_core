package de.uniol.inf.is.odysseus.core.server.mep.functions.time;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;

public class ToDateFunction extends AbstractFunction<String> {

	private static final long serialVersionUID = -5061294726713702455L;
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			new SDFDatatype[] { SDFDatatype.STRING },
			new SDFDatatype[] { SDFDatatype.LONG } };
	private SimpleDateFormat dateFormat;

	@Override
	public int getArity() {
		return 2;
	}

	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos) {
		return accTypes[argPos];
	}

	@Override
	public String getSymbol() {
		return "toDate";
	}

	@Override
	public String getValue() {
		if (dateFormat == null){
			dateFormat = new SimpleDateFormat((String) getInputValue(0));
		}
		Date d = new Date(Math.round(getNumericalInputValue(1)));
		return dateFormat.format(d);
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFDatatype.STRING;
	}

}
