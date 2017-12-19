package de.uniol.inf.is.odysseus.mep.functions;

import java.util.List;

import de.uniol.inf.is.odysseus.core.ConversionOptions;
import de.uniol.inf.is.odysseus.core.conversion.CSVParser;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class SplitFunction extends AbstractFunction<List<String>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5795445731857931414L;
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			new SDFDatatype[] { SDFDatatype.STRING }, // Variable
			new SDFDatatype[] { SDFDatatype.STRING } // OUTPUT COUNT
			};
	private static final SDFDatatype[] returnTypes = new SDFDatatype[] {
			SDFDatatype.STRING};

	public SplitFunction() {
		super("Split",2,accTypes,SDFDatatype.LIST_STRING);
	}
	

	@Override
	public List<String> getValue() {
		List<String> ret = CSVParser.parseCSV((String)getInputValue(0), ConversionOptions.determineDelimiter((String)getInputValue(1)), true);
		return ret;
	}

	@Override
	public SDFDatatype getReturnType(int pos) {
		return returnTypes[0];
	}

}
