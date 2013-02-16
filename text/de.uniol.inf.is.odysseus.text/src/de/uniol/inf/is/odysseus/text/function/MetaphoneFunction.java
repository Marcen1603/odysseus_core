package de.uniol.inf.is.odysseus.text.function;

import org.apache.commons.codec.language.Metaphone;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;

/**
 * MEP function to compute the metaphone of a string
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class MetaphoneFunction extends AbstractFunction<String> {


	/**
	 * 
	 */
	private static final long serialVersionUID = -8470425457909284574L;
	private static final SDFDatatype[] accTypes = new SDFDatatype[] { SDFDatatype.STRING };

	@Override
	public int getArity() {
		return 1;
	}

	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException(
					"negative argument index not allowed");
		}
		if (argPos > 0) {
			throw new IllegalArgumentException(this.getSymbol() + " has only "
					+ this.getArity() + " argument(s).");
		}
		return accTypes;
	}

	@Override
	public String getSymbol() {
		return "metaphone";
	}

	@Override
	public String getValue() {
		Metaphone metaphone = new Metaphone();
		String metaphoneValue = metaphone
				.metaphone(getInputValue(0).toString());
		return metaphoneValue;
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFDatatype.STRING;
	}

}
