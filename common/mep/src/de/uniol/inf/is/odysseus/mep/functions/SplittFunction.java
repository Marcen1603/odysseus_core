package de.uniol.inf.is.odysseus.mep.functions;

import java.util.Arrays;
import java.util.List;

import de.uniol.inf.is.odysseus.core.conversion.CSVParser;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class SplittFunction extends AbstractFunction<List<String>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5795445731857931414L;
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			new SDFDatatype[] { SDFDatatype.STRING }, // Variable
			new SDFDatatype[] { SDFDatatype.STRING }, // Delimiter
			new SDFDatatype[] { SDFDatatype.LONG } // OUTPUT COUNT
			};
	private static final SDFDatatype[] returnTypes = new SDFDatatype[] {
			SDFDatatype.STRING};

	@Override
	public int getArity() {
		return 3;
	}

	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos) {
		return accTypes[argPos];
	}

	@Override
	public String getSymbol() {
		return "Split";
	}

	@Override
	public List<String> getValue() {
		String[] v = CSVParser.parseCSV((String)getInputValue(0), ((String)getInputValue(1)).toCharArray()[0], true);
		List<String> ret = Arrays.asList(v);
		return ret;
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFDatatype.TUPLE;
	}

	@Override
	public int getReturnTypeCard() {
		return getNumericalInputValue(2).intValue();
	}

	@Override
	public SDFDatatype getReturnType(int pos) {
		return returnTypes[0];
	}

}
