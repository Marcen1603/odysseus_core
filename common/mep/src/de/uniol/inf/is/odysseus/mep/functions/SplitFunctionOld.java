package de.uniol.inf.is.odysseus.mep.functions;

import java.util.Arrays;
import java.util.List;

import de.uniol.inf.is.odysseus.core.conversion.CSVParser;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

@Deprecated
public class SplitFunctionOld extends AbstractFunction<List<String>> {

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

	public SplitFunctionOld() {
		super("Split",3,accTypes,SDFDatatype.LIST_STRING);
	}
	

	@Override
	public List<String> getValue() {
		String[] v = CSVParser.parseCSV((String)getInputValue(0), CSVParser.determineDelimiter((String)getInputValue(1)), true);
		List<String> ret = Arrays.asList(v);
		return ret;
	}

	@Override
	public SDFDatatype getReturnType(int pos) {
		return returnTypes[0];
	}

}
