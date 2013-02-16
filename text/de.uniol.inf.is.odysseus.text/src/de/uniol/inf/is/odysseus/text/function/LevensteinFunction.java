package de.uniol.inf.is.odysseus.text.function;

import org.apache.commons.lang.StringUtils;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;

/**
 * MEP function to compute the levenstein distance of a string
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class LevensteinFunction extends AbstractFunction<Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5254931226986934896L;
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			{ SDFDatatype.STRING }, { SDFDatatype.STRING } };

	@Override
	public int getArity() {
		return 2;
	}

	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException(
					"negative argument index not allowed");
		}
		if (argPos > this.getArity()) {
			throw new IllegalArgumentException(this.getSymbol() + " has only "
					+ this.getArity() + " argument(s): two strings");
		}
		return accTypes[argPos];
	}

	@Override
	public String getSymbol() {
		return "levenstein";
	}

	@Override
	public Integer getValue() {
		return StringUtils.getLevenshteinDistance(getInputValue(0).toString(),
				getInputValue(1).toString());
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFDatatype.INTEGER;
	}

}
