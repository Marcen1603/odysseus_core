package de.uniol.inf.is.odysseus.text.function;

import org.apache.commons.codec.language.Soundex;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;

/**
 * MEP function to compute the soundex of a string
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class SoundexFunction extends AbstractFunction<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3286522655596260036L;
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
		return "soundex";
	}

	@Override
	public String getValue() {
		Soundex soundex = new Soundex();
		String soundexValue = soundex.soundex(getInputValue(0).toString());
		return soundexValue;
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFDatatype.STRING;
	}

}
