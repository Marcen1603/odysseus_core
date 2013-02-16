package de.uniol.inf.is.odysseus.text.function;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;
import org.apache.commons.codec.language.ColognePhonetic;
/**
 * MEP function to compute the cologne phonetic of a string
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ColognePhoneticFunction extends AbstractFunction<String> {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1265565609372371657L;
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
		return "colognephonetic";
	}

	@Override
	public String getValue() {
		ColognePhonetic colognePhonetic = new ColognePhonetic();
		String colognePhoneticValue = colognePhonetic.colognePhonetic(getInputValue(0).toString());
		return colognePhoneticValue;
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFDatatype.STRING;
	}

}
